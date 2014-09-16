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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.inject.Inject;

/**
 * Implementation of a service for managing beacon responses.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class BeaconResponseServiceImpl implements BeaconResponseService {

    @Inject
    private BeaconDao beaconDao;

    @Inject
    private QueryDao queryDao;

    @Logged
    @Override
    public BeaconResponse queryBob(String chrom, Long pos, String allele, String ref) {
        QueryTo q = queryDao.getQuery(chrom, pos, allele, ref);
        BeaconResponse br = new BeaconResponse(beaconDao.getBob(), q, null);

        if (queryDao.checkIfQuerySuccessfullyNormalizedAndValid(q, ref)) {
            return br;
        }
        br.setResponse(false);

        // execute queries in parallel
        List<Future<Boolean>> futures = new ArrayList<>();
        for (BeaconTo b : beaconDao.getAllBeacons()) {
            futures.add(beaconDao.getProcessor(b).executeQuery(b, q));
        }

        // collect results
        for (Future<Boolean> r : futures) {
            Boolean res = null;
            try {
                res = r.get();
            } catch (InterruptedException | ExecutionException ex) {
                // ignore, response already null
            }
            if (res != null && res) {
                br.setResponse(true);
            }
        }

        return br;
    }

    @Logged
    @Override
    public BeaconResponse queryBeacon(String beaconId, String chrom, Long pos, String allele, String ref) {
        QueryTo q = queryDao.getQuery(chrom, pos, allele, ref);

        BeaconTo b = beaconDao.getBeacon(beaconId);
        if (b == null) {
            // nonexisting beaconId param specified
            return new BeaconResponse(new BeaconTo(null, "invalid beacon"), q, null);
        }

        BeaconResponse br = new BeaconResponse(b, q, null);
        if (queryDao.checkIfQuerySuccessfullyNormalizedAndValid(q, ref)) {
            return br;
        }

        try {
            br.setResponse(beaconDao.getProcessor(b).executeQuery(b, q).get());
        } catch (InterruptedException | ExecutionException ex) {
            // ignore
        }
        return br;
    }

    @Logged
    @Override
    public Collection<BeaconResponse> queryAll(String chrom, Long pos, String allele, String ref) {
        QueryTo q = queryDao.getQuery(chrom, pos, allele, ref);

        // init to create a response for each beacon even if the query is invalid
        Map<BeaconTo, BeaconResponse> brs = new HashMap<>();
        for (BeaconTo b : beaconDao.getAllBeacons()) {
            brs.put(b, new BeaconResponse(b, q, null));
        }

        // validate query
        if (queryDao.checkIfQuerySuccessfullyNormalizedAndValid(q, ref)) {
            return brs.values();
        }

        // execute queries in parallel
        Map<BeaconTo, Future<Boolean>> futures = new HashMap<>();
        for (BeaconTo b : beaconDao.getAllBeacons()) {
            futures.put(b, beaconDao.getProcessor(b).executeQuery(b, q));
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

        return brs.values();
    }

}
