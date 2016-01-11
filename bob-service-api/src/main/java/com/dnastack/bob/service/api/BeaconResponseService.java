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
     * @param beaconId   beacon to query
     * @param chrom      chromosome
     * @param pos        position
     * @param allele     allele
     * @param ref        reference genome (optional)
     * @param onBehalfOf user
     *
     * @return list of beacon responses
     *
     * @throws java.lang.ClassNotFoundException
     */
    BeaconResponseDto queryBeacon(String beaconId, String chrom, Long pos, String allele, String ref, UserDto onBehalfOf) throws ClassNotFoundException;

    /**
     * Queries specified beacons.
     *
     * @param beaconIds  collection of beacon IDs
     * @param chrom      chromosome
     * @param pos        position
     * @param allele     allele
     * @param ref        reference genome (optional)
     * @param onBehalfOf user
     *
     * @return collection of beacon responses
     *
     * @throws java.lang.ClassNotFoundException
     */
    Collection<BeaconResponseDto> queryBeacons(Collection<String> beaconIds, String chrom, Long pos, String allele, String ref, UserDto onBehalfOf) throws ClassNotFoundException;

    /**
     * Queries all the beacons.
     *
     * @param chrom      chromosome
     * @param pos        position
     * @param allele     allele
     * @param ref        reference genome (optional)
     * @param onBehalfOf user
     *
     * @return collection of beacon responses
     *
     * @throws java.lang.ClassNotFoundException
     */
    Collection<BeaconResponseDto> queryAll(String chrom, Long pos, String allele, String ref, UserDto onBehalfOf) throws ClassNotFoundException;

}
