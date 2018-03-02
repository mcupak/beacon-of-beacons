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

import com.dnastack.bob.persistence.enumerated.Chromosome;
import com.dnastack.bob.service.dto.ChromosomeDto;
import com.dnastack.bob.service.mapper.api.ChromosomeMapper;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Default implementation of a mapper of chromosomes to their DTOs.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@ApplicationScoped
public class ChromosomeMapperImpl implements ChromosomeMapper {

    private static final long serialVersionUID = -259458823674113124L;

    @Override
    public ChromosomeDto mapEntityToDto(Chromosome c, boolean showInternal) {
        return (c == null) ? null : ChromosomeDto.valueOf(c.name());
    }

    @Override
    public Chromosome mapDtoToEntity(ChromosomeDto c) {
        return (c == null) ? null : Chromosome.valueOf(c.name());
    }

    @Override
    public Set<ChromosomeDto> mapEntitiesToDtos(Collection<Chromosome> cs, boolean showInternal) {
        return cs.parallelStream().map((Chromosome c) -> mapEntityToDto(c, showInternal)).collect(Collectors.toSet());
    }

    @Override
    public Set<Chromosome> mapDtosToEntities(Collection<ChromosomeDto> cs) {
        return cs.parallelStream().map((ChromosomeDto c) -> mapDtoToEntity(c)).collect(Collectors.toSet());
    }

    @Override
    public Chromosome mapEntityToEntity(Chromosome e) {
        return e;
    }

    @Override
    public ChromosomeDto mapDtoToDto(ChromosomeDto e) {
        return e;
    }

}
