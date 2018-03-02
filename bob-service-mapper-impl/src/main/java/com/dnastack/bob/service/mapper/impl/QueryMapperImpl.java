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

import com.dnastack.bob.persistence.entity.Query;
import com.dnastack.bob.service.dto.QueryDto;
import com.dnastack.bob.service.mapper.api.QueryMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Default implementation of a mapper of queries to their DTOs.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@ApplicationScoped
public class QueryMapperImpl implements QueryMapper {

    private static final long serialVersionUID = -3218469571160524447L;

    @Inject
    private ChromosomeMapperImpl chromosomeMapper;

    @Inject
    private ReferenceMapperImpl referenceMapper;

    @Override
    public QueryDto mapEntityToDto(Query q, boolean showInternal) {
        return (q == null)
               ? null
               : QueryDto.builder()
                         .chromosome(chromosomeMapper.mapEntityToDto(q.getChromosome(), showInternal))
                         .position(q.getPosition())
                         .allele(q.getAllele())
                         .reference(referenceMapper.mapEntityToDto(q.getReference(), showInternal))
                         .build();
    }

    @Override
    public Query mapDtoToEntity(QueryDto q) {
        return (q == null)
               ? null
               : Query.builder()
                      .chromosome(chromosomeMapper.mapDtoToEntity(q.getChromosome()))
                      .position(q.getPosition())
                      .allele(q.getAllele())
                      .reference(referenceMapper.mapDtoToEntity(q.getReference()))
                      .build();
    }

    @Override
    public Set<QueryDto> mapEntitiesToDtos(Collection<Query> qs, boolean showInternal) {
        return (qs == null)
               ? null
               : qs.parallelStream().map((Query q) -> mapEntityToDto(q, showInternal)).collect(Collectors.toSet());
    }

    @Override
    public Set<Query> mapDtosToEntities(Collection<QueryDto> qs) {
        return (qs == null)
               ? null
               : qs.parallelStream().map((QueryDto q) -> mapDtoToEntity(q)).collect(Collectors.toSet());
    }

    @Override
    public Query mapEntityToEntity(Query q) {
        return (q == null)
               ? null
               : Query.builder()
                      .chromosome(q.getChromosome())
                      .position(q.getPosition())
                      .allele(q.getAllele())
                      .reference(q.getReference())
                      .dataSet(q.getDataSet())
                      .user(q.getUser())
                      .submitted(new Date())
                      .build();
    }

    @Override
    public QueryDto mapDtoToDto(QueryDto q) {
        return (q == null)
               ? null
               : QueryDto.builder()
                         .chromosome(q.getChromosome())
                         .position(q.getPosition())
                         .allele(q.getAllele())
                         .reference(q.getReference())
                         .build();
    }

}
