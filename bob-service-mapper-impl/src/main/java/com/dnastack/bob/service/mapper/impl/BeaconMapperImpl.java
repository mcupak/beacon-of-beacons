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

import com.dnastack.bob.persistence.entity.Beacon;
import com.dnastack.bob.service.dto.BeaconDto;
import com.dnastack.bob.service.mapper.api.BeaconMapper;
import com.dnastack.bob.service.mapper.api.ReferenceMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Default implementation of a mapper of beacons to their DTOs.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@ApplicationScoped
public class BeaconMapperImpl implements BeaconMapper {

    private static final long serialVersionUID = 3074778941535018981L;
    @Inject
    private ReferenceMapper referenceMapper;

    @Override
    public BeaconDto mapEntityToDto(Beacon b, boolean showInternal) {
        return mapEntityToDto(b, null, showInternal);
    }

    @Override
    public Set<BeaconDto> mapEntitiesToDtos(Collection<Beacon> bs, boolean showInternal) {
        return (bs == null)
               ? null
               : bs.parallelStream()
                   .map((Beacon br) -> mapEntityToDto(br, null, showInternal))
                   .collect(Collectors.toSet());
    }

    @Override
    public BeaconDto mapEntityToDto(Beacon b, Set<Beacon> descendants, boolean showInternal) {
        if (b == null) {
            return null;
        }

        BeaconDto.BeaconDtoBuilder builder = BeaconDto.builder()
                                                      .id(b.getId())
                                                      .name(b.getName())
                                                      .aggregator(b.getAggregator())
                                                      .organization(b.getOrganization() == null
                                                                    ? null
                                                                    : b.getOrganization().getName())
                                                      .description(b.getDescription())
                                                      .createdDate(b.getCreatedDate())
                                                      .supportedReferences(referenceMapper.mapEntitiesToDtos(b.getSupportedReferences(),
                                                                                                             showInternal));

        if (showInternal) {
            builder.email(b.getEmail())
                   .homePage(b.getHomePage())
                   .enabled(b.getEnabled())
                   .visible(b.getVisible())
                   .url(b.getUrl());
        }

        if (b.getAggregator() != null && b.getAggregator() == true) {
            builder.aggregatedBeacons(mapEntitiesToDtos(descendants, showInternal));
        }

        return builder.build();
    }

    @Override
    public Beacon mapDtoToEntity(BeaconDto b) {
        return b == null
               ? null
               : Beacon.builder()
                       .id(b.getId())
                       .name(b.getName())
                       .aggregator(b.isAggregator())
                       .description(b.getDescription())
                       .enabled(b.isEnabled())
                       .visible(b.isVisible())
                       .email(b.getEmail())
                       .homePage(b.getHomePage())
                       .url(b.getUrl())
                       .supportedReferences(referenceMapper.mapDtosToEntities(b.getSupportedReferences()))
                       .createdDate(b.getCreatedDate())
                       .build();
    }

    @Override
    public Set<BeaconDto> mapEntitiesToDtos(Map<Beacon, Set<Beacon>> beaconsWithDescendants, boolean showInternal) {
        return (beaconsWithDescendants == null)
               ? null
               : beaconsWithDescendants.entrySet()
                                       .parallelStream()
                                       .map((Map.Entry<Beacon, Set<Beacon>> e) -> mapEntityToDto(e.getKey(),
                                                                                                 e.getValue(),
                                                                                                 showInternal))
                                       .collect(Collectors.toSet());
    }

    @Override
    public Set<Beacon> mapDtosToEntities(Collection<BeaconDto> bs) {
        return (bs == null)
               ? null
               : bs.parallelStream().map((BeaconDto b) -> mapDtoToEntity(b)).collect(Collectors.toSet());
    }

    @Override
    public Beacon mapEntityToEntity(Beacon b) {
        return b == null
               ? null
               : Beacon.builder()
                       .id(b.getId())
                       .name(b.getName())
                       .aggregator(b.getAggregator())
                       .description(b.getDescription())
                       .enabled(b.getEnabled())
                       .visible(b.getVisible())
                       .email(b.getEmail())
                       .homePage(b.getHomePage())
                       .url(b.getUrl())
                       .supportedReferences(b.getSupportedReferences())
                       .organization(b.getOrganization())
                       .api(b.getApi())
                       .auth(b.getAuth())
                       .responseParser(b.getResponseParser())
                       .externalUrlParser(b.getExternalUrlParser())
                       .fetcher(b.getFetcher())
                       .requester(b.getRequester())
                       .chromosomeConverter(b.getChromosomeConverter())
                       .positionConverter(b.getPositionConverter())
                       .alleleConverter(b.getAlleleConverter())
                       .referenceConverter(b.getReferenceConverter())
                       .beaconConverter(b.getBeaconConverter())
                       .parents(b.getParents())
                       .children(b.getChildren())
                       .datasets(b.getDatasets())
                       .createdDate(b.getCreatedDate())
                       .build();
    }

    @Override
    public BeaconDto mapDtoToDto(BeaconDto b) {
        return b == null
               ? null
               : BeaconDto.builder()
                          .id(b.getId())
                          .name(b.getName())
                          .aggregator(b.isAggregator())
                          .organization(b.getOrganization())
                          .description(b.getDescription())
                          .enabled(b.isEnabled())
                          .visible(b.isVisible())
                          .email(b.getEmail())
                          .homePage(b.getHomePage())
                          .url(b.getUrl())
                          .createdDate(b.getCreatedDate())
                          .supportedReferences(b.getSupportedReferences())
                          .aggregatedBeacons(b.getAggregatedBeacons())
                          .build();
    }

}
