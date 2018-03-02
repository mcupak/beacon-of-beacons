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
        return (o == null)
               ? null
               : Organization.builder()
                             .id(o.getId())
                             .name(o.getName())
                             .description(o.getDescription())
                             .url(o.getUrl())
                             .address(o.getAddress())
                             .logo(o.getLogo())
                             .createdDate(o.getCreatedDate())
                             .build();
    }

    @Override
    public OrganizationDto mapEntityToDto(Organization o, boolean showInternal) {
        return (o == null)
               ? null
               : OrganizationDto.builder()
                                .id(o.getId())
                                .name(o.getName())
                                .address(o.getAddress())
                                .description(o.getDescription())
                                .url(o.getUrl())
                                .logo(o.getLogo())
                                .createdDate(o.getCreatedDate())
                                .build();
    }

    @Override
    public Organization mapEntityToEntity(Organization o) {
        return (o == null)
               ? null
               : Organization.builder()
                             .id(o.getId())
                             .name(o.getName())
                             .address(o.getAddress())
                             .description(o.getDescription())
                             .url(o.getUrl())
                             .logo(o.getLogo())
                             .createdDate(o.getCreatedDate())
                             .build();
    }

    @Override
    public OrganizationDto mapDtoToDto(OrganizationDto o) {
        return (o == null)
               ? null
               : OrganizationDto.builder()
                                .id(o.getId())
                                .name(o.getName())
                                .address(o.getAddress())
                                .description(o.getDescription())
                                .url(o.getUrl())
                                .logo(o.getLogo())
                                .createdDate(o.getCreatedDate())
                                .build();
    }

    @Override
    public Set<OrganizationDto> mapEntitiesToDtos(Collection<Organization> os, boolean showInternal) {
        return (os == null)
               ? null
               : os.parallelStream()
                   .map((Organization o) -> mapEntityToDto(o, showInternal))
                   .collect(Collectors.toSet());
    }

    @Override
    public Set<Organization> mapDtosToEntities(Collection<OrganizationDto> os) {
        return (os == null)
               ? null
               : os.parallelStream().map((OrganizationDto o) -> mapDtoToEntity(o)).collect(Collectors.toSet());
    }

}
