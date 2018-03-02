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
package com.dnastack.bob.rest.api;

import com.dnastack.bob.service.dto.BeaconResponseDto;

import java.util.Collection;

/**
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public interface BeaconResponseResource {

    /**
     * Query all the beacons or a specific beacon as determined by a param.
     *
     * @param beaconIds beacon to query (optional)
     * @param chrom     chromosome
     * @param pos       position
     * @param referenceAllele    reference allele
     * @param allele    allele
     * @param ref       reference genome (optional)
     * @return list of beacon responses
     * @throws java.lang.ClassNotFoundException
     */
    Collection<BeaconResponseDto> query(String beaconIds, String chrom, Long pos, String referenceAllele, String allele, String ref) throws ClassNotFoundException;

    /**
     * Query a given beacon
     *
     * @param beaconId beacon to query
     * @param chrom    chromosome
     * @param pos      position
     * @param referenceAllele    reference allele
     * @param allele   allele
     * @param ref      reference genome (optional)
     * @return list of beacon responses
     * @throws java.lang.ClassNotFoundException
     */
    BeaconResponseDto queryBeacon(String beaconId, String chrom, Long pos, String referenceAllele, String allele, String ref) throws ClassNotFoundException;

}
