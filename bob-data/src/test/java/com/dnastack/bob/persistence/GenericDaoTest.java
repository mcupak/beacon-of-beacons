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
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

/**
 * Tests for methods in GenericDao.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public abstract class GenericDaoTest extends BasicDaoTest {

    @PersistenceContext
    private EntityManager em;

    public abstract GenericDao getDao();

    public abstract List<BasicEntity> getNewData();

    public abstract Class<? extends BasicEntity> getEntityClass();

    protected String getTableName(Class<? extends BasicEntity> entity) {
        return entity.getSimpleName();
    }

    protected Long countAll(Class<? extends BasicEntity> entity) {
        return (Long) em.createQuery(String.format("SELECT COUNT(e) FROM %s e", getTableName(entity))).getSingleResult();
    }

    @SuppressWarnings("unchecked")
    protected List<BasicEntity> findAll(Class<? extends BasicEntity> entity) {
        return em.createQuery(String.format("SELECT e FROM %s e", getTableName(entity))).getResultList();
    }

    @SuppressWarnings("unchecked")
    protected List<BasicEntity> findByAttribute(Class<? extends BasicEntity> entity, String attribute, String value) {
        return em.createQuery(String.format("SELECT e FROM %s e WHERE e.%s=%s", getTableName(entity), attribute, value)).getResultList();
    }

    protected BasicEntity findOneByAttribute(Class<? extends BasicEntity> entity, String attribute, String value) {
        @SuppressWarnings("unchecked")
        List<BasicEntity> res = em.createQuery("SELECT e FROM " + getTableName(entity) + " e WHERE e." + attribute + "=" + value).getResultList();
        return (res.isEmpty()) ? null : res.get(0);
    }

    protected BasicEntity findOne(Class<? extends BasicEntity> entity) {
        @SuppressWarnings("unchecked")
        List<BasicEntity> res = em.createQuery(String.format("SELECT e FROM %s e", getTableName(entity))).getResultList();

        return (res.isEmpty()) ? null : res.get(0);
    }

    protected BasicEntity findById(Class<? extends BasicEntity> entity, Long id) {
        return (BasicEntity) em.find(entity, id);
    }

    protected BasicEntity findById(Class<? extends BasicEntity> entity, String id) {
        return (BasicEntity) em.find(entity, id);
    }

    @Test
    public void testCountAll() {
        assertThat(getDao().countAll()).isEqualTo(countAll(getEntityClass()));
    }

    @Test
    public void testFlush() throws Exception {
        getDao().flush();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFindAll() {
        assertThat(getDao().findAll()).containsExactlyElementsOf(findAll(getEntityClass()));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSave() {
        if (getNewData().size() < 1) {
            fail("No data to save.");
        }

        BasicEntity b;
        for (BasicEntity e : getNewData()) {
            assertThat(findAll(getEntityClass())).doesNotContain(e);

            b = getDao().save(e);

            assertThat(findAll(getEntityClass()).contains(b));
        }
    }

    public abstract void testUpdate();

    public abstract void testDelete();

    public abstract void testDeleteNonExistent();

    public abstract void testFindById();

}
