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
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * JPA-based implementation of organization DAO.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RequestScoped
public class OrganizationDaoImpl implements OrganizationDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Organization save(Organization o) {
        em.persist(o);

        return o;
    }

    @Override
    public Organization update(Organization o) {
        return em.merge(o);
    }

    @Override
    public Organization findById(String id) {
        return em.find(Organization.class, id);
    }

    @Override
    public void remove(String id) {
        em.remove(em.getReference(Organization.class, id));
    }

    @Override
    public void flush() {
        em.flush();
    }

    @Override
    public long countAll() {
        return em.createQuery(String.format("SELECT COUNT(e.id) FROM %s e", Organization.class.getSimpleName()), Long.class).getSingleResult();
    }

    @Override
    public List<Organization> findAll() {
        return em.createQuery(String.format("SELECT e FROM %s e", Organization.class.getSimpleName()), Organization.class).getResultList();
    }

}
