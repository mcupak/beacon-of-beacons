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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.ejb.AsyncResult;

/**
 * Abstract beacon service not requiring genome specification.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public abstract class GenomeUnawareBeaconService implements BeaconService {

    @Override
    public Future<BeaconResponse> executeQuery(Beacon beacon, Query query) {
        BeaconResponse res = new BeaconResponse(beacon, query, null);

        try {
            res.setResponse(parseQueryResponse(getQueryResponse(beacon, query, null).get()).get());
        } catch (InterruptedException | ExecutionException ex) {
            // ignore, response already null
        }

        return new AsyncResult<>(res);
    }
}
