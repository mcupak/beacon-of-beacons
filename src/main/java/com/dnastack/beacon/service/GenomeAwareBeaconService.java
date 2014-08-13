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

/**
 * Abstract beacon service handling multiple genome specific queries.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public abstract class GenomeAwareBeaconService implements BeaconService {

    protected abstract String[] getRefs();

    @Override
    public BeaconResponse executeQuery(Beacon beacon, Query query) {
        BeaconResponse res = new BeaconResponse(beacon, query, null);

        // TODO: parallelize
        for (String ref : getRefs()) {
            Boolean r = parseQueryResponse(getQueryResponse(beacon, query, ref));
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

        return res;
    }
}
