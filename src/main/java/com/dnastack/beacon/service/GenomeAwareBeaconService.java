/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dnastack.beacon.service;

import com.dnastack.beacon.core.Beacon;
import com.dnastack.beacon.core.BeaconResponse;
import com.dnastack.beacon.core.BeaconService;
import com.dnastack.beacon.core.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.ejb.AsyncResult;

/**
 * Abstract beacon service handling multiple genome specific queries.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public abstract class GenomeAwareBeaconService implements BeaconService {

    protected abstract String[] getRefs();

    @Override
    public Future<BeaconResponse> executeQuery(Beacon beacon, Query query) {
        BeaconResponse res = new BeaconResponse(beacon, query, null);

        // execute queries in parallel
        List<Future<String>> fs = new ArrayList<>();
        for (String ref : getRefs()) {
            fs.add(getQueryResponse(beacon, query, ref));
        }

        // parse queries in parallel
        List<Future<Boolean>> bs = new ArrayList<>();
        for (Future<String> f : fs) {
            try {
                bs.add(parseQueryResponse(f.get()));
            } catch (InterruptedException | ExecutionException ex) {
                // ignore
            }
        }

        // collect results
        for (Future<Boolean> b : bs) {
            Boolean r = null;
            try {
                r = b.get();
            } catch (InterruptedException | ExecutionException ex) {
                // ignore, already null
            }
            if (r != null) {
                if (r) {
                    res.setResponse(r);
                    break;
                } else {
                    if (res.getResponse() == null) {
                        res.setResponse(r);
                    }
                }
            }
        }

        return new AsyncResult<>(res);
    }
}
