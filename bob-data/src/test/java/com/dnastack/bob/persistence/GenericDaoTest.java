package com.dnastack.bob.persistence;

import com.dnastack.bob.persistence.api.GenericDao;
import com.dnastack.bob.persistence.entity.BasicEntity;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

}
