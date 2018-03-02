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

import com.dnastack.bob.persistence.api.GenericDao;
import com.dnastack.bob.persistence.api.UserDao;
import com.dnastack.bob.persistence.entity.BasicEntity;
import com.dnastack.bob.persistence.entity.User;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.CleanupStrategy;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.transaction.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

/**
 * User DAO test.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RunWith(Arquillian.class)
@Transactional
@UsingDataSet("user.json")
@Cleanup(strategy = CleanupStrategy.USED_TABLES_ONLY) // this is important in order to prevent foreign-key violations
public class UserDaoTest extends GenericDaoTest<User, Long> {

    @Inject
    private UserDao dao;

    @Inject
    private UserTransaction utx;

    @Override
    public GenericDao<User, Long> getDao() {
        return dao;
    }

    @Override
    public List<User> getEntitiesForSave() {
        List<User> res = new ArrayList<>();
        res.add(User.builder().userName("new").build());

        return res;
    }

    @Override
    public User getEntityForUpdate(User e) {
        e.setUserName("unknown");
        return e;
    }

    @Test
    public void testFindByUserName() {
        User u = (User) findOne(User.class);
        List<User> found = dao.findByUserName(u.getUserName());

        assertThat(found.size()).isEqualTo(1);
        assertThat(found.get(0)).isEqualToComparingOnlyGivenFields(u, "userName");
    }

    @Test
    @Transactional(TransactionMode.DISABLED)
    @UsingDataSet("user.json")
    public void testSaveDuplicateUserName() throws NotSupportedException, SystemException {
        User u = (User) findOne(User.class);
        User u2 = User.builder().userName(u.getUserName()).build();

        utx.begin();
        assertThatThrownBy(() -> {
            dao.save(u2);
            utx.commit();
        }).isInstanceOf(RollbackException.class);
    }

    @Test
    @UsingDataSet("user.json")
    @Transactional(TransactionMode.DISABLED)
    @Override
    public void testSave() {
        User u = (User) findOne(User.class);
        User u2 = User.builder().userName("different").build();

        try {
            utx.begin();
            u2 = getDao().save(u2);
            utx.commit();
        } catch (NotSupportedException | SystemException | HeuristicMixedException | RollbackException | HeuristicRollbackException e) {
            fail("Could not handle transaction");
        }

        List<User> findAll = findAll(User.class).parallelStream()
                                                .map((BasicEntity e) -> (User) e)
                                                .collect(Collectors.toList());
        assertThat(findAll.size()).isEqualTo(2);
        assertThat(findAll).contains(u, u2);
    }

}
