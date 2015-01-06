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
package com.dnastack.bob.service;

import com.dnastack.bob.dao.BeaconDao;
import com.dnastack.bob.dto.BeaconTo;
import com.dnastack.bob.util.Entity2ToConvertor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 * Implementation of a service managing beacons.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RequestScoped
public class BeaconServiceImpl implements BeaconService {

    @Inject
    private BeaconDao beaconDao;

    @Override
    public BeaconTo getBeacon(String beaconId) {
        return Entity2ToConvertor.getBeaconTo(beaconDao.getVisibleBeacon(beaconId));
    }

    @Override
    public Collection<BeaconTo> getBeacons(Collection<String> beaconIds) {
        List<BeaconTo> res = new ArrayList<>();
        for (String id : beaconIds) {
            BeaconTo b = getBeacon(id);
            if (b != null) {
                res.add(b);
            }
        }

        return res;
    }

    @Override
    public Collection<BeaconTo> getAll() {
        return Entity2ToConvertor.getBeaconTos(beaconDao.getVisibleBeacons());
    }

}
