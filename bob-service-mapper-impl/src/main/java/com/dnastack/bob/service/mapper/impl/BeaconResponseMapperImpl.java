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
