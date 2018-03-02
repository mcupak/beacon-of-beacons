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

import com.dnastack.bob.persistence.api.GenericDao;
import com.dnastack.bob.persistence.entity.BasicEntity;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Default generic DAO implementation.
 *
 * @param <T> entity type
 * @param <I> ID type
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public abstract class AbstractGenericDaoImpl<T extends BasicEntity<I>, I> implements GenericDao<T, I> {

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

    protected T getSingleResult(TypedQuery<T> query) {
        T e;
        try {
            e = query.getSingleResult();
        } catch (PersistenceException ex) {
            e = null;
        }
        return e;
    }

    protected List<T> getResultList(TypedQuery<T> query) {
        List<T> e;
        try {
            e = query.getResultList();
        } catch (PersistenceException ex) {
            e = null;
        }
        return e;
    }

    @Override
    public long countAll() {
        return em.createQuery(String.format("SELECT COUNT(e.id) FROM %s e", entityClass.getSimpleName()), Long.class)
                 .getSingleResult();
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
        return getResultList(em.createQuery(String.format("SELECT e FROM %s e", entityClass.getSimpleName()),
                                            entityClass));
    }

    @Override
    public T findById(I id) {
        return em.find(entityClass, id);
    }

    @Override
    public T getReferenceById(I id) {
        return em.getReference(entityClass, id);
    }

    @Override
    public void delete(I id) {
        em.remove(em.getReference(entityClass, id));
    }
}
