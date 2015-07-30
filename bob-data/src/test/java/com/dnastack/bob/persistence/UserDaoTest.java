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
import com.dnastack.bob.persistence.api.UserDao;
import com.dnastack.bob.persistence.entity.BasicEntity;
import com.dnastack.bob.persistence.entity.User;
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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * User DAO test.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RunWith(Arquillian.class)
@Transactional
@UsingDataSet("user_init.json")
@Cleanup(strategy = CleanupStrategy.USED_TABLES_ONLY) // this is important in order to prevent foreign-key violations
public class UserDaoTest extends EntityWithLongIdDaoTest {

    @Inject
    private UserDao dao;

    @Override
    public GenericDao<User> getDao() {
        return dao;
    }

    @Override
    public Class<? extends BasicEntity> getEntityClass() {
        return User.class;
    }

    @Override
    public List<BasicEntity> getNewData() {
        List<BasicEntity> res = new ArrayList<>();
        User o = new User();
        o.setIp("0.0.0.0");
        res.add(o);

        return res;
    }

    @Override
    @Test
    public void testUpdate() {
        User e = (User) findOne(getEntityClass());
        e.setIp("unknown");

        User b = dao.update(e);

        assertThat(findAll(getEntityClass())).contains(b);
    }

    @Override
    @Test
    public void testDelete() {
        User e = (User) findOne(getEntityClass());

        dao.delete(e.getId());

        assertThat(findAll(getEntityClass())).doesNotContain(e);
    }

    @Override
    @Test
    public void testFindById() {
        User e = (User) findOne(getEntityClass());

        User e2 = dao.findById(e.getId());

        assertThat(e).isEqualTo(e2);
    }

}
