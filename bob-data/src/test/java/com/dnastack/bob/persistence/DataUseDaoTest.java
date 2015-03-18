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

import com.dnastack.bob.persistence.api.DataUseDao;
import com.dnastack.bob.persistence.api.GenericDao;
import com.dnastack.bob.persistence.entity.BasicEntity;
import com.dnastack.bob.persistence.entity.DataUse;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.CleanupStrategy;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertThat;

/**
 * Data use DAO test.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RunWith(Arquillian.class)
@Transactional
@UsingDataSet("data_use_init.json")
@Cleanup(strategy = CleanupStrategy.USED_TABLES_ONLY) // this is important in order to prevent foreign-key violations
public class DataUseDaoTest extends EntityWithLongIdDaoTest {

    @Inject
    private DataUseDao dao;

    @Override
    public GenericDao<DataUse> getDao() {
        return dao;
    }

    @Override
    public Class<? extends BasicEntity> getEntityClass() {
        return DataUse.class;
    }

    @Override
    public List<BasicEntity> getNewData() {
        List<BasicEntity> res = new ArrayList<>();
        DataUse o = new DataUse();
        o.setCategory("new");
        o.setDescription("new");
        res.add(o);

        return res;
    }

    @Override
    @Test
    public void testUpdate() {
        DataUse e = (DataUse) findOne(getEntityClass());
        e.setCategory("updated");

        DataUse b = dao.update(e);

        assertThat(findAll(getEntityClass()), hasItem(samePropertyValuesAs(b)));
    }

    @Override
    @Test
    public void testDelete() {
        DataUse e = (DataUse) findOne(getEntityClass());
        dao.delete(e.getId());
        assertThat(findAll(getEntityClass()), not(hasItem(e)));
    }

    @Override
    @Test
    public void testFindById() {
        DataUse e = (DataUse) findOne(getEntityClass());
        DataUse e2 = dao.findById(e.getId());
        assertThat(e, equalTo(e2));
    }

}
