/*
 * The MIT License
 *
 * Copyright 2014 Miroslav Cupak (mirocupak@gmail.com).
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
    public OrganizationDto find(String organizationId) {
        return organizationMapper.mapEntityToDto(organizationDao.findById(organizationId), false);
    }

    @Override
    public Collection<OrganizationDto> findAll() {
        return organizationMapper.mapEntitiesToDtos(organizationDao.findAll(), false);
    }

    @Override
    public Collection<OrganizationDto> findWithVisibleBeacons() {
        return organizationMapper.mapEntitiesToDtos(organizationDao.findByVisibility(true), false);
    }

    @Override
    public Collection<OrganizationDto> find(Collection<String> ids) {
        return organizationMapper.mapEntitiesToDtos(organizationDao.findByIds(ids), false);
    }

    @Override
    public OrganizationDto create(@NonNull OrganizationDto organization) {
        return organizationMapper.mapEntityToDto(organizationDao.save(organizationMapper.mapDtoToEntity(organization)), false);
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

    @Override
    public OrganizationDto findWithVisibleBeacons(@NonNull String id) {
        return organizationMapper.mapEntityToDto(organizationDao.findByIdAndVisibility(id, true), false);
    }

    @Override
    public Collection<OrganizationDto> findWithVisibleBeacons(Collection<String> ids) {
        return organizationMapper.mapEntitiesToDtos(organizationDao.findByIdsAndVisibility(ids, true), false);
    }

}
