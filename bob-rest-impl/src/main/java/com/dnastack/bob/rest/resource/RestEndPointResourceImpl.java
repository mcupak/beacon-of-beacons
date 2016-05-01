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
package com.dnastack.bob.rest.resource;

import com.dnastack.bob.rest.api.RestEndPointResource;
import com.dnastack.bob.rest.base.RestEndPoint;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Information/help rest resource.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Path("/")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@RequestScoped
@Named
public class RestEndPointResourceImpl implements RestEndPointResource {

    private static final String SEPARATOR = "/";
    private static final RestEndPoint BEACONS = new RestEndPoint("beacons", "beacons", "beacons");
    private static final RestEndPoint ORGANIZATIONS = new RestEndPoint("organizations",
                                                                       "organizations",
                                                                       "organizations");
    private static final RestEndPoint RESPONSES = new RestEndPoint("responses",
                                                                   "responses",
                                                                   "responses?chrom=14&pos=106833421&allele=A");
    private static final RestEndPoint ALLELES = new RestEndPoint("alleles", "alleles", "alleles");
    private static final RestEndPoint CHROMOSOMES = new RestEndPoint("chromosomes", "chromosomes", "chromosomes");
    private static final RestEndPoint REFERENCES = new RestEndPoint("references", "references", "references");

    @Context
    private UriInfo uriInfo;

    private String baseUrl;

    @PostConstruct
    private void init() {
        baseUrl = uriInfo.getBaseUri().normalize().toString();
        if (!baseUrl.endsWith(SEPARATOR)) {
            baseUrl += SEPARATOR;
        }
    }

    @GET
    @Override
    public Collection<RestEndPoint> showEndPoints() {
        Set<RestEndPoint> reps = new HashSet<>();
        reps.add(new RestEndPoint(BEACONS.getId(), baseUrl + BEACONS.getBaseUrl(), baseUrl + BEACONS.getExample()));
        reps.add(new RestEndPoint(ORGANIZATIONS.getId(),
                                  baseUrl + ORGANIZATIONS.getBaseUrl(),
                                  baseUrl + ORGANIZATIONS.getExample()));
        reps.add(new RestEndPoint(RESPONSES.getId(),
                                  baseUrl + RESPONSES.getBaseUrl(),
                                  baseUrl + RESPONSES.getExample()));
        reps.add(new RestEndPoint(ALLELES.getId(), baseUrl + ALLELES.getBaseUrl(), baseUrl + ALLELES.getExample()));
        reps.add(new RestEndPoint(CHROMOSOMES.getId(),
                                  baseUrl + CHROMOSOMES.getBaseUrl(),
                                  baseUrl + CHROMOSOMES.getExample()));
        reps.add(new RestEndPoint(REFERENCES.getId(),
                                  baseUrl + REFERENCES.getBaseUrl(),
                                  baseUrl + REFERENCES.getExample()));

        return Collections.unmodifiableSet(reps);
    }

}
