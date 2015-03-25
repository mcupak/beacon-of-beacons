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
package com.dnastack.bob.persistence;

import com.dnastack.bob.persistence.api.BeaconDao;
import com.dnastack.bob.persistence.api.GenericDao;
import com.dnastack.bob.persistence.entity.BasicEntity;
import com.dnastack.bob.persistence.entity.Beacon;
import com.dnastack.bob.persistence.entity.Organization;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.CleanupStrategy;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Beacon DAO test.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RunWith(Arquillian.class)
@Transactional
@UsingDataSet("beacon_init.json")
@Cleanup(strategy = CleanupStrategy.USED_TABLES_ONLY) // this is important in order to prevent foreign-key violations
public class BeaconDaoTest extends EntityWithStringIdDaoTest {

    @Inject
    private BeaconDao dao;

    @Override
    public GenericDao<Beacon> getDao() {
        return dao;
    }

    @Override
    public Class<? extends BasicEntity> getEntityClass() {
        return Beacon.class;
    }

    @Override
    public List<BasicEntity> getNewData() {
        Organization o = (Organization) findOne(Organization.class);

        List<BasicEntity> res = new ArrayList<>();
        Beacon b = new Beacon();
        b.setId("new");
        b.setName("new");
        b.setDescription("new");
        b.setVisible(true);
        b.setEnabled(true);
        b.setOrganization(o);
        res.add(b);

        return res;
    }

    @Override
    @Test
    public void testUpdate() {
        Beacon e = (Beacon) findOne(getEntityClass());
        e.setName("updated");

        Beacon b = dao.update(e);

        assertThat(findAll(getEntityClass())).contains(b);
    }

    @Override
    @Test
    public void testDelete() {
        Beacon e = (Beacon) findOne(getEntityClass());
        dao.delete(e.getId());
        assertThat(findAll(getEntityClass())).doesNotContain(e);
    }

    @Override
    @Test
    public void testFindById() {
        Beacon e = (Beacon) findOne(getEntityClass());
        Beacon e2 = dao.findById(e.getId());
        assertThat(e).isEqualTo(e2);
    }

    @Test
    public void testFindByVisibility() {
        List<Beacon> visible = dao.findByVisibility(true);
        assertThat(visible).extracting("visible").containsOnly(true);
        List<Beacon> inVisible = dao.findByVisibility(false);
        assertThat(inVisible).extracting("visible").containsOnly(false);

        assertThat((long) visible.size() + (long) inVisible.size()).isEqualTo(countAll(getEntityClass()));
    }

    @Test
    public void testFindByAggregation() {
        List<Beacon> aggregators = dao.findByAggregation(true);
        assertThat(aggregators).extracting("processor").are(getNullCondition());

        List<Beacon> regulars = dao.findByAggregation(false);
        assertThat(regulars).extracting("visible").doesNotContainNull();

        assertThat((long) aggregators.size() + (long) regulars.size()).isEqualTo(countAll(getEntityClass()));
    }

    @Test
    @UsingDataSet("beacon_relationship.json")
    public void testHaveRelationship() {
        Beacon parent = (Beacon) findById(getEntityClass(), "parent");
        Beacon childWithParent = (Beacon) findById(getEntityClass(), "childWithParent");

        assertThat(dao.haveRelationship(childWithParent, parent)).isTrue();
    }

    @Test
    @UsingDataSet("beacon_relationship.json")
    public void testDoNotHaveRelationship() {
        Beacon parent = (Beacon) findById(getEntityClass(), "parent");
        Beacon childWithoutParent = (Beacon) findById(getEntityClass(), "childWithoutParent");

        assertThat(dao.haveRelationship(childWithoutParent, parent)).isFalse();
    }

    @Test
    @UsingDataSet("beacon_relationship.json")
    public void testAddExistingRelationship() {
        Beacon parent = (Beacon) findById(getEntityClass(), "parent");
        Beacon childWithParent = (Beacon) findById(getEntityClass(), "childWithParent");

        boolean res = dao.addRelationship(childWithParent, parent);

        childWithParent = (Beacon) findById(getEntityClass(), "childWithParent");
        assertThat(res).isFalse();
        assertThat(childWithParent.getParents()).doesNotHaveDuplicates().isNotEmpty().containsOnly(parent);
    }

    @Test
    @UsingDataSet("beacon_relationship.json")
    public void testAddNonExistingRelationship() {
        Beacon parent = (Beacon) findById(getEntityClass(), "parent");
        Beacon childWithoutParent = (Beacon) findById(getEntityClass(), "childWithoutParent");

        boolean res = dao.addRelationship(childWithoutParent, parent);

        childWithoutParent = (Beacon) findById(getEntityClass(), "childWithParent");
        assertThat(res).isTrue();
        assertThat(childWithoutParent.getParents()).doesNotHaveDuplicates().isNotEmpty().containsOnly(parent);
    }

    @Test
    @UsingDataSet("beacon_relationship.json")
    public void testRemoveExistingRelationship() {
        Beacon parent = (Beacon) findById(getEntityClass(), "parent");
        Beacon childWithParent = (Beacon) findById(getEntityClass(), "childWithParent");

        assertThat(childWithParent.getParents()).doesNotHaveDuplicates().isNotEmpty().contains(parent);

        boolean res = dao.removeRelationship(childWithParent, parent);

        childWithParent = (Beacon) findById(getEntityClass(), "childWithParent");
        assertThat(res).isTrue();
        assertThat(childWithParent.getParents()).isEmpty();
    }

    @Test
    @UsingDataSet("beacon_relationship.json")
    public void testRemoveNonExistingRelationship() {
        Beacon parent = (Beacon) findById(getEntityClass(), "parent");
        Beacon childWithoutParent = (Beacon) findById(getEntityClass(), "childWithoutParent");

        assertThat(childWithoutParent.getParents()).isEmpty();

        boolean res = dao.removeRelationship(childWithoutParent, parent);

        childWithoutParent = (Beacon) findById(getEntityClass(), "childWithoutParent");
        assertThat(res).isFalse();
        assertThat(childWithoutParent.getParents()).isEmpty();
    }

    @Test
    @UsingDataSet("beacon_descendants.json")
    public void testFindAllDescendants() {
        Beacon parent = (Beacon) findById(getEntityClass(), "root");
        Set<Beacon> all = new HashSet<>();
        for (BasicEntity e : findAll(getEntityClass())) {
            all.add((Beacon) e);
        }

        Set<Beacon> bs = dao.findDescendants(parent, true, true, true, true);

        assertThat(bs).containsAll(all).doesNotHaveDuplicates().isNotEmpty();
    }

}
