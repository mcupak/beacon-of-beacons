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
import java.util.List;
import javax.inject.Inject;
import org.hamcrest.Matchers;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.CleanupStrategy;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;

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

        assertThat(findAll(getEntityClass()), hasItem(samePropertyValuesAs(b)));
    }

    @Override
    @Test
    public void testDelete() {
        Beacon e = (Beacon) findOne(getEntityClass());
        dao.delete(e.getId());
        assertThat(findAll(getEntityClass()), not(hasItem(e)));
    }

    @Override
    @Test
    public void testFindById() {
        Beacon e = (Beacon) findOne(getEntityClass());
        Beacon e2 = dao.findById(e.getId());
        assertThat(e, equalTo(e2));
    }

    @Test
    public void testFindByVisibility() {
        Long count = countAll(getEntityClass());

        List<Beacon> visible = dao.findByVisibility(true);
        count -= visible.size();
        assertThat(visible, everyItem(Matchers.<Beacon>hasProperty("visible", equalTo(true))));
        List<Beacon> inVisible = dao.findByVisibility(false);
        count -= inVisible.size();
        assertThat(inVisible, everyItem(Matchers.<Beacon>hasProperty("visible", equalTo(false))));

        assertThat(count, equalTo(0L));
    }

    @Test
    public void testFindByAggregation() {
        Long count = countAll(getEntityClass());

        List<Beacon> aggregators = dao.findByAggregation(true);
        count -= aggregators.size();
        assertThat(aggregators, everyItem(Matchers.<Beacon>hasProperty("processor", nullValue())));

        List<Beacon> regulars = dao.findByAggregation(false);
        count -= regulars.size();
        assertThat(regulars, everyItem(Matchers.<Beacon>hasProperty("visible", notNullValue())));

        assertThat(count, equalTo(0L));
    }

}
