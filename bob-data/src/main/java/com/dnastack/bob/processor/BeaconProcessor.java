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
package com.dnastack.bob.processor;

import com.dnastack.bob.entity.Beacon;
import com.dnastack.bob.entity.Query;
import com.dnastack.bob.entity.Reference;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * Beacon query service.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public interface BeaconProcessor {

    /**
     * Obtains genomes supported by the service.
     *
     * @return set of supported genomes
     */
    Set<Reference> getSupportedReferences();

    /**
     * Asynchronously executes a query agaist a beacon.
     *
     * @param beacon beacon
     * @param query  query
     *
     * @return true/false according to the beacons response (or null if the valid response could not be obtained)
     */
    Future<Boolean> executeQuery(Beacon beacon, Query query);

    /**
     * Asynchronously xtracts beacon response value from the given raw query reponse.
     *
     * @param beacon beacon
     * @param response response
     *
     * @return true/false for valid values, null otherwise
     */
    Future<Boolean> parseQueryResponse(Beacon beacon, String response);

    /**
     * Asynchronously obtains raw response to the query from the beacon.
     *
     * @param beacon beacon to query
     * @param query  query
     *
     * @return raw result of the query from the beacon
     */
    Future<String> getQueryResponse(Beacon beacon, Query query);

}
