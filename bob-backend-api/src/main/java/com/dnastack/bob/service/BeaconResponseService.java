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
package com.dnastack.bob.service;

import com.dnastack.bob.dto.BeaconResponseTo;
import java.util.Collection;

/**
 * Service for managing beacon responses.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public interface BeaconResponseService {

    /**
     * Query a given beacon
     *
     * @param beaconId beacon to query
     * @param chrom    chromosome
     * @param pos      position
     * @param allele   allele
     * @param ref      reference genome (optional)
     *
     * @return list of beacon responses
     */
    BeaconResponseTo queryBeacon(String beaconId, String chrom, Long pos, String allele, String ref);

    /**
     * Query specified beacons.
     *
     * @param beaconIds collection of beacon IDs
     * @param chrom     chromosome
     * @param pos       position
     * @param allele    allele
     * @param ref       reference genome (optional)
     *
     * @return collection of beacon responses
     */
    Collection<BeaconResponseTo> queryBeacons(Collection<String> beaconIds, String chrom, Long pos, String allele, String ref);

    /**
     * Query all the beacons.
     *
     * @param chrom  chromosome
     * @param pos    position
     * @param allele allele
     * @param ref    reference genome (optional)
     *
     * @return collection of beacon responses
     */
    Collection<BeaconResponseTo> queryAll(String chrom, Long pos, String allele, String ref);

}
