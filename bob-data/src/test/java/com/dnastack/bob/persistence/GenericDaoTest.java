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
package com.dnastack.bob.persistence;

import com.dnastack.bob.persistence.api.GenericDao;
import com.dnastack.bob.persistence.entity.BasicEntity;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for methods in GenericDao.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public abstract class GenericDaoTest extends BasicDaoTest {

    @PersistenceContext
    protected EntityManager em;

    public abstract GenericDao getDao();

    public abstract List<BasicEntity> getNewData();

    public abstract Class<? extends BasicEntity> getEntityClass();

    protected String getTableName(Class entity) {
        return entity.getSimpleName();
    }

    protected Long countAll(Class entity) {
        return (Long) em.createQuery("SELECT COUNT(e) FROM " + getTableName(entity) + " e").getSingleResult();
    }

    protected List<BasicEntity> findAll(Class entity) {
        return em.createQuery("SELECT e FROM " + getTableName(entity) + " e").getResultList();
    }

    protected List<BasicEntity> findByAttribute(Class entity, String attribute, String value) {
        return em.createQuery("SELECT e FROM " + getTableName(entity) + " e WHERE e." + attribute + "=" + value).getResultList();
    }

    protected BasicEntity findOneByAttribute(Class entity, String attribute, String value) {
        List<BasicEntity> res = em.createQuery("SELECT e FROM " + getTableName(entity) + " e WHERE e." + attribute + "=" + value).getResultList();
        return (res.isEmpty()) ? null : res.get(0);
    }

    protected BasicEntity findOne(Class entity) {
        List<BasicEntity> res = em.createQuery("SELECT e FROM " + getTableName(entity) + " e").getResultList();

        return (res.isEmpty()) ? null : res.get(0);
    }

    protected BasicEntity findById(Class entity, Long id) {
        return (BasicEntity) em.find(entity, id);
    }

    @Test
    public void testCountAll() {
        assertEquals((long) countAll(getEntityClass()), getDao().countAll());
    }

    @Test
    public void testFlush() throws Exception {
        getDao().flush();
    }

    @Test
    public void testFindAll() {
        Set<BasicEntity> ents = new HashSet<>(findAll(getEntityClass()));

        for (Object e : getDao().findAll()) {
            assertTrue(ents.contains(e));
        }
    }

    @Test
    public void testSave() {
        if (getNewData().size() < 1) {
            fail("No data to save.");
        }

        BasicEntity b;
        for (BasicEntity e : getNewData()) {
            assertThat(findAll(getEntityClass()), not(hasItem(equalTo(e))));
            b = getDao().save(e);
            assertThat(findAll(getEntityClass()), hasItem(equalTo(b)));
        }
    }

    public abstract void testUpdate();

    public abstract void testDelete();

    public abstract void testDeleteNonExistent();

    public abstract void testFindById();

}
