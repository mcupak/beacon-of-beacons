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

import com.dnastack.bob.persistence.api.BeaconDao;
import com.dnastack.bob.persistence.api.OrganizationDao;
import com.dnastack.bob.persistence.entity.Beacon;
import com.dnastack.bob.persistence.entity.Organization;
import com.dnastack.bob.service.api.BeaconService;
import com.dnastack.bob.service.converter.impl.*;
import com.dnastack.bob.service.dto.BeaconDto;
import com.dnastack.bob.service.fetcher.impl.GetResponseFetcher;
import com.dnastack.bob.service.mapper.api.BeaconMapper;
import com.dnastack.bob.service.parser.impl.JsonResponseExistsResponseParser;
import com.dnastack.bob.service.parser.impl.JsonResponseExternalUrlParser;
import com.dnastack.bob.service.processor.util.CdiBeanResolver;
import com.dnastack.bob.service.processor.util.EjbResolver;
import com.dnastack.bob.service.requester.impl.RefChromPosAlleleRequestConstructor;
import com.google.common.collect.ImmutableSet;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

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
@Log
public class BeaconServiceImpl implements BeaconService {

    public static final String QUERY_URL = "/query?reference=%s&chromosome=%s&position=%d&referenceBases=&alternateBases=%s&dataset=";
    public static final String AUTH = "OAUTH2.0";
    public static final String PARENT_ID = "dnastack";
    public static final String BOB_ID = "bob";

    @Inject
    private BeaconDao beaconDao;

    @Inject
    private OrganizationDao organizationDao;

    @Inject
    private CdiBeanResolver cdiResolver;

    @Inject
    private EjbResolver ejbResolver;

    @Inject
    private BeaconMapper beaconMapper;

    private String generateId() {
        return UUID.randomUUID().toString();
    }

    private boolean beaconExists(@NonNull BeaconDto beacon) {
        return beaconDao.findById(beacon.getId()) != null;
    }

    private Beacon getBeaconForSaving(@NonNull BeaconDto beacon) {
        Organization o = organizationDao.findByName(beacon.getOrganization());
        if (o == null) {
            throw new IllegalArgumentException("organization");
        }

        Beacon b = beaconMapper.mapDtoToEntity(beacon);
        b.setOrganization(o);
        b.setAuth(AUTH);
        b.setUrl(beacon.getUrl() + QUERY_URL);
        b.setEnabled(beacon.isEnabled());
        b.setVisible(beacon.isVisible());

        b.setCreatedDate(Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        b.setResponseParser(ejbResolver.getClassId(JsonResponseExistsResponseParser.class));
        b.setExternalUrlParser(ejbResolver.getClassId(JsonResponseExternalUrlParser.class));
        b.setFetcher(ejbResolver.getClassId(GetResponseFetcher.class));
        b.setRequester(cdiResolver.getClassId(RefChromPosAlleleRequestConstructor.class));
        b.setReferenceConverter(cdiResolver.getClassId(EmptyReferenceConverter.class));
        b.setChromosomeConverter(cdiResolver.getClassId(EmptyChromosomeConverter.class));
        b.setPositionConverter(cdiResolver.getClassId(EmptyPositionConverter.class));
        b.setAlleleConverter(cdiResolver.getClassId(EmptyAlleleConverter.class));
        b.setBeaconConverter(cdiResolver.getClassId(BeaconIdBeaconConverter.class));

        Beacon parent = beaconDao.findById(PARENT_ID);
        Beacon bob = beaconDao.findById(BOB_ID);
        if (bob != null) {
            b.setParents(parent == null ? ImmutableSet.of(bob) : ImmutableSet.of(parent, bob));
        }

        return b;
    }

    @Override
    public BeaconDto find(String id) {
        Beacon b = beaconDao.findByIdAndVisibility(id, true);
        return beaconMapper.mapEntityToDto(b, beaconDao.findDescendants(b, false, false, false, false), false);
    }

    @Override
    public Collection<BeaconDto> find(Collection<String> ids) {
        return beaconDao.findByIdsAndVisibility(ids, true)
                        .stream()
                        .map(b -> beaconMapper.mapEntityToDto(b,
                                                              beaconDao.findDescendants(b, false, false, false, false),
                                                              false))
                        .collect(Collectors.toSet());
    }

    @Override
    public Collection<BeaconDto> find() {
        return beaconDao.findByVisibility(true)
                        .stream()
                        .map(b -> beaconMapper.mapEntityToDto(b,
                                                              beaconDao.findDescendants(b, false, false, false, false),
                                                              false))
                        .collect(Collectors.toSet());
    }

    @Override
    public BeaconDto create(@NonNull BeaconDto beacon) {
        Beacon res = beaconDao.save(getBeaconForSaving(beacon));
        log.info(String.format("Beacon created: %s", res.getId()));

        return beaconMapper.mapEntityToDto(res, false);
    }

    @Override
    public BeaconDto update(@NonNull String id, @NonNull BeaconDto beacon) {
        Beacon b = beaconDao.findById(id);
        if (b == null) {
            throw new IllegalArgumentException("Could not find beacon with ID: " + id);
        }

        Beacon res = beaconDao.update(getBeaconForSaving(beacon));
        log.info(String.format("Beacon updated: %s", res.getId()));

        return beaconMapper.mapEntityToDto(res, false);
    }

    @Override
    public void delete(@NonNull String id) {
        Beacon b = beaconDao.findById(id);
        if (b == null) {
            throw new IllegalArgumentException("Could not find beacon with ID: " + id);
        }
        b.setEnabled(false);
        b.setVisible(false);
        beaconDao.update(b);
        log.info(String.format("Beacon disabled: %s", b.getId()));
    }

}
