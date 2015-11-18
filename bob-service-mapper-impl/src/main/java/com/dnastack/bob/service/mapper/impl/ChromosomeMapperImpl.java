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

import com.dnastack.bob.persistence.enumerated.Chromosome;
import com.dnastack.bob.service.dto.ChromosomeDto;
import com.dnastack.bob.service.mapper.api.ChromosomeMapper;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;

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
