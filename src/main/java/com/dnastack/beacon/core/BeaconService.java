/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dnastack.beacon.core;

import java.util.concurrent.Future;

/**
 * Beacon query service.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public interface BeaconService {

    /**
     * Asynchronously executes a query agaist a beacon.
     *
     * @param beacon beacon
     * @param query query representation
     * @return response containing the beacon, the query and true/false according to the beacons response (or null if
     * the valid response could not be obtained)
     */
    Future<BeaconResponse> executeQuery(Beacon beacon, Query query);

    /**
     * Asynchronously xtracts beacon response value from the given raw query reponse.
     *
     * @param response response
     * @return true/false for valid values, null otherwise
     */
    Future<Boolean> parseQueryResponse(String response);

    /**
     * Asynchronously obtains raw response to the query from the beacon.
     *
     * @param beacon beacon to query
     * @param query query
     * @param ref reference genome (optional)
     * @return raw result of the query from the beacon
     */
    Future<String> getQueryResponse(Beacon beacon, Query query, String ref);

}
