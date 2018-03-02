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
package com.dnastack.bob.persistence.impl;

import com.dnastack.bob.persistence.api.BeaconDao;
import com.dnastack.bob.persistence.entity.Beacon;

import javax.enterprise.context.Dependent;
import javax.inject.Named;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Basic beacon DAO implementation..
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Named
@Dependent
public class BeaconDaoImpl extends AbstractGenericDaoImpl<Beacon, String> implements BeaconDao {

    private static final long serialVersionUID = 7394221412609376503L;

    @Override
    public List<Beacon> findByAggregation(boolean aggregator) {
        return getResultList(em.createNamedQuery("findBeaconsByAggregation", Beacon.class)
                               .setParameter("aggregator", aggregator));
    }

    @Override
    public List<Beacon> findByVisibility(boolean visible) {
        return getResultList(em.createNamedQuery("findBeaconsByVisibility", Beacon.class)
                               .setParameter("visible", visible));
    }

    @Override
    public boolean addRelationship(Beacon child, Beacon parent) {
        Set<Beacon> parents = child.getParents();
        if (parents == null) {
            child.setParents(new HashSet<>());
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

    @Override
    public List<Beacon> findByIdsAndVisibility(Collection<String> ids, boolean visible) {
        return getResultList(em.createNamedQuery("findBeaconsByIdsAndVisibility", Beacon.class)
                               .setParameter("ids", ids)
                               .setParameter("visible", visible));
    }

    @Override
    public Beacon findByIdAndVisibility(String id, boolean visible) {
        return getSingleResult(em.createNamedQuery("findBeaconByIdAndVisibility", Beacon.class)
                                 .setParameter("id", id)
                                 .setParameter("visible", visible));
    }

    @Override
    public List<Beacon> findByIds(Collection<String> ids) {
        return getResultList(em.createNamedQuery("findBeaconsByIds", Beacon.class).setParameter("ids", ids));
    }

}
