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

import com.dnastack.bob.rest.util.RestEndPoint;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 * Information/help rest resource.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Path("/")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
@RequestScoped
@Named
public class RestEndPointResource {

    private static final RestEndPoint beacons = new RestEndPoint("beacons", "beacons", "beacons");
    private static final RestEndPoint responses = new RestEndPoint("responses", "responses", "responses?chrom=14&pos=106833421&allele=A");
    private static final RestEndPoint alleles = new RestEndPoint("alleles", "alleles", "alleles");
    private static final RestEndPoint chromosomes = new RestEndPoint("chromosomes", "chromosomes", "chromosomes");
    private static final RestEndPoint references = new RestEndPoint("references", "references", "references");

    @Context
    private UriInfo uriInfo;

    private String baseUrl;

    @PostConstruct
    private void init() {
        baseUrl = uriInfo.getBaseUri().toString();
    }

    /**
     * Shows REST welcome page.
     *
     * @return response
     */
    @GET
    public Collection<RestEndPoint> showEndPoints() {
        Set<RestEndPoint> reps = new HashSet<>();
        reps.add(new RestEndPoint(beacons.getId(), baseUrl + beacons.getBaseUrl(), baseUrl + beacons.getExample()));
        reps.add(new RestEndPoint(responses.getId(), baseUrl + responses.getBaseUrl(), baseUrl + responses.getExample()));
        reps.add(new RestEndPoint(alleles.getId(), baseUrl + alleles.getBaseUrl(), baseUrl + alleles.getExample()));
        reps.add(new RestEndPoint(chromosomes.getId(), baseUrl + chromosomes.getBaseUrl(), baseUrl + chromosomes.getExample()));
        reps.add(new RestEndPoint(references.getId(), baseUrl + references.getBaseUrl(), baseUrl + references.getExample()));

        return Collections.unmodifiableSet(reps);
    }

}
