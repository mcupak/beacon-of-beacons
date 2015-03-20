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
package com.dnastack.bob.persistence.impl;

import com.dnastack.bob.persistence.api.BeaconDao;
import com.dnastack.bob.persistence.entity.Beacon;
import java.util.List;
import javax.enterprise.context.RequestScoped;

/**
 * Basic beacon DAO implementation..
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RequestScoped
public class BeaconDaoImpl extends AbstractEntityWithStringIdDaoImpl<Beacon> implements BeaconDao {

    private static final long serialVersionUID = 7394221412609376503L;

    @Override
    public List<Beacon> findByAggregation(boolean aggregator) {
        return em.createNamedQuery((aggregator) ? "findAggregatingBeacons" : "findRegularBeacons", Beacon.class).getResultList();
    }

    @Override
    public List<Beacon> findByVisibility(boolean visible) {
        return em.createNamedQuery("findBeaconsByVisibility", Beacon.class).setParameter("visible", visible).getResultList();
    }

}
