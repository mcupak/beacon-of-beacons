/* 
 * Copyright 2018 DNAstack.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
