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

import com.dnastack.bob.dto.BeaconResponseTo;
import com.dnastack.bob.dto.BeaconTo;
import com.dnastack.bob.dto.ChromosomeTo;
import com.dnastack.bob.dto.QueryTo;
import com.dnastack.bob.dto.ReferenceTo;
import com.dnastack.bob.entity.Beacon;
import com.dnastack.bob.entity.BeaconResponse;
import com.dnastack.bob.entity.Chromosome;
import com.dnastack.bob.entity.Query;
import com.dnastack.bob.entity.Reference;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Convertor of entities to TOs.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class Entity2ToConvertor {

    /**
     * Converts a beacon to a beacon TO.
     *
     * @param b beacon
     *
     * @return beacon TO
     */
    public static BeaconTo getBeaconTo(Beacon b) {
        return (b == null) ? null : new BeaconTo(b.getId(), b.getName(), b.getOrganization(), b.isAggregator());
    }

    /**
     * Converts a collection of beacons to a collection of beacon TOs.
     *
     * @param bs beacons
     *
     * @return beacon TOs
     */
    public static Collection<BeaconTo> getBeaconTos(Collection<Beacon> bs) {
        Set<BeaconTo> res = new HashSet<>();
        for (Beacon br : bs) {
            res.add(getBeaconTo(br));
        }

        return res;
    }

    /**
     * Converts a chromosome to a chromosome TO.
     *
     * @param c chromosome
     *
     * @return chromosome TO
     */
    public static ChromosomeTo getChromosomeTo(Chromosome c) {
        return (c == null) ? null : ChromosomeTo.valueOf(c.name());
    }

    /**
     * Converts a reference to a reference TO.
     *
     * @param r reference
     *
     * @return reference TO
     */
    public static ReferenceTo getReferenceTo(Reference r) {
        return (r == null) ? null : ReferenceTo.valueOf(r.name());
    }

    /**
     * Converts a query to a query TO.
     *
     * @param q query
     *
     * @return query TO
     */
    public static QueryTo getQueryTo(Query q) {
        return (q == null) ? null : new QueryTo(getChromosomeTo(q.getChromosome()), q.getPosition(), q.getAllele(), getReferenceTo(q.getReference()));
    }

    /**
     * Converts a collection of queries to a collection of query TOs.
     *
     * @param qs queries
     *
     * @return query TOs
     */
    public static Collection<QueryTo> getQueryTos(Collection<Query> qs) {
        Set<QueryTo> res = new HashSet<>();
        for (Query br : qs) {
            res.add(getQueryTo(br));
        }

        return res;
    }

    /**
     * Converts a beacon response to a beacon response TO.
     *
     * @param br beacon response
     *
     * @return beacon response TO
     */
    public static BeaconResponseTo getBeaconResponseTo(BeaconResponse br) {
        return (br == null) ? null : new BeaconResponseTo(getBeaconTo(br.getBeacon()), getQueryTo(br.getQuery()), br.getResponse());
    }

    /**
     * Converts a collection of beacon responses to a collection of beacon response TOs.
     *
     * @param brs beacon responses
     *
     * @return beacon response TOs
     */
    public static Collection<BeaconResponseTo> getBeaconResponseTos(Collection<BeaconResponse> brs) {
        Set<BeaconResponseTo> res = new HashSet<>();
        for (BeaconResponse br : brs) {
            res.add(getBeaconResponseTo(br));
        }

        return res;
    }
}
