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

import com.dnastack.bob.persistence.entity.Beacon;
import com.dnastack.bob.service.dto.BeaconDto;
import com.dnastack.bob.service.mapper.api.BeaconMapper;
import com.dnastack.bob.service.mapper.api.ReferenceMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;
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
        if (b == null) {
            return null;
        }

        BeaconDto.BeaconDtoBuilder builder = BeaconDto.builder().id(b.getId()).name(b.getName()).aggregator(b.getAggregator()).organization(b.getOrganization() == null ? null : b.getOrganization().getName()).description(b.getDescription()).enabled(b.getEnabled()).visible(b.getVisible()).supportedReferences(referenceMapper.mapEntitiesToDtos(b.getSupportedReferences(), showInternal));

        if (showInternal) {
            builder.email(b.getEmail()).homePage(b.getHomePage()).url(b.getUrl());
        }

        return builder.build();
    }

    @Override
    public Beacon mapDtoToEntity(BeaconDto b) {
        return b == null ? null : Beacon.builder().id(b.getId()).name(b.getName()).aggregator(b.isAggregator()).description(b.getDescription()).enabled(b.isEnabled()).visible(b.isVisible()).email(b.getEmail()).homePage(b.getHomePage()).url(b.getUrl()).supportedReferences(referenceMapper.mapDtosToEntities(b.getSupportedReferences())).build();
    }

    @Override
    public Set<BeaconDto> mapEntitiesToDtos(Collection<Beacon> bs, boolean showInternal) {
        return (bs == null) ? null : bs.parallelStream().map((Beacon br) -> mapEntityToDto(br, showInternal)).collect(Collectors.toSet());
    }

    @Override
    public Set<Beacon> mapDtosToEntities(Collection<BeaconDto> bs) {
        return (bs == null) ? null : bs.parallelStream().map((BeaconDto b) -> mapDtoToEntity(b)).collect(Collectors.toSet());
    }

    @Override
    public Beacon mapEntityToEntity(Beacon b) {
        return b == null ? null : Beacon.builder().id(b.getId()).name(b.getName()).aggregator(b.getAggregator()).description(b.getDescription()).enabled(b.getEnabled()).visible(b.getVisible()).email(b.getEmail()).homePage(b.getHomePage()).url(b.getUrl()).supportedReferences(b.getSupportedReferences()).organization(b.getOrganization()).api(b.getApi()).auth(b.getAuth()).responseParser(b.getResponseParser()).externalUrlParser(b.getExternalUrlParser()).fetcher(b.getFetcher()).requester(b.getRequester()).chromosomeConverter(b.getChromosomeConverter()).positionConverter(b.getPositionConverter()).alleleConverter(b.getAlleleConverter()).referenceConverter(b.getReferenceConverter()).beaconConverter(b.getBeaconConverter()).parents(b.getParents()).children(b.getChildren()).datasets(b.getDatasets()).build();
    }

    @Override
    public BeaconDto mapDtoToDto(BeaconDto b) {
        return b == null ? null : BeaconDto.builder().id(b.getId()).name(b.getName()).aggregator(b.isAggregator()).organization(b.getOrganization()).description(b.getDescription()).enabled(b.isEnabled()).visible(b.isVisible()).email(b.getEmail()).homePage(b.getHomePage()).url(b.getUrl()).supportedReferences(b.getSupportedReferences()).build();
    }

}
