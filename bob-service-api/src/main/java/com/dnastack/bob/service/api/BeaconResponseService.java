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
package com.dnastack.bob.service.api;

import com.dnastack.bob.service.dto.BeaconResponseDto;
import com.dnastack.bob.service.dto.UserDto;

import java.util.Collection;

/**
 * Service for managing beacon responses.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public interface BeaconResponseService {

    /**
     * Queries a given beacon.
     *
     * @param beaconId        beacon to query
     * @param chrom           chromosome
     * @param pos             position
     * @param referenceAllele reference allele
     * @param allele          allele
     * @param ref             reference genome (optional)
     * @param onBehalfOf      user
     * @param ip              ip
     * @return list of beacon responses
     * @throws java.lang.ClassNotFoundException
     */
    BeaconResponseDto queryBeacon(String beaconId, String chrom, Long pos, String referenceAllele, String allele, String ref, UserDto onBehalfOf, String ip) throws ClassNotFoundException;

    /**
     * Queries specified beacons.
     *
     * @param beaconIds       collection of beacon IDs
     * @param chrom           chromosome
     * @param pos             position
     * @param referenceAllele reference allele
     * @param allele          allele
     * @param ref             reference genome (optional)
     * @param onBehalfOf      user
     * @param ip              ip
     * @return collection of beacon responses
     * @throws java.lang.ClassNotFoundException
     */
    Collection<BeaconResponseDto> queryBeacons(Collection<String> beaconIds, String chrom, Long pos, String referenceAllele, String allele, String ref, UserDto onBehalfOf, String ip) throws ClassNotFoundException;

    /**
     * Queries all the beacons.
     *
     * @param chrom           chromosome
     * @param pos             position
     * @param referenceAllele reference allele
     * @param allele          allele
     * @param ref             reference genome (optional)
     * @param onBehalfOf      user
     * @param ip              ip
     * @return collection of beacon responses
     * @throws java.lang.ClassNotFoundException
     */
    Collection<BeaconResponseDto> queryAll(String chrom, Long pos, String referenceAllele, String allele, String ref, UserDto onBehalfOf, String ip) throws ClassNotFoundException;

}
