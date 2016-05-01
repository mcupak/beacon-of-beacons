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

import com.dnastack.bob.persistence.entity.BeaconResponse;
import com.dnastack.bob.service.dto.BeaconResponseDto;
import com.dnastack.bob.service.mapper.api.BeaconMapper;
import com.dnastack.bob.service.mapper.api.BeaconResponseMapper;
import com.dnastack.bob.service.mapper.api.QueryMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Default implementation of a mapper of beacon responses to their DTOs.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@ApplicationScoped
public class BeaconResponseMapperImpl implements BeaconResponseMapper {

    private static final long serialVersionUID = -3787447748973760122L;

    @Inject
    private BeaconMapper beaconMapper;

    @Inject
    private QueryMapper queryMapper;

    @Override
    public BeaconResponseDto mapEntityToDto(BeaconResponse br, boolean showInternal) {
        return (br == null)
               ? null
               : BeaconResponseDto.builder()
                                  .beacon(beaconMapper.mapEntityToDto(br.getBeacon(), showInternal))
                                  .query(queryMapper.mapEntityToDto(br.getQuery(), showInternal))
                                  .response(br.getResponse())
                                  .externalUrl(br.getExternalUrl())
                                  .info(br.getInfo())
                                  .build();
    }

    @Override
    public BeaconResponse mapDtoToEntity(BeaconResponseDto br) {
        return (br == null)
               ? null
               : BeaconResponse.builder()
                               .beacon(beaconMapper.mapDtoToEntity(br.getBeacon()))
                               .query(queryMapper.mapDtoToEntity(br.getQuery()))
                               .response(br.getResponse())
                               .externalUrl(br.getExternalUrl())
                               .frequency(br.getFrequency())
                               .info(br.getInfo())
                               .build();
    }

    @Override
    public BeaconResponse mapEntityToEntity(BeaconResponse br) {
        return (br == null)
               ? null
               : BeaconResponse.builder()
                               .beacon(br.getBeacon())
                               .query(br.getQuery())
                               .response(br.getResponse())
                               .externalUrl(br.getExternalUrl())
                               .frequency(br.getFrequency())
                               .info(br.getInfo())
                               .build();
    }

    @Override
    public BeaconResponseDto mapDtoToDto(BeaconResponseDto br) {
        return (br == null)
               ? null
               : BeaconResponseDto.builder()
                                  .beacon(br.getBeacon())
                                  .query(br.getQuery())
                                  .response(br.getResponse())
                                  .externalUrl(br.getExternalUrl())
                                  .frequency(br.getFrequency())
                                  .info(br.getInfo())
                                  .build();
    }

    @Override
    public Set<BeaconResponseDto> mapEntitiesToDtos(Collection<BeaconResponse> brs, boolean showInternal) {
        return (brs == null)
               ? null
               : brs.parallelStream()
                    .map((BeaconResponse br) -> mapEntityToDto(br, showInternal))
                    .collect(Collectors.toSet());
    }

    @Override
    public Set<BeaconResponse> mapDtosToEntities(Collection<BeaconResponseDto> brs) {
        return (brs == null)
               ? null
               : brs.parallelStream().map((BeaconResponseDto br) -> mapDtoToEntity(br)).collect(Collectors.toSet());
    }
}
