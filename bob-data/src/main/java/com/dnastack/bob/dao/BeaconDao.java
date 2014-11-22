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
package com.dnastack.bob.dao;

import com.dnastack.bob.entity.Beacon;
import java.util.Collection;

/**
 * Mapper and holder of beacons and services.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public interface BeaconDao {

    /**
     * Retrieves all the beacons.
     *
     * @return collection of beacons
     */
    Collection<Beacon> getAllBeacons();

    /**
     * Retrieves all aggregating beacons.
     *
     * @return collection of beacons
     */
    Collection<Beacon> getAggregatingBeacons();

    /**
     * Retrieves all non-aggregating beacons.
     *
     * @return collection of beacons
     */
    Collection<Beacon> getRegularBeacons();

    /**
     * Retrieves all visible beacons.
     *
     * @return collection of beacons
     */
    Collection<Beacon> getVisibleBeacons();

    /**
     * Retrieves all invisible (anonymous) beacons.
     *
     * @return collection of beacons
     */
    Collection<Beacon> getHiddenBeacons();

    /**
     * Finds beacon by its ID.
     *
     * @param beaconId beacon ID
     *
     * @return beacon with the given ID
     */
    Beacon getBeacon(String beaconId);

    /**
     * Finds a visible beacon by its ID.
     *
     * @param beaconId beacon ID
     *
     * @return beacon with the given ID or null if the beacon is invisible/does not exist
     */
    Beacon getVisibleBeacon(String beaconId);
}
