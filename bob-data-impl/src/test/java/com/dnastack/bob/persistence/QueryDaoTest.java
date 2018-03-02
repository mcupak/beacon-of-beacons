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
import com.dnastack.bob.persistence.api.QueryDao;
import com.dnastack.bob.persistence.entity.Query;
import com.dnastack.bob.persistence.entity.User;
import com.dnastack.bob.persistence.enumerated.Chromosome;
import com.dnastack.bob.persistence.enumerated.Reference;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.CleanupStrategy;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Query DAO test.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RunWith(Arquillian.class)
@Transactional
@UsingDataSet({"user.json", "query.json"})
@Cleanup(strategy = CleanupStrategy.USED_TABLES_ONLY) // this is important in order to prevent foreign-key violations
public class QueryDaoTest extends GenericDaoTest<Query, Long> {

    @Inject
    private QueryDao dao;

    @Override
    public GenericDao<Query, Long> getDao() {
        return dao;
    }

    @Override
    public List<Query> getEntitiesForSave() {
        List<Query> res = new ArrayList<>();
        Query o = new Query();
        o.setAllele("G");
        o.setChromosome(Chromosome.CHR22);
        o.setReference(Reference.HG38);
        o.setPosition(10L);
        o.setSubmitted(new Date());
        o.setUser((User) findOne(User.class));
        res.add(o);

        return res;
    }

    @Override
    public Query getEntityForUpdate(Query e) {
        e.setAllele("T");
        return e;
    }

}
