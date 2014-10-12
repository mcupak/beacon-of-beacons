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
package com.dnastack.bob.service;

import com.dnastack.bob.dao.BeaconDao;
import com.dnastack.bob.dao.QueryDao;
import com.dnastack.bob.dto.BeaconResponseTo;
import com.dnastack.bob.entity.Beacon;
import com.dnastack.bob.entity.BeaconResponse;
import com.dnastack.bob.entity.Query;
import com.dnastack.bob.log.Logged;
import com.dnastack.bob.util.Entity2ToConvertor;
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
import javax.validation.Validator;

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

    @Inject
    private Validator validator;

    private boolean checkIfQuerySuccessfullyNormalizedAndValid(Query q, String ref) {
        return (!(ref == null || ref.isEmpty()) && q.getReference() == null) || !validator.validate(q).isEmpty();
    }

    @Asynchronous
    private Future<Boolean> queryBeacon(Beacon b, Query q) {
        Boolean total = null;

        if (b.isAggregator()) {
            total = false;

            // execute queries in parallel
            Map<Beacon, Future<Boolean>> futures = new HashMap<>();
            for (Beacon bt : beaconDao.getAggregatees(b)) {
                futures.put(bt, bt.getProcessor().executeQuery(bt, q));
            }

            // collect results
            for (Entry<Beacon, Future<Boolean>> e : futures.entrySet()) {
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
                total = b.getProcessor().executeQuery(b, q).get();
            } catch (InterruptedException | ExecutionException ex) {
                // ignore
            }
        }

        return new AsyncResult<>(total);

    }

    private Map<Beacon, BeaconResponse> setUpBeaconResponseMap(Collection<String> beaconIds, Query q) {
        Map<Beacon, BeaconResponse> brs = new HashMap<>();
        if (beaconIds == null) {
            // fetch all beacons
            Collection<Beacon> beacons = beaconDao.getVisibleBeacons();
            for (Beacon b : beacons) {
                brs.put(b, new BeaconResponse(b, q, null));
            }
        } else {
            for (String id : beaconIds) {
                Beacon b = beaconDao.getVisibleBeacon(id);
                if (b != null) {
                    brs.put(b, new BeaconResponse(b, q, null));
                }
            }
        }

        return brs;
    }

    private Map<Beacon, BeaconResponse> fillBeaconResponseMap(Map<Beacon, BeaconResponse> brs, Query q) {
        // execute queries in parallel
        Map<Beacon, Future<Boolean>> futures = new HashMap<>();
        for (Beacon b : brs.keySet()) {
            futures.put(b, queryBeacon(b, q));
        }

        // collect results
        for (Entry<Beacon, Future<Boolean>> e : futures.entrySet()) {
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
        Query q = queryDao.getQuery(chrom, pos, allele, ref);

        // init to create a response for each beacon even if the query is invalid
        Map<Beacon, BeaconResponse> brs = setUpBeaconResponseMap(beaconIds, q);

        // validate query
        if (checkIfQuerySuccessfullyNormalizedAndValid(q, ref)) {
            return brs.values();
        }

        return fillBeaconResponseMap(brs, q).values();
    }

    @Logged
    @Override
    public BeaconResponseTo queryBeacon(String beaconId, String chrom, Long pos, String allele, String ref) {
        Query q = queryDao.getQuery(chrom, pos, allele, ref);

        Beacon b = beaconDao.getVisibleBeacon(beaconId);
        if (b == null) {
            // nonexisting beaconId param specified
            return Entity2ToConvertor.getBeaconResponseTo(new BeaconResponse(new Beacon(null, "invalid beacon"), q, null));
        }

        BeaconResponse br = new BeaconResponse(b, q, null);
        if (checkIfQuerySuccessfullyNormalizedAndValid(q, ref)) {
            return Entity2ToConvertor.getBeaconResponseTo(br);
        }

        try {
            br.setResponse(queryBeacon(b, q).get());
        } catch (InterruptedException | ExecutionException ex) {
            // ignore, response already null
        }

        return Entity2ToConvertor.getBeaconResponseTo(br);
    }

    @Logged
    @Override
    public Collection<BeaconResponseTo> queryBeacons(Collection<String> beaconIds, String chrom, Long pos, String allele, String ref) {
        if (beaconIds == null) {
            return new HashSet<>();
        }

        return Entity2ToConvertor.getBeaconResponseTos(queryMultipleBeacons(beaconIds, chrom, pos, allele, ref));
    }

    @Logged
    @Override
    public Collection<BeaconResponseTo> queryAll(String chrom, Long pos, String allele, String ref) {
        return Entity2ToConvertor.getBeaconResponseTos(queryMultipleBeacons(null, chrom, pos, allele, ref));
    }

}
