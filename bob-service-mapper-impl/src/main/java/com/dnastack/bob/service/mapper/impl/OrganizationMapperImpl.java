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

import com.dnastack.bob.persistence.entity.Organization;
import com.dnastack.bob.service.dto.OrganizationDto;
import com.dnastack.bob.service.mapper.api.OrganizationMapper;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Default implementation of a mapper of organizations to their DTOs.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@ApplicationScoped
public class OrganizationMapperImpl implements OrganizationMapper {

    private static final long serialVersionUID = -2767891376938941979L;

    @Override
    public Organization mapDtoToEntity(OrganizationDto o) {
        return (o == null) ? null : Organization.builder().id(o.getId()).name(o.getName()).description(o.getDescription()).url(o.getUrl()).address(o.getAddress()).logo(o.getLogo()).createdDate(o.getCreatedDate()).build();
    }

    @Override
    public OrganizationDto mapEntityToDto(Organization o, boolean showInternal) {
        return (o == null) ? null : OrganizationDto.builder().id(o.getId()).name(o.getName()).address(o.getAddress()).description(o.getDescription()).url(o.getUrl()).logo(o.getLogo()).createdDate(o.getCreatedDate()).build();
    }

    @Override
    public Organization mapEntityToEntity(Organization o) {
        return (o == null) ? null : Organization.builder().id(o.getId()).name(o.getName()).address(o.getAddress()).description(o.getDescription()).url(o.getUrl()).logo(o.getLogo()).createdDate(o.getCreatedDate()).build();
    }

    @Override
    public OrganizationDto mapDtoToDto(OrganizationDto o) {
        return (o == null) ? null : OrganizationDto.builder().id(o.getId()).name(o.getName()).address(o.getAddress()).description(o.getDescription()).url(o.getUrl()).logo(o.getLogo()).createdDate(o.getCreatedDate()).build();
    }

    @Override
    public Set<OrganizationDto> mapEntitiesToDtos(Collection<Organization> os, boolean showInternal) {
        return (os == null) ? null : os.parallelStream().map((Organization o) -> mapEntityToDto(o, showInternal)).collect(Collectors.toSet());
    }

    @Override
    public Set<Organization> mapDtosToEntities(Collection<OrganizationDto> os) {
        return (os == null) ? null : os.parallelStream().map((OrganizationDto o) -> mapDtoToEntity(o)).collect(Collectors.toSet());
    }

}
