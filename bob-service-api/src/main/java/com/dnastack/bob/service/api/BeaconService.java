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
package com.dnastack.bob.service.api;

import com.dnastack.bob.service.dto.BeaconDto;

import java.util.Collection;

/**
 * Service managing beacons.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public interface BeaconService {

    /**
     * Retrieves a visible beacon.
     *
     * @param id id of the beacon
     * @return beacon
     */
    BeaconDto find(String id);

    /**
     * Creates a new beacon.
     *
     * @param beacon
     * @return
     */
    BeaconDto create(BeaconDto beacon);

    /**
     * Updates a beacon.
     *
     * @param id     ID of the beacon to update
     * @param beacon template
     * @return
     */
    BeaconDto update(String id, BeaconDto beacon);

    /**
     * Removes a beacon.
     *
     * @param id ID of the beacon to remove
     */
    void delete(String id);

    /**
     * Retrieves visible beacons with specified IDs.
     *
     * @param ids collection of beacon ids
     * @return collection of beacons
     */
    Collection<BeaconDto> find(Collection<String> ids);

    /**
     * Retrieves all the visible beacons.
     *
     * @return collection of beacons
     */
    Collection<BeaconDto> find();

}
