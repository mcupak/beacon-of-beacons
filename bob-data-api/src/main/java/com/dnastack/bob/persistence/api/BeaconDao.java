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
package com.dnastack.bob.persistence.api;

import com.dnastack.bob.persistence.entity.Beacon;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Beacon DAO.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public interface BeaconDao extends GenericDao<Beacon, String> {

    /**
     * Retrieves beacons by aggregation flag.
     *
     * @param aggregator true if looking for aggregating beacons, false if looking for regular beacons
     * @return collection of beacons
     */
    List<Beacon> findByAggregation(boolean aggregator);

    /**
     * Retrieves beacons by visibility.
     *
     * @param visible true if looking for visible beacons, false if looking for invisible/anonymous beacons
     * @return collection of beacons
     */
    List<Beacon> findByVisibility(boolean visible);

    /**
     * Retrieves beacons by their IDs.
     *
     * @param ids IDs
     * @return collection of beacons
     */
    List<Beacon> findByIds(Collection<String> ids);

    /**
     * Retrieves beacons by visibility.
     *
     * @param ids     IDs
     * @param visible true if looking for visible beacons, false if looking for invisible/anonymous beacons
     * @return collection of beacons
     */
    List<Beacon> findByIdsAndVisibility(Collection<String> ids, boolean visible);

    /**
     * Retrieves a beacon by its ID and visibility.
     *
     * @param id      ID
     * @param visible true if looking for visible beacons, false if looking for invisible/anonymous beacons
     * @return collection of beacons
     */
    Beacon findByIdAndVisibility(String id, boolean visible);

    /**
     * Checks existence of a parent-child association.
     *
     * @param parent parent
     * @param child  child
     * @return true if the child did not have that parent already, false otherwise
     */
    boolean haveRelationship(Beacon child, Beacon parent);

    /**
     * Creates parent-child association.
     *
     * @param parent parent
     * @param child  child
     * @return true if the child did not have that parent already, false otherwise
     */
    boolean addRelationship(Beacon child, Beacon parent);

    /**
     * Removes parent-child association.
     *
     * @param parent parent
     * @param child  child
     * @return true if the child had that parent already, false otherwise
     */
    boolean removeRelationship(Beacon child, Beacon parent);

    /**
     * Computes a set of nodes covered by a specific node transitively.
     *
     * @param parent             node
     * @param includeAggregators true if aggregators should be included in the results
     * @param includeInvisible   true if invisible beacons should be included in the results
     * @param includeDisabled    true if disabled beacons should be included as well
     * @param includeSelf        true if self should be included in the results
     * @return set of nodes
     */
    Set<Beacon> findDescendants(Beacon parent, boolean includeAggregators, boolean includeInvisible, boolean includeDisabled, boolean includeSelf);

}
