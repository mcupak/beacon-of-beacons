/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dnastack.beacon.core;

/**
 * Beacon query service.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public interface BeaconService {

    /**
     * Executes a query agaist a beacon.
     *
     * @param beacon beacon
     * @param query query representation
     * @return response containing the beacon, the query and true/false according to the beacons response (or null if
     * the valid response could not be obtained)
     */
    BeaconResponse executeQuery(Beacon beacon, Query query);

    /**
     * Extracts beacon response value from the given raw query reponse.
     *
     * @param response response
     * @return true/false for valid values, null otherwise
     */
    Boolean parseQueryResponse(String response);

    /**
     * Obtains raw response to the query from the beacon.
     *
     * @param beacon beacon to query
     * @param query query
     * @return raw result of the query from the beacon
     */
    String getQueryResponse(Beacon beacon, Query query);

}
