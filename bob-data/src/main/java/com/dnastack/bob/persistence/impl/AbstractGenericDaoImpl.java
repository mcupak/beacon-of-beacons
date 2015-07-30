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
package com.dnastack.bob.persistence.impl;

import com.dnastack.bob.persistence.api.GenericDao;
import com.dnastack.bob.persistence.entity.BasicEntity;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Default generic DAO implementation.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 * @param <T> entity
 */
public abstract class AbstractGenericDaoImpl<T extends BasicEntity> implements GenericDao<T> {

    private static final long serialVersionUID = 8059580643827478476L;

    @PersistenceContext
    protected EntityManager em;

    private Class<T> entityClass;

    @PostConstruct
    @SuppressWarnings("unchecked")
    private void initialize() {
        if (entityClass == null) {
            Type type = getClass().getGenericSuperclass();
            loop:
            while (true) {
                if (type instanceof ParameterizedType) {
                    Type[] arguments = ((ParameterizedType) type).getActualTypeArguments();
                    for (Type argument : arguments) {
                        if ((argument instanceof Class) && BasicEntity.class.isAssignableFrom(((Class<T>) argument))) {
                            entityClass = (Class<T>) argument;
                            break loop;
                        }
                    }
                }
                type = ((Class<T>) type).getGenericSuperclass();
                if (type == Object.class) {
                    throw new RuntimeException("Could not find a DatabaseObject subclass parameterized type");
                }
            }
        }
    }

    @Override
    public long countAll() {
        return em.createQuery(String.format("SELECT COUNT(e.id) FROM %s e", entityClass.getSimpleName()), Long.class).getSingleResult();
    }

    protected Class<T> getEntityClass() {
        return entityClass;
    }

    @Override
    public T save(T t) {
        em.persist(t);
        return t;
    }

    @Override
    public void flush() {
        em.flush();
    }

    @Override
    public T update(T t) {
        return em.merge(t);
    }

    @Override
    public List<T> findAll() {
        return em.createQuery(String.format("SELECT e FROM %s e", getEntityClass().getSimpleName()), getEntityClass()).getResultList();
    }
}
