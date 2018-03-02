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
package com.dnastack.bob.persistence;

import com.dnastack.bob.persistence.api.BeaconDao;
import com.dnastack.bob.persistence.api.GenericDao;
import com.dnastack.bob.persistence.entity.BasicEntity;
import com.dnastack.bob.persistence.entity.Beacon;
import com.dnastack.bob.persistence.entity.Organization;
import com.dnastack.bob.persistence.enumerated.Reference;
import com.google.common.collect.ImmutableSet;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.CleanupStrategy;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Beacon DAO test.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RunWith(Arquillian.class)
@Transactional
@UsingDataSet({"organization_1.json", "beacon_1.json", "beacon_2.json"})
@Cleanup(strategy = CleanupStrategy.USED_TABLES_ONLY) // this is important in order to prevent foreign-key violations
public class BeaconDaoTest extends GenericDaoTest<Beacon, String> {

    @Inject
    private BeaconDao dao;

    @Override
    public GenericDao<Beacon, String> getDao() {
        return dao;
    }

    @Override
    public List<Beacon> getEntitiesForSave() {
        Organization o = (Organization) findOne(Organization.class);

        List<Beacon> res = new ArrayList<>();
        Beacon b = new Beacon();
        b.setId("new");
        b.setName("new");
        b.setDescription("new");
        b.setCreatedDate(Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        b.setVisible(true);
        b.setEnabled(true);
        b.setAggregator(true);
        b.setOrganization(o);
        b.setSupportedReferences(EnumSet.noneOf(Reference.class));
        res.add(b);

        return res;
    }

    @Override
    public Beacon getEntityForUpdate(Beacon e) {
        e.setName("updated");
        return e;
    }

    @Test
    public void testFindByVisibility() {
        List<Beacon> visible = dao.findByVisibility(true);
        assertThat(visible).extracting("visible").containsOnly(true);

        List<Beacon> inVisible = dao.findByVisibility(false);
        assertThat(inVisible).extracting("visible").containsOnly(false);

        assertThat(visible.size() + (long) inVisible.size()).isEqualTo(countAll(Beacon.class));
    }

    @Test
    public void testFindByIds() {
        Set<Beacon> all = findAll(Beacon.class).stream().map((BasicEntity e) -> (Beacon) e).collect(Collectors.toSet());

        List<Beacon> found = dao.findByIds(ImmutableSet.of("test", "test2"));

        assertThat(found.size()).isEqualTo(2);
        assertThat(found).containsAll(all);
    }

    @Test
    public void testFindByIdsAndVisibility() {
        Beacon b = (Beacon) findById(Beacon.class, "test");

        List<Beacon> found = dao.findByIdsAndVisibility(ImmutableSet.of(b.getId(), "test2"), true);

        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(b);
    }

    @Test
    public void testFindByIdAndVisibility() {
        Beacon b = (Beacon) findById(Beacon.class, "test");
        Beacon found = dao.findByIdAndVisibility(b.getId(), true);

        assertThat(found).isNotNull();
        assertThat(found).isEqualToComparingFieldByField(b);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFindByAggregation() {
        List<Beacon> aggregators = dao.findByAggregation(true);
        assertThat(aggregators).extracting("processor").are(getNullCondition());

        List<Beacon> regulars = dao.findByAggregation(false);
        assertThat(regulars).extracting("visible").doesNotContainNull();

        assertThat(aggregators.size() + (long) regulars.size()).isEqualTo(countAll(Beacon.class));
    }

    @Test
    @UsingDataSet({"organization_1.json", "beacon_relationship.json"})
    public void testHaveRelationship() {
        Beacon parent = (Beacon) findById(Beacon.class, "parent");
        Beacon childWithParent = (Beacon) findById(Beacon.class, "childWithParent");

        assertThat(dao.haveRelationship(childWithParent, parent)).isTrue();
    }

    @Test
    @UsingDataSet({"organization_1.json", "beacon_relationship.json"})
    public void testDoNotHaveRelationship() {
        Beacon parent = (Beacon) findById(Beacon.class, "parent");
        Beacon childWithoutParent = (Beacon) findById(Beacon.class, "childWithoutParent");

        assertThat(dao.haveRelationship(childWithoutParent, parent)).isFalse();
    }

    @Test
    @UsingDataSet({"organization_1.json", "beacon_relationship.json"})
    public void testAddExistingRelationship() {
        Beacon parent = (Beacon) findById(Beacon.class, "parent");
        Beacon childWithParent = (Beacon) findById(Beacon.class, "childWithParent");

        boolean res = dao.addRelationship(childWithParent, parent);

        childWithParent = (Beacon) findById(Beacon.class, "childWithParent");
        assertThat(res).isFalse();
        assertThat(childWithParent.getParents()).doesNotHaveDuplicates().isNotEmpty().containsOnly(parent);
    }

    @Test
    @UsingDataSet({"organization_1.json", "beacon_relationship.json"})
    public void testAddNonExistingRelationship() {
        Beacon parent = (Beacon) findById(Beacon.class, "parent");
        Beacon childWithoutParent = (Beacon) findById(Beacon.class, "childWithoutParent");

        boolean res = dao.addRelationship(childWithoutParent, parent);

        childWithoutParent = (Beacon) findById(Beacon.class, "childWithParent");
        assertThat(res).isTrue();
        assertThat(childWithoutParent.getParents()).doesNotHaveDuplicates().isNotEmpty().containsOnly(parent);
    }

    @Test
    @UsingDataSet({"organization_1.json", "beacon_relationship.json"})
    public void testRemoveExistingRelationship() {
        Beacon parent = (Beacon) findById(Beacon.class, "parent");
        Beacon childWithParent = (Beacon) findById(Beacon.class, "childWithParent");

        assertThat(childWithParent.getParents()).doesNotHaveDuplicates().isNotEmpty().contains(parent);

        boolean res = dao.removeRelationship(childWithParent, parent);

        childWithParent = (Beacon) findById(Beacon.class, "childWithParent");
        assertThat(res).isTrue();
        assertThat(childWithParent.getParents()).isEmpty();
    }

    @Test
    @UsingDataSet({"organization_1.json", "beacon_relationship.json"})
    public void testRemoveNonExistingRelationship() {
        Beacon parent = (Beacon) findById(Beacon.class, "parent");
        Beacon childWithoutParent = (Beacon) findById(Beacon.class, "childWithoutParent");

        assertThat(childWithoutParent.getParents()).isEmpty();

        boolean res = dao.removeRelationship(childWithoutParent, parent);

        childWithoutParent = (Beacon) findById(Beacon.class, "childWithoutParent");
        assertThat(res).isFalse();
        assertThat(childWithoutParent.getParents()).isEmpty();
    }

    @Test
    @UsingDataSet({"organization_1.json", "beacon_descendants.json"})
    public void testFindAllDescendants() {
        Beacon parent = (Beacon) findById(Beacon.class, "root");
        Set<Beacon> all = findAll(Beacon.class).stream().map((BasicEntity e) -> (Beacon) e).collect(Collectors.toSet());

        Set<Beacon> bs = dao.findDescendants(parent, true, true, true, true);

        assertThat(bs).containsAll(all).doesNotHaveDuplicates().isNotEmpty();
    }

}
