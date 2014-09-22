/*
 * The MIT License
 *
 * Copyright 2014 Miroslav Cupak (mirocupak@gmail.com).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.dnastack.beacon.service;

import com.dnastack.beacon.dao.BeaconDao;
import com.dnastack.beacon.dao.QueryDao;
import com.dnastack.beacon.dto.BeaconTo;
import com.dnastack.beacon.dto.QueryTo;
import com.dnastack.beacon.log.Logged;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Implementation of a service for managing beacon responses.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Stateless
@LocalBean
public class BeaconResponseServiceImpl implements BeaconResponseService {

    @Inject
    private BeaconDao beaconDao;

    @Inject
    private QueryDao queryDao;

    @Asynchronous
    private Future<Boolean> queryBeacon(BeaconTo b, QueryTo q) {
        Boolean total = null;

        if (beaconDao.isAgregator(b)) {
            total = false;

            // execute queries in parallel
            Map<BeaconTo, Future<Boolean>> futures = new HashMap<>();
            for (BeaconTo bt : beaconDao.getAggregatees(b)) {
                futures.put(bt, beaconDao.getProcessor(bt).executeQuery(bt, q));
            }

            // collect results
            for (Entry<BeaconTo, Future<Boolean>> e : futures.entrySet()) {
                Boolean res = null;
                try {
                    res = e.getValue().get();
                } catch (InterruptedException | ExecutionException ex) {
                    // ignore, response already null
                }
                if (res != null && res) {
                    total = true;
                }
            }
        } else {
            try {
                total = beaconDao.getProcessor(b).executeQuery(b, q).get();
            } catch (InterruptedException | ExecutionException ex) {
                // ignore
            }
        }

        return new AsyncResult<>(total);

    }

    @Logged
    @Override
    public BeaconResponse queryBeacon(String beaconId, String chrom, Long pos, String allele, String ref) {
        QueryTo q = queryDao.getQuery(chrom, pos, allele, ref);

        BeaconTo b = beaconDao.getVisibleBeacon(beaconId);
        if (b == null) {
            // nonexisting beaconId param specified
            return new BeaconResponse(new BeaconTo(null, "invalid beacon"), q, null);
        }

        BeaconResponse br = new BeaconResponse(b, q, null);

        if (queryDao.checkIfQuerySuccessfullyNormalizedAndValid(q, ref)) {
            return br;
        }

        try {
            br.setResponse(queryBeacon(b, q).get());
        } catch (InterruptedException | ExecutionException ex) {
            // ignore, response already null
        }

        return br;
    }

    private Map<BeaconTo, BeaconResponse> setUpBeaconResponseMap(Collection<String> beaconIds, QueryTo q) {
        Map<BeaconTo, BeaconResponse> brs = new HashMap<>();
        if (beaconIds == null) {
            // fetch all beacons
            Collection<BeaconTo> beacons = beaconDao.getVisibleBeacons();
            for (BeaconTo b : beacons) {
                brs.put(b, new BeaconResponse(b, q, null));
            }
        } else {
            for (String id : beaconIds) {
                BeaconTo b = beaconDao.getVisibleBeacon(id);
                if (b != null) {
                    brs.put(b, new BeaconResponse(b, q, null));
                }
            }
        }

        return brs;
    }

    private Map<BeaconTo, BeaconResponse> fillBeaconResponseMap(Map<BeaconTo, BeaconResponse> brs, QueryTo q) {
        // execute queries in parallel
        Map<BeaconTo, Future<Boolean>> futures = new HashMap<>();
        for (BeaconTo b : brs.keySet()) {
            futures.put(b, queryBeacon(b, q));
        }

        // collect results
        for (Entry<BeaconTo, Future<Boolean>> e : futures.entrySet()) {
            Boolean b = null;
            try {
                b = e.getValue().get();
            } catch (InterruptedException | ExecutionException ex) {
                // ignore, response already null
            }
            if (b != null) {
                brs.get(e.getKey()).setResponse(b);
            }
        }

        return brs;
    }

    private Collection<BeaconResponse> queryMultipleBeacons(Collection<String> beaconIds, String chrom, Long pos, String allele, String ref) {
        QueryTo q = queryDao.getQuery(chrom, pos, allele, ref);

        // init to create a response for each beacon even if the query is invalid
        Map<BeaconTo, BeaconResponse> brs = setUpBeaconResponseMap(beaconIds, q);

        // validate query
        if (queryDao.checkIfQuerySuccessfullyNormalizedAndValid(q, ref)) {
            return brs.values();
        }

        return fillBeaconResponseMap(brs, q).values();
    }

    @Logged
    @Override
    public Collection<BeaconResponse> queryBeacons(Collection<String> beaconIds, String chrom, Long pos, String allele, String ref) {
        if (beaconIds == null) {
            return new HashSet<>();
        }

        return queryMultipleBeacons(beaconIds, chrom, pos, allele, ref);
    }

    @Logged
    @Override
    public Collection<BeaconResponse> queryAll(String chrom, Long pos, String allele, String ref) {
        return queryMultipleBeacons(null, chrom, pos, allele, ref);
    }

}
