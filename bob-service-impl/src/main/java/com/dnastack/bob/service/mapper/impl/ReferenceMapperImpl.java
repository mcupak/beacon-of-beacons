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

import com.dnastack.bob.persistence.enumerated.Reference;
import com.dnastack.bob.service.dto.ReferenceDto;
import com.dnastack.bob.service.mapper.api.ReferenceMapper;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;

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
        return (rs == null) ? null : rs.parallelStream().map((ReferenceDto br) -> mapDtoToEntity(br)).collect(Collectors.toSet());
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
        return (rs == null) ? null : rs.parallelStream().map((Reference br) -> mapEntityToDto(br, showInternal)).collect(Collectors.toSet());
    }

}
