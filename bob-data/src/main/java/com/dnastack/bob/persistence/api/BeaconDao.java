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
package com.dnastack.bob.persistence.api;

import com.dnastack.bob.persistence.entity.Beacon;
import java.util.List;
import java.util.Set;

/**
 * Beacon DAO.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public interface BeaconDao extends EntityWithStringIdDao<Beacon> {

    /**
     * Retrieves beacons by aggregation flag.
     *
     * @param aggregator true if looking for aggregating beacons, false if looking for regular beacons
     *
     * @return collection of beacons
     */
    List<Beacon> findByAggregation(boolean aggregator);

    /**
     * Retrieves beacons by visibility.
     *
     * @param visible true if looking for visible beacons, false if looking for invisible/anonymous beacons
     *
     * @return collection of beacons
     */
    List<Beacon> findByVisibility(boolean visible);

    /**
     * Creates parent-child association.
     *
     * @param parent parent
     * @param child  child
     *
     * @return true if the child did not have that parent already, false otherwise
     */
    boolean addRelationship(Beacon child, Beacon parent);

    /**
     * Removes parent-child association.
     *
     * @param parent parent
     * @param child  child
     *
     * @return true if the child had that parent already, false otherwise
     */
    boolean removeRelationship(Beacon child, Beacon parent);

    /**
     * Computes a set of all the regular (non-aggregating) nodes covered by a specific node transitively.
     *
     * @param parent node
     *
     * @return set of nodes
     */
    Set<Beacon> getRegularDescendants(Beacon parent);

}
