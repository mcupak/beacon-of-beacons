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
package com.dnastack.beacon.rest;

import com.dnastack.beacon.service.BeaconResponse;
import com.dnastack.beacon.service.BeaconResponseService;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Query rest resource.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Path("/responses")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
public class ResponseResource {

    @Inject
    private BeaconResponseService responseService;

    @Inject
    private BeaconResponseComparator beaconResponseComparator;

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
    @GET
    @Path("/{beaconId}")
    public BeaconResponse queryBeacon(@PathParam("beaconId") String beaconId, @QueryParam("chrom") String chrom, @QueryParam("pos") Long pos, @QueryParam("allele") String allele, @QueryParam("ref") String ref) {
        return responseService.queryBeacon(beaconId, chrom, pos, allele, ref);
    }

    /**
     * Query all the beacons or a specific beacon as determined by a param.
     *
     * @param beaconId beacon to query (optional)
     * @param chrom    chromosome
     * @param pos      position
     * @param allele   allele
     * @param ref      reference genome (optional)
     *
     * @return list of beacon responses
     */
    @GET
    public Collection<BeaconResponse> query(@QueryParam("beacon") String beaconId, @QueryParam("chrom") String chrom, @QueryParam("pos") Long pos, @QueryParam("allele") String allele, @QueryParam("ref") String ref) {
        Set<BeaconResponse> brs = new TreeSet<>(beaconResponseComparator);
        if (beaconId == null) {
            brs.addAll(responseService.queryAll(chrom, pos, allele, ref));
        } else {
            brs.add(responseService.queryBeacon(beaconId, chrom, pos, allele, ref));
        }

        return brs;
    }
}
