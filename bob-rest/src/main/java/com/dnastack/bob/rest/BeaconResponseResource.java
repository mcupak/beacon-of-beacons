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
package com.dnastack.bob.rest;

import com.dnastack.bob.dto.BeaconResponseTo;
import com.dnastack.bob.rest.util.BeaconResponseToComparator;
import com.dnastack.bob.service.BeaconResponseServiceImpl;
import com.dnastack.bob.util.ParsingUtils;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
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
@RequestScoped
@Named
public class BeaconResponseResource {

    @Inject
    private BeaconResponseServiceImpl beaconResponseService;

    @Inject
    private BeaconResponseToComparator beaconResponseComparator;

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
    public BeaconResponseTo queryBeacon(@PathParam("beaconId") String beaconId, @QueryParam("chrom") String chrom, @QueryParam("pos") Long pos, @QueryParam("allele") String allele, @QueryParam("ref") String ref) {
        return beaconResponseService.queryBeacon(beaconId, chrom, pos, allele, ref);
    }

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
     */
    @GET
    public Collection<BeaconResponseTo> query(@QueryParam("beacon") String beaconIds, @QueryParam("chrom") String chrom, @QueryParam("pos") Long pos, @QueryParam("allele") String allele, @QueryParam("ref") String ref) {
        Set<BeaconResponseTo> brs = new TreeSet<>(beaconResponseComparator);
        if (beaconIds == null) {
            brs.addAll(beaconResponseService.queryAll(chrom, pos, allele, ref));
        } else {
            brs.addAll(beaconResponseService.queryBeacons(ParsingUtils.parseMultipleParameterValues(beaconIds), chrom, pos, allele, ref));
        }

        return brs;
    }
}
