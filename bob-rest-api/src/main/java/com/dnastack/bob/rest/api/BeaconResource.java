/*
 * The MIT License
 *
 * Copyright 2015 DNAstack.
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
package com.dnastack.bob.rest.api;

import com.dnastack.bob.service.dto.BeaconDto;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;

/**
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public interface BeaconResource {

    /**
     * Creates a beacon.
     *
     * @param uriInfo URI info
     * @param beacon  beacon
     * @return URI of the beacon created
     */
    Response create(UriInfo uriInfo, BeaconDto beacon);

    /**
     * Removes a beacon.
     *
     * @param uriInfo  URI info
     * @param beaconId ID of the beacon
     * @return
     */
    Response delete(UriInfo uriInfo, String beaconId);

    /**
     * Shows all the beacons or a specific beacon as determined by a param.
     *
     * @param beaconIds beacon ID
     * @return set of beacons
     */
    Collection<BeaconDto> show(String beaconIds);

    /**
     * Shows beacon details.
     *
     * @param beaconId id of the beacon
     * @return beacon
     */
    BeaconDto showBeacon(String beaconId);

    /**
     * Updates a beacon.
     *
     * @param uriInfo  URI info
     * @param beaconId ID of the beacon
     * @param beacon   beacon
     * @return updated beacon
     */
    Response update(UriInfo uriInfo, String beaconId, BeaconDto beacon);

}
