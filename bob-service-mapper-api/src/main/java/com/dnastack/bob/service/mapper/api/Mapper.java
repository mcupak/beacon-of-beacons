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
