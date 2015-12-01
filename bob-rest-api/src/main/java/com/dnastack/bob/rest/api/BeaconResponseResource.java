/*
 * The MIT License
 *
 * Copyright 2015 DNAstack.
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
package com.dnastack.bob.rest.api;

import com.dnastack.bob.service.dto.BeaconResponseDto;
import java.util.Collection;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public interface BeaconResponseResource {

    /**
     * Query all the beacons or a specific beacon as determined by a param.
     *
     * @param beaconIds beacon to query (optional)
     * @param chrom     chromosome
     * @param pos       position
     * @param allele    allele
     * @param ref       reference genome (optional)
     *
     * @return list of beacon responses
     *
     * @throws java.lang.ClassNotFoundException
     */
    @GET
    Collection<BeaconResponseDto> query(@QueryParam(value = "beacon") String beaconIds, @QueryParam(value = "chrom") String chrom, @QueryParam(value = "pos") Long pos, @QueryParam(value = "allele") String allele, @QueryParam(value = "ref") String ref) throws ClassNotFoundException;

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
     *
     * @throws java.lang.ClassNotFoundException
     */
    @GET
    @Path(value = "/{beaconId}")
    BeaconResponseDto queryBeacon(@PathParam(value = "beaconId") String beaconId, @QueryParam(value = "chrom") String chrom, @QueryParam(value = "pos") Long pos, @QueryParam(value = "allele") String allele, @QueryParam(value = "ref") String ref) throws ClassNotFoundException;

}
