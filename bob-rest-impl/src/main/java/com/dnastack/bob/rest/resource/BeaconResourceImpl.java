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
package com.dnastack.bob.rest.resource;

import com.dnastack.bob.rest.api.BeaconResource;
import com.dnastack.bob.rest.base.Error;
import com.dnastack.bob.rest.comparator.BeaconDtoComparator;
import com.dnastack.bob.rest.comparator.NameComparator;
import com.dnastack.bob.service.api.BeaconService;
import com.dnastack.bob.service.dto.BeaconDto;
import io.swagger.annotations.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

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
@Api(value = "Beacons")
public class BeaconResourceImpl implements BeaconResource {

    @Inject
    private BeaconService beaconService;

    @Inject
    @NameComparator
    private BeaconDtoComparator beaconComparator;

    @GET
    @Path(value = "/{beaconId}")
    @ApiOperation(value = "Find beacon by ID", notes = "Finds beacon with the given ID.", response = BeaconDto.class)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "bad request", response = Error.class), @ApiResponse(code = 404, message = "not found", response = Error.class), @ApiResponse(code = 500, message = "internal" + " server error", response = Error.class)})
    @Override
    public BeaconDto showBeacon(@ApiParam(value = "ID of the beacon.") @PathParam(value = "beaconId") String beaconId) {
        BeaconDto b = beaconService.find(beaconId);
        if (b == null) {
            throw new NotFoundException("Cannot find beacon with ID: " + beaconId);
        }
        return b;
    }

    @GET
    @ApiOperation(value = "List beacons", notes = "Lists beacons.", response = BeaconDto.class, responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "bad request", response = Error.class), @ApiResponse(code = 404, message = "not found", response = Error.class), @ApiResponse(code = 500, message = "internal" + " server error", response = Error.class)})
    @Override
    public Collection<BeaconDto> show(@ApiParam(value = "Filter containing a single beacon ID or a list of comma-separated IDs enclosed in [].") @QueryParam(value = "beacon") String beaconIds) {
        Set<BeaconDto> bs = new TreeSet<>(beaconComparator);
        if (beaconIds == null) {
            bs.addAll(beaconService.find());
        } else {
            bs.addAll(beaconService.find(parseMultipleParameterValues(beaconIds)));
        }

        return bs;
    }

    @POST
    @Override
    public Response create(@Context UriInfo uriInfo, BeaconDto beacon) {
        BeaconDto o = beaconService.create(beacon);
        return Response.created(URI.create(uriInfo.getAbsolutePath() + "/" + o.getId())).build();
    }

    @PUT
    @Path(value = "/{beaconId}")
    @Override
    public Response update(@Context UriInfo uriInfo, @PathParam(value = "beaconId") String beaconId, BeaconDto beacon) {
        BeaconDto o = beaconService.update(beaconId, beacon);
        return Response.ok(o).build();
    }

    @DELETE
    @Path(value = "/{beaconId}")
    @Override
    public Response delete(@Context UriInfo uriInfo, @PathParam(value = "beaconId") String beaconId) {
        beaconService.delete(beaconId);
        return Response.noContent().build();
    }
}
