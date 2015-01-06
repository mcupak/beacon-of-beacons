/*
 * The MIT License
 *
 * Copyright 2014 DNAstack.
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
package com.dnastack.bob.util;

import com.dnastack.bob.dao.BeaconDao;
import com.dnastack.bob.entity.Beacon;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Resolver of beacon aggregations.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@ApplicationScoped
@Named
public class BeaconAggregationResolver implements Serializable {

    private static final long serialVersionUID = 104L;

    @Inject
    private BeaconDao beaconDao;

    private Multimap<Beacon, Beacon> aggregations;

    @PostConstruct
    private void computeAggregations() {
        aggregations = HashMultimap.create();

        // first pass
        Collection<Beacon> all = beaconDao.getAllBeacons();
        for (Beacon b : all) {
            for (Beacon parent : b.getAggregators()) {
                if (parent.getProcessor() == null) {
                    aggregations.put(parent, b);
                }
            }
        }

        // multiple passes to balance
        Collection<Beacon> aggs = beaconDao.getAggregatingBeacons();
        boolean changed;
        do {
            changed = false;
            for (Beacon a : aggs) {
                Collection<Beacon> children = aggregations.get(a);
                for (Beacon c : children) {
                    if (c.isAggregator()) {
                        aggregations.putAll(a, aggregations.get(c));
                        aggregations.remove(a, c);
                        changed = true;
                    }
                }
            }
        } while (changed);
    }

    /**
     * Computes children of a node.
     *
     * @param b node
     *
     * @return collection of the given node's children
     */
    public Collection<Beacon> getAggregatees(Beacon b) {
        Set<Beacon> aggs = new HashSet<>();
        if (b.isAggregator()) {
            Collection<Beacon> bs = beaconDao.getAllBeacons();
            for (Beacon c : bs) {
                if (c.getAggregators().contains(b)) {
                    aggs.add(c);
                }
            }
        }

        return aggs;
    }

    /**
     * Computes a collection of all the regular (non-aggregating) nodes covered by a specific node transitively.
     *
     * @param b node
     *
     * @return collection of nodes
     */
    public Collection<Beacon> getAtomicAggregatees(Beacon b) {
        return Collections.unmodifiableCollection(aggregations.get(b));
    }
}
