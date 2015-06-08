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
import com.dnastack.bob.service.util.EntityDtoConvertor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

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
    
    @Override
    public OrganizationDto find(String organizationId) {
        Organization o = organizationDao.findById(organizationId);
        return EntityDtoConvertor.getOrganizationDto((o == null) ? null : o);
    }
    
    @Override
    public Collection<OrganizationDto> findAll() {
        return EntityDtoConvertor.getOrganizationDtos(organizationDao.findAll());
    }
    
    @Override
    public Collection<OrganizationDto> find(Collection<String> ids) {
        List<OrganizationDto> res = new ArrayList<>();
        for (String id : ids) {
            OrganizationDto b = find(id);
            if (b != null) {
                res.add(b);
            }
        }
        
        return res;
    }
    
    @Override
    public OrganizationDto create(OrganizationDto organization) {
        if (organization == null) {
            throw new NullPointerException("organization");
        }
        return EntityDtoConvertor.getOrganizationDto(organizationDao.save(EntityDtoConvertor.getOrganization(organization)));
    }
    
    @Override
    public OrganizationDto update(String id, OrganizationDto organization) {
        if (organization == null) {
            throw new NullPointerException("organization");
        }
        
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
        
        return EntityDtoConvertor.getOrganizationDto(organizationDao.update(o));
    }
    
    @Override
    public void delete(String id) {
        if (id == null) {
            throw new NullPointerException("id");
        }
        organizationDao.delete(id);
    }
    
}
