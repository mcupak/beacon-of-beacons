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

import com.dnastack.bob.persistence.api.GenericDao;
import com.dnastack.bob.persistence.api.OrganizationDao;
import com.dnastack.bob.persistence.entity.BasicEntity;
import com.dnastack.bob.persistence.entity.Organization;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Organization DAO test.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RunWith(Arquillian.class)
@Transactional
@UsingDataSet("organization_1.json")
@Cleanup(strategy = CleanupStrategy.USED_TABLES_ONLY) // this is important in order to prevent foreign-key violations
public class OrganizationDaoTest extends GenericDaoTest<Organization, String> {

    @Inject
    private OrganizationDao dao;

    @Override
    public GenericDao<Organization, String> getDao() {
        return dao;
    }

    @Override
    public List<Organization> getEntitiesForSave() {
        List<Organization> res = new ArrayList<>();
        Organization o = new Organization();
        o.setId("new");
        o.setName("new");
        o.setCreatedDate(Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        res.add(o);

        return res;
    }

    @Override
    public Organization getEntityForUpdate(Organization e) {
        e.setName("updated");
        return e;
    }

    @Test
    public void testFindByName() {
        Organization o = (Organization) findOne(Organization.class);
        Organization found = dao.findByName(o.getName());

        assertThat(found.getName()).isEqualTo(o.getName());
    }

    @Test
    @UsingDataSet({"organization_1.json", "organization_2.json", "beacon_1.json"})
    public void testFindWithBeacons() {
        Organization o = (Organization) findById(Organization.class, "test");
        List<Organization> found = dao.findByVisibility(true);

        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(o);
    }

    @Test
    @UsingDataSet({"organization_1.json", "organization_2.json", "beacon_1.json"})
    public void testFindWithBeaconsAndId() {
        Organization o = (Organization) findById(Organization.class, "test");
        Organization found = dao.findByIdAndVisibility(o.getId(), true);

        assertThat(found).isNotNull();
        assertThat(found).isEqualToComparingFieldByField(o);
    }

    @Test
    @UsingDataSet({"organization_1.json", "organization_2.json", "beacon_1.json"})
    public void testFindWithoutBeaconsAndId() {
        Organization o = (Organization) findById(Organization.class, "test2");

        Organization found = dao.findByIdAndVisibility(o.getId(), true);

        assertThat(found).isNull();
    }

    @Test
    @UsingDataSet({"organization_1.json", "organization_2.json", "beacon_1.json"})
    public void testFindWithBeaconsAndIds() {
        Organization o = (Organization) findById(Organization.class, "test");

        List<Organization> found = dao.findByIdsAndVisibility(ImmutableSet.of(o.getId(), "test2"), true);

        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(o);
    }

    @Test
    @UsingDataSet({"organization_1.json", "organization_2.json"})
    public void testFindByIds() {
        Set<Organization> all = findAll(Organization.class).stream().map((BasicEntity e) -> (Organization) e).collect(Collectors.toSet());

        List<Organization> found = dao.findByIds(ImmutableSet.of("test", "test2"));

        assertThat(found.size()).isEqualTo(2);
        assertThat(found).containsAll(all);
    }

    @Test
    @UsingDataSet({"organization_1.json", "organization_2.json", "beacon_2.json"})
    public void testFindVisible() {
        List<Organization> found = dao.findByVisibility(true);

        assertThat(found).isEmpty();
    }
}
