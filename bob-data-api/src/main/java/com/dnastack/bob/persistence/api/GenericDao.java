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
package com.dnastack.bob.persistence.api;

import com.dnastack.bob.persistence.entity.BasicEntity;

import java.io.Serializable;
import java.util.List;

/**
 * DAO for generic entities.
 *
 * @param <T> entity type
 * @param <I> ID type
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public interface GenericDao<T extends BasicEntity<I>, I> extends Serializable {

    long countAll();

    T save(T entity);

    T update(T entity);

    List<T> findAll();

    void flush();

    void delete(I id);

    T findById(I id);

    T getReferenceById(I id);

}
