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

import com.dnastack.bob.persistence.api.OrganizationDao;
import com.dnastack.bob.persistence.entity.Organization;
import lombok.NonNull;

import javax.enterprise.context.Dependent;
import javax.inject.Named;
import java.util.Collection;
import java.util.List;

/**
 * JPA-based implementation of organization DAO.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Named
@Dependent
public class OrganizationDaoImpl extends AbstractGenericDaoImpl<Organization, String> implements OrganizationDao {

    private static final long serialVersionUID = 2403615525311736080L;

    @Override
    public Organization findByName(@NonNull String name) {
        return getSingleResult(em.createNamedQuery("findOrganizationByName", Organization.class)
                                 .setParameter("name", name));
    }

    @Override
    public List<Organization> findByVisibility(boolean visible) {
        return getResultList(em.createNamedQuery("findOrganizationsByVisibility", Organization.class)
                               .setParameter("visible", visible));
    }

    @Override
    public Organization findByIdAndVisibility(String id, boolean visible) {
        return getSingleResult(em.createNamedQuery("findOrganizationByIdAndVisibility", Organization.class)
                                 .setParameter("id", id)
                                 .setParameter("visible", visible));
    }

    @Override
    public List<Organization> findByIdsAndVisibility(Collection<String> ids, boolean visible) {
        return getResultList(em.createNamedQuery("findOrganizationsByIdsAndVisibility", Organization.class)
                               .setParameter("ids", ids)
                               .setParameter("visible", visible));
    }

    @Override
    public List<Organization> findByIds(Collection<String> ids) {
        return getResultList(em.createNamedQuery("findOrganizationsByIds", Organization.class)
                               .setParameter("ids", ids));
    }

}
