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
package com.dnastack.beacon.service;

import com.dnastack.beacon.dao.BeaconDao;
import com.dnastack.beacon.dto.BeaconTo;
import com.dnastack.beacon.log.Logged;
import java.util.Collections;
import java.util.Set;
import javax.inject.Inject;

/**
 * Implementation of a service managing beacons.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class BeaconServiceImpl implements BeaconService {

    @Inject
    private BeaconDao beaconDao;

    @Logged
    @Override
    public BeaconTo getBob() {
        return beaconDao.getBob();
    }

    @Logged
    @Override
    public BeaconTo getBeacon(String beaconId) {
        return beaconDao.getBeacon(beaconId);
    }

    @Logged
    @Override
    public Set<BeaconTo> getAll() {
        return Collections.unmodifiableSet(beaconDao.getAllBeacons());
    }

}
