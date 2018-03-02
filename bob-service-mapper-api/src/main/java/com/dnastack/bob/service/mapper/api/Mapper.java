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
package com.dnastack.bob.service.mapper.api;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * Mapper of entities to their DTOs.
 *
 * @param <E> entity type
 * @param <D> dto type
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public interface Mapper<E, D> extends Serializable {

    /**
     * Maps an entity to a DTO.
     *
     * @param e            entity
     * @param showInternal true if internal fields should be included in the DTO, false otherwise
     * @return dto
     */
    D mapEntityToDto(E e, boolean showInternal);

    /**
     * Maps a DTO to an entity.
     *
     * @param d dto
     * @return entity
     */
    E mapDtoToEntity(D d);

    /**
     * Clones an entity to a new entity.
     *
     * @param e entity
     * @return entity
     */
    E mapEntityToEntity(E e);

    /**
     * Clones a DTO to a new DTO.
     *
     * @param d dto
     * @return dto
     */
    D mapDtoToDto(D d);

    /**
     * Maps a collection of entities to a collection of DTOs.
     *
     * @param es           entities
     * @param showInternal true if internal fields should be included in the DTO, false otherwise
     * @return dtos
     */
    Set<D> mapEntitiesToDtos(Collection<E> es, boolean showInternal);

    /**
     * Maps a collection of DTOs to a collection of entities.
     *
     * @param ds dtos
     * @return entities
     */
    Set<E> mapDtosToEntities(Collection<D> ds);
}
