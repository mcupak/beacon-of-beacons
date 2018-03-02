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
package com.dnastack.bob.service.impl;

import com.dnastack.bob.persistence.api.OrganizationDao;
import com.dnastack.bob.persistence.entity.Organization;
import com.dnastack.bob.service.api.OrganizationService;
import com.dnastack.bob.service.dto.OrganizationDto;
import com.dnastack.bob.service.mapper.api.OrganizationMapper;
import lombok.NonNull;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.Collection;

/**
 * Implementation of a service managing organizations.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Stateless
@Local(OrganizationService.class)
@Named
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

    @Inject
    private OrganizationDao organizationDao;

    @Inject
    private OrganizationMapper organizationMapper;

    @Override
    public OrganizationDto find(@NonNull String id) {
        return organizationMapper.mapEntityToDto(organizationDao.findByIdAndVisibility(id, true), false);
    }

    @Override
    public Collection<OrganizationDto> find() {
        return organizationMapper.mapEntitiesToDtos(organizationDao.findByVisibility(true), false);
    }

    @Override
    public Collection<OrganizationDto> find(Collection<String> ids) {
        return organizationMapper.mapEntitiesToDtos(organizationDao.findByIdsAndVisibility(ids, true), false);
    }


    @Override
    public OrganizationDto create(@NonNull OrganizationDto organization) {
        return organizationMapper.mapEntityToDto(organizationDao.save(organizationMapper.mapDtoToEntity(organization)),
                                                 false);
    }

    @Override
    public OrganizationDto update(@NonNull String id, @NonNull OrganizationDto organization) {
        Organization o = organizationDao.findById(id);
        if (organization.getId() != null) {
            o.setId(organization.getId());
        }
        if (organization.getName() != null) {
            o.setName(organization.getName());
        }
        if (organization.getDescription() != null) {
            o.setDescription(organization.getDescription());
        }
        if (organization.getUrl() != null) {
            o.setUrl(organization.getUrl());
        }
        if (organization.getAddress() != null) {
            o.setAddress(organization.getAddress());
        }

        return organizationMapper.mapEntityToDto(organizationDao.update(o), false);
    }

    @Override
    public void delete(@NonNull String id) {
        organizationDao.delete(id);
    }

}
