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
     * Retrieves beacon details.
     *
     * @param id id of the beacon
     * @return beacon
     */
    BeaconDto find(String id);

    /**
     * Retrieves beacon details, if visible.
     *
     * @param id id of the beacon
     * @return beacon
     */
    BeaconDto findVisible(String id);

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
     * Retrieves beacons with specified IDs.
     *
     * @param ids collection of beacon ids
     * @return collection of beacons
     */
    Collection<BeaconDto> find(Collection<String> ids);

    /**
     * Retrieves visible beacons with specified IDs.
     *
     * @param ids collection of beacon ids
     * @return collection of beacons
     */
    Collection<BeaconDto> findVisible(Collection<String> ids);

    /**
     * Retrieves all the beacons.
     *
     * @return collection of beacons
     */
    Collection<BeaconDto> findAll();

    /**
     * Retrieves visible beacons.
     *
     * @return collection of beacons
     */
    Collection<BeaconDto> findVisible();

}
