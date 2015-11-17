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
package com.dnastack.bob.persistence.impl;

import com.dnastack.bob.persistence.api.UserDao;
import com.dnastack.bob.persistence.entity.User;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

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
            TypedQuery<User> q = userName == null ? em.createNamedQuery("findUserByUserNameNull", User.class) : em.createNamedQuery("findUserByUserName", User.class).setParameter("userName", userName);
            us = q.getResultList();
        } catch (PersistenceException ex) {
            us = null;
        }

        return us;
    }

    @Override
    public List<User> findByIp(String ip) {
        List<User> us;
        try {
            TypedQuery<User> q = ip == null ? em.createNamedQuery("findUserByIpNull", User.class) : em.createNamedQuery("findUserByIp", User.class).setParameter("ip", ip);
            us = q.getResultList();
        } catch (PersistenceException ex) {
            us = null;
        }

        return us;
    }

}