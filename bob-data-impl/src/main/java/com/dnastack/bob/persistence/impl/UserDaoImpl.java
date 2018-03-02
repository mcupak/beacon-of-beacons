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

import com.dnastack.bob.persistence.api.UserDao;
import com.dnastack.bob.persistence.entity.User;

import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * JPA-based implementation of user DAO.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Named
@Dependent
public class UserDaoImpl extends AbstractGenericDaoImpl<User, Long> implements UserDao {

    private static final long serialVersionUID = 5901767014411009095L;

    @Override
    public List<User> findByUserName(String userName) {
        List<User> us;
        try {
            TypedQuery<User> q = userName == null
                                 ? em.createNamedQuery("findUserByUserNameNull", User.class)
                                 : em.createNamedQuery("findUserByUserName", User.class)
                                     .setParameter("userName", userName);
            us = q.getResultList();
        } catch (PersistenceException ex) {
            us = null;
        }

        return us;
    }

}
