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

import com.dnastack.bob.persistence.api.DataUseRequirementDao;
import com.dnastack.bob.persistence.api.GenericDao;
import com.dnastack.bob.persistence.entity.DataUseRequirement;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.CleanupStrategy;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Data use requirement DAO test.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RunWith(Arquillian.class)
@Transactional
@UsingDataSet("data_use_requirement.json")
@Cleanup(strategy = CleanupStrategy.USED_TABLES_ONLY) // this is important in order to prevent foreign-key violations
public class DataUseRequirementDaoTest extends GenericDaoTest<DataUseRequirement, Long> {

    @Inject
    private DataUseRequirementDao dao;

    @Override
    public GenericDao<DataUseRequirement, Long> getDao() {
        return dao;
    }

    @Override
    public List<DataUseRequirement> getEntitiesForSave() {
        List<DataUseRequirement> res = new ArrayList<>();
        DataUseRequirement o = new DataUseRequirement();
        o.setName("new");
        o.setDescription("new");
        res.add(o);

        return res;
    }

    @Override
    public DataUseRequirement getEntityForUpdate(DataUseRequirement e) {
        e.setName("updated");
        return e;
    }

}
