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

import com.dnastack.bob.persistence.api.BeaconDao;
import com.dnastack.bob.persistence.api.OrganizationDao;
import com.dnastack.bob.persistence.entity.Beacon;
import com.dnastack.bob.persistence.entity.Organization;
import com.dnastack.bob.service.api.BeaconService;
import com.dnastack.bob.service.converter.impl.BeaconIdBeaconConverter;
import com.dnastack.bob.service.converter.impl.EmptyAlleleConverter;
import com.dnastack.bob.service.converter.impl.EmptyChromosomeConverter;
import com.dnastack.bob.service.converter.impl.EmptyPositionConverter;
import com.dnastack.bob.service.converter.impl.EmptyReferenceConverter;
import com.dnastack.bob.service.dto.BeaconDto;
import com.dnastack.bob.service.fetcher.impl.GetResponseFetcher;
import com.dnastack.bob.service.parser.impl.JsonResponseExistsResponseParser;
import com.dnastack.bob.service.requester.impl.RefChromPosAlleleRequestConstructor;
import com.dnastack.bob.service.util.CdiBeanResolver;
import com.dnastack.bob.service.util.EjbResolver;
import com.dnastack.bob.service.util.EntityDtoConvertor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

/**
 * Implementation of a service managing beacons.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Stateless
@Local(BeaconService.class)
@Named
@Transactional
public class BeaconServiceImpl implements BeaconService {

    public static final String QUERY_URL = "/query?reference=%s&chromosome=%s&position=%d&referenceBases=&alternateBases=%s&dataset=";
    public static final String AUTH = "OAUTH2.0";

    @Inject
    private BeaconDao beaconDao;

    @Inject
    private OrganizationDao organizationDao;

    @Inject
    private CdiBeanResolver cdiResolver;

    @Inject
    private EjbResolver ejbResolver;

    private String generateId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public BeaconDto find(String beaconId) {
        Beacon b = beaconDao.findById(beaconId);
        return EntityDtoConvertor.getBeaconDto((b == null || !b.getVisible()) ? null : b, false);
    }

    @Override
    public Collection<BeaconDto> find(Collection<String> beaconIds) {
        List<BeaconDto> res = new ArrayList<>();
        for (String id : beaconIds) {
            BeaconDto b = find(id);
            if (b != null) {
                res.add(b);
            }
        }

        return res;
    }

    @Override
    public Set<BeaconDto> findAll() {
        return EntityDtoConvertor.getBeaconDtos(beaconDao.findByVisibility(true), false);
    }

    @Override
    public BeaconDto create(BeaconDto beacon) {
        if (beacon == null) {
            throw new NullPointerException("beacon");
        }

        Organization o = organizationDao.findByName(beacon.getOrganization());
        if (o == null) {
//            o = new Organization();
//            o.setId(generateId());
//            o.setName(beacon.getOrganization());
//            organizationDao.save(o);
            throw new IllegalArgumentException("organization");
        }

        Beacon b = EntityDtoConvertor.getBeacon(beacon);
        b.setOrganization(o);
        b.setAuth(AUTH);
        b.setUrl(beacon.getUrl() + QUERY_URL);

        b.setParser(ejbResolver.getClassId(JsonResponseExistsResponseParser.class));
        b.setFetcher(ejbResolver.getClassId(GetResponseFetcher.class));
        b.setRequester(cdiResolver.getClassId(RefChromPosAlleleRequestConstructor.class));
        b.setReferenceConverter(cdiResolver.getClassId(EmptyReferenceConverter.class));
        b.setChromosomeConverter(cdiResolver.getClassId(EmptyChromosomeConverter.class));
        b.setPositionConverter(cdiResolver.getClassId(EmptyPositionConverter.class));
        b.setAlleleConverter(cdiResolver.getClassId(EmptyAlleleConverter.class));
        b.setBeaconConverter(cdiResolver.getClassId(BeaconIdBeaconConverter.class));

        return EntityDtoConvertor.getBeaconDto(beaconDao.save(b), false);
    }

    @Override
    public BeaconDto update(String id, BeaconDto beacon) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(String id) {
        if (id == null) {
            throw new NullPointerException("id");
        }
        beaconDao.delete(id);
    }

}
