/*
 * The MIT License
 *
 * Copyright 2015 DNAstack.
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
        return getSingleResult(em.createNamedQuery("findOrganizationByName", Organization.class).setParameter("name", name));
    }

    @Override
    public List<Organization> findByVisibility(boolean visible) {
        return getResultList(em.createNamedQuery("findOrganizationsByVisibility", Organization.class).setParameter("visible", visible));
    }

    @Override
    public Organization findByIdAndVisibility(String id, boolean visible) {
        return getSingleResult(em.createNamedQuery("findOrganizationByIdAndVisibility", Organization.class).setParameter("id", id).setParameter("visible", visible));
    }

    @Override
    public List<Organization> findByIdsAndVisibility(Collection<String> ids, boolean visible) {
        return getResultList(em.createNamedQuery("findOrganizationsByIdsAndVisibility", Organization.class).setParameter("ids", ids).setParameter("visible", visible));
    }

    @Override
    public List<Organization> findByIds(Collection<String> ids) {
        return getResultList(em.createNamedQuery("findOrganizationsByIds", Organization.class).setParameter("ids", ids));
    }

}
