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
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

/**
 * Basic beacon DAO implementation..
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Named
@Dependent
public class BeaconDaoImpl extends AbstractEntityWithStringIdDaoImpl<Beacon> implements BeaconDao {

    private static final long serialVersionUID = 7394221412609376503L;

    @Override
    public List<Beacon> findByAggregation(boolean aggregator) {
        return em.createNamedQuery("findBeaconsByAggregation", Beacon.class).setParameter("aggregator", aggregator).getResultList();
    }

    @Override
    public List<Beacon> findByVisibility(boolean visible) {
        return em.createNamedQuery("findBeaconsByVisibility", Beacon.class).setParameter("visible", visible).getResultList();
    }

    @Override
    public boolean addRelationship(Beacon child, Beacon parent) {
        Set<Beacon> parents = child.getParents();
        if (parents == null) {
            child.setParents(new HashSet<Beacon>());
        }
        boolean res = child.getParents().add(parent);

        em.merge(child);
        return res;
    }

    @Override
    public boolean removeRelationship(Beacon child, Beacon parent) {
        Set<Beacon> parents = child.getParents();
        if (parents == null) {
            return false;
        }
        boolean res = child.getParents().remove(parent);

        em.merge(child);
        return res;
    }

    private boolean beaconSatisfiesProperties(Beacon b, boolean invisible, boolean disabled) {
        return (b.getVisible() || invisible) && (b.getEnabled() || disabled);
    }

    @Override
    public Set<Beacon> findDescendants(Beacon parent, boolean includeAggregators, boolean includeInvisible, boolean includeDisabled, boolean includeSelf) {
        Set<Beacon> desc = new HashSet<>();

        Queue<Beacon> buffer = new LinkedBlockingQueue<>();
        Beacon b = parent;
        while (b != null) {
            if (b.equals(parent) && includeSelf) {
                if (beaconSatisfiesProperties(b, includeInvisible, includeDisabled)) {
                    desc.add(b);
                }
            } else if (b.getAggregator()) {
                if (includeAggregators) {
                    if (beaconSatisfiesProperties(b, includeInvisible, includeDisabled)) {
                        desc.add(b);
                    }
                }
            } else {
                if (beaconSatisfiesProperties(b, includeInvisible, includeDisabled)) {
                    desc.add(b);
                }
            }

            buffer.addAll(b.getChildren());
            b = buffer.poll();
        }
        return desc;
    }

    @Override
    public boolean haveRelationship(Beacon child, Beacon parent) {
        return child.getParents().contains(parent);
    }

}
