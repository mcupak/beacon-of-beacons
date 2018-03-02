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
package com.dnastack.bob.service.mapper.impl;

import com.dnastack.bob.persistence.enumerated.Reference;
import com.dnastack.bob.service.dto.ReferenceDto;
import com.dnastack.bob.service.mapper.api.ReferenceMapper;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Default implementation of a mapper of references to their DTOs.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@ApplicationScoped
public class ReferenceMapperImpl implements ReferenceMapper {

    private static final long serialVersionUID = -2454592064550045785L;

    @Override
    public Reference mapDtoToEntity(ReferenceDto r) {
        return (r == null) ? null : Reference.valueOf(r.name());
    }

    @Override
    public Set<Reference> mapDtosToEntities(Collection<ReferenceDto> rs) {
        return (rs == null)
               ? null
               : rs.parallelStream().map((ReferenceDto br) -> mapDtoToEntity(br)).collect(Collectors.toSet());
    }

    @Override
    public ReferenceDto mapEntityToDto(Reference r, boolean showInternal) {
        return (r == null) ? null : ReferenceDto.valueOf(r.name());
    }

    @Override
    public Reference mapEntityToEntity(Reference e) {
        return e;
    }

    @Override
    public ReferenceDto mapDtoToDto(ReferenceDto e) {
        return e;
    }

    @Override
    public Set<ReferenceDto> mapEntitiesToDtos(Collection<Reference> rs, boolean showInternal) {
        return (rs == null)
               ? null
               : rs.parallelStream()
                   .map((Reference br) -> mapEntityToDto(br, showInternal))
                   .collect(Collectors.toSet());
    }

}
