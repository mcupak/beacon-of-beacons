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

import com.dnastack.bob.persistence.api.DataUseDao;
import com.dnastack.bob.persistence.entity.DataUse;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

/**
 * JPA-based implementation of data use DAO.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Named
@Dependent
public class DataUseDaoImpl extends AbstractGenericDaoImpl<DataUse, Long> implements DataUseDao {

    private static final long serialVersionUID = -3202753985625190279L;

}
