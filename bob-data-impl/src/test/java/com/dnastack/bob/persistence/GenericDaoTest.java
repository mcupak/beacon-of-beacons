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
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.junit.Assert.assertEquals;

/**
 * Tests for methods in GenericDao.
 *
 * @param <T> entity type
 * @param <I> ID type
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public abstract class GenericDaoTest<T extends BasicEntity<I>, I> extends BasicDaoTest {

    @PersistenceContext
    private EntityManager em;

    private Class<T> entityClass;

    private Class<I> idClass;

    @SuppressWarnings("unchecked")
    protected GenericDaoTest() {
        if (entityClass == null || idClass == null) {
            Type type = getClass().getGenericSuperclass();
            loop:
            while (true) {
                if (type instanceof ParameterizedType) {
                    Type[] arguments = ((ParameterizedType) type).getActualTypeArguments();
                    for (Type argument : arguments) {
                        if ((argument instanceof Class)) {
                            if (BasicEntity.class.isAssignableFrom(((Class<T>) argument))) {
                                entityClass = (Class<T>) argument;
                            } else {
                                idClass = (Class<I>) argument;
                            }
                            if (entityClass != null && idClass != null) {
                                break loop;
                            }
                        }
                    }
                }
                type = ((Class<T>) type).getGenericSuperclass();
                if (type == Object.class) {
                    throw new RuntimeException("Could not find a parameterized type");
                }
            }
        }
    }

    private static String getTableName(Class<? extends BasicEntity> entity) {
        return entity.getSimpleName();
    }

    public abstract GenericDao<T, I> getDao();

    public abstract T getEntityForUpdate(T e);

    public abstract List<T> getEntitiesForSave();

    @SuppressWarnings("unchecked")
    protected I getInvalidId() {
        return (I) (idClass.isAssignableFrom(Long.class) ? 1L : idClass.isAssignableFrom(Long.class) ? "zzz" : null);
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
        @SuppressWarnings("unchecked") List<BasicEntity> res = em.createQuery("SELECT e FROM " + getTableName(entity) + " e WHERE e." + attribute + "=" + value).getResultList();
        return (res.isEmpty()) ? null : res.get(0);
    }

    protected BasicEntity findOne(Class<? extends BasicEntity> entity) {
        @SuppressWarnings("unchecked") List<BasicEntity> res = em.createQuery(String.format("SELECT e FROM %s e", getTableName(entity))).getResultList();

        return (res.isEmpty()) ? null : res.get(0);
    }

    protected BasicEntity findById(Class<? extends BasicEntity> entity, Long id) {
        return em.find(entity, id);
    }

    protected BasicEntity findById(Class<? extends BasicEntity> entity, String id) {
        return em.find(entity, id);
    }

    @Test
    public void testCountAll() {
        assertThat(getDao().countAll()).isEqualTo(countAll(entityClass));
    }

    @Test
    public void testFlush() throws Exception {
        getDao().flush();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFindAll() {
        assertThat(getDao().findAll()).containsExactlyElementsOf(findAll(entityClass).parallelStream().map(i -> (T) i).collect(Collectors.toList()));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSave() {
        if (getEntitiesForSave().size() < 1) {
            fail("No data to save.");
        }

        T b;
        for (T e : getEntitiesForSave()) {
            assertThat(findAll(entityClass)).doesNotContain(e);

            b = getDao().save(e);

            assertThat(findAll(entityClass).contains(b));
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDelete() {
        T e = (T) findOne(entityClass);

        getDao().delete(e.getId());

        assertThat(findAll(entityClass)).doesNotContain(e);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindById() {
        T e = (T) findOne(entityClass);

        T e2 = getDao().findById(e.getId());

        assertThat(e).isEqualToComparingFieldByField(e2);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetReferenceById() {
        T e = (T) findOne(entityClass);

        T e2 = getDao().getReferenceById(e.getId());

        assertThat(e).isEqualTo(e2);
    }

    @Test(expected = Exception.class)
    @SuppressWarnings("unchecked")
    public void testDeleteNonExistent() {
        Long orig = countAll(entityClass);

        getDao().delete(getInvalidId());

        assertEquals(orig, countAll(entityClass));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUpdate() {
        T e = getEntityForUpdate((T) findOne(entityClass));

        T b = getDao().update(e);

        assertThat(findAll(entityClass)).contains(b);
    }
}
