/*
 * The MIT License
 *
 * Copyright 2014 DNAstack.
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
package com.dnastack.bob.service.mapper.impl;

import com.dnastack.bob.persistence.entity.Query;
import com.dnastack.bob.service.dto.QueryDto;
import com.dnastack.bob.service.mapper.api.QueryMapper;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

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
        return (q == null) ? null : QueryDto.builder().chromosome(chromosomeMapper.mapEntityToDto(q.getChromosome(), showInternal)).position(q.getPosition()).allele(q.getAllele()).reference(referenceMapper.mapEntityToDto(q.getReference(), showInternal)).build();
    }

    @Override
    public Query mapDtoToEntity(QueryDto q) {
        return (q == null) ? null : Query.builder().chromosome(chromosomeMapper.mapDtoToEntity(q.getChromosome())).position(q.getPosition()).allele(q.getAllele()).reference(referenceMapper.mapDtoToEntity(q.getReference())).build();
    }

    @Override
    public Set<QueryDto> mapEntitiesToDtos(Collection<Query> qs, boolean showInternal) {
        return (qs == null) ? null : qs.parallelStream().map((Query q) -> mapEntityToDto(q, showInternal)).collect(Collectors.toSet());
    }

    @Override
    public Set<Query> mapDtosToEntities(Collection<QueryDto> qs) {
        return (qs == null) ? null : qs.parallelStream().map((QueryDto q) -> mapDtoToEntity(q)).collect(Collectors.toSet());
    }

    @Override
    public Query mapEntityToEntity(Query q) {
        return (q == null) ? null : Query.builder().chromosome(q.getChromosome()).position(q.getPosition()).allele(q.getAllele()).reference(q.getReference()).dataSet(q.getDataSet()).user(q.getUser()).submitted(new Date()).build();
    }

    @Override
    public QueryDto mapDtoToDto(QueryDto q) {
        return (q == null) ? null : QueryDto.builder().chromosome(q.getChromosome()).position(q.getPosition()).allele(q.getAllele()).reference(q.getReference()).build();
    }

}
