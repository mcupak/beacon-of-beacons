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

import com.dnastack.bob.rest.comparator.BeaconDtoComparator;
import com.dnastack.bob.rest.comparator.NameComparator;
import com.dnastack.bob.service.api.BeaconService;
import com.dnastack.bob.service.dto.BeaconDto;
import java.net.URI;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static com.dnastack.bob.rest.util.ParameterParser.parseMultipleParameterValues;

/**
 * Query rest resource.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Path("/beacons")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@RequestScoped
@Named
public class BeaconResource {

    @Inject
    private BeaconService beaconService;

    @Inject
    @NameComparator
    private BeaconDtoComparator beaconComparator;

    /**
     * Shows beacon details.
     *
     * @param beaconId id of the beacon
     *
     * @return beacon
     */
    @GET
    @Path("/{beaconId}")
    public BeaconDto showBeacon(@PathParam("beaconId") String beaconId) {
        BeaconDto b = beaconService.findVisible(beaconId);
        if (b == null) {
            throw new NotFoundException("Cannot find beacon with ID: " + beaconId);
        }
        return b;
    }

    /**
     * Shows all the beacons or a specific beacon as determined by a param.
     *
     * @param beaconIds beacon ID
     *
     * @return set of beacons
     */
    @GET
    public Collection<BeaconDto> show(@QueryParam("beacon") String beaconIds) {
        Set<BeaconDto> bs = new TreeSet<>(beaconComparator);
        if (beaconIds == null) {
            bs.addAll(beaconService.findVisible());
        } else {
            bs.addAll(beaconService.findVisible(parseMultipleParameterValues(beaconIds)));
        }

        return bs;
    }

    @POST
    public Response create(@Context UriInfo uriInfo, BeaconDto beacon) {
        BeaconDto o = beaconService.create(beacon);
        return Response.created(URI.create(uriInfo.getAbsolutePath() + "/" + o.getId())).build();
    }

    @PUT
    @Path("/{beaconId}")
    public Response update(@Context UriInfo uriInfo, @PathParam("beaconId") String beaconId, BeaconDto beacon) {
        BeaconDto o = beaconService.update(beaconId, beacon);
        return Response.ok(o).build();
    }

    @DELETE
    @Path("/{beaconId}")
    public Response delete(@Context UriInfo uriInfo, @PathParam("beaconId") String beaconId) {
        beaconService.delete(beaconId);
        return Response.noContent().build();
    }
}
