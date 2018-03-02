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

import com.dnastack.bob.rest.api.BeaconResponseResource;
import com.dnastack.bob.rest.base.Error;
import com.dnastack.bob.rest.comparator.BeaconResponseDtoComparator;
import com.dnastack.bob.service.api.BeaconResponseService;
import com.dnastack.bob.service.dto.BeaconResponseDto;
import com.dnastack.bob.service.dto.UserDto;
import io.swagger.annotations.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import static com.dnastack.bob.rest.util.IpExtractor.extractIpAddress;
import static com.dnastack.bob.rest.util.ParameterParser.parseMultipleParameterValues;

/**
 * Query rest resource.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Path("/responses")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@RequestScoped
@Named
@Api(value = "Responses")
public class BeaconResponseResourceImpl implements BeaconResponseResource {

    @Context
    private HttpServletRequest request;

    @Inject
    private BeaconResponseService beaconResponseService;

    @Inject
    private BeaconResponseDtoComparator beaconResponseComparator;

    private UserDto user;

    private String ip;

    @PostConstruct
    private void init() {
        user = null;
        ip = extractIpAddress(request);
    }

    @GET
    @Path(value = "/{beaconId}")
    @ApiOperation(value = "Query beacon", notes = "Executes a query for a specific allele against a specific beacon.", response = BeaconResponseDto.class)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "bad request", response = Error.class), @ApiResponse(code = 404, message = "not found", response = Error.class), @ApiResponse(code = 500, message = "internal" + " server error", response = Error.class)})
    @Override
    public BeaconResponseDto queryBeacon(@ApiParam(value = "ID of the beacon.") @PathParam(value = "beaconId") String beaconId, @ApiParam(value = "Chromosome ID. Accepted values: 1-22, X, Y, MT. Note: For compatibility with conventions set by some of the existing beacons, an arbitrary prefix is accepted as well (e.g. chr1 is equivalent to chrom1 and 1).") @QueryParam(value = "chrom") String chrom, @ApiParam(value = "Coordinate within a chromosome (0-based).") @QueryParam(value = "pos") Long pos, @ApiParam(value = "Any string of nucleotides A,C,T,G.") @QueryParam(value = "referenceAllele") String referenceAllele, @ApiParam(value = "Any string of nucleotides A,C,T,G or D, I for deletion and insertion, respectively. Note: For compatibility with conventions set by some of the existing beacons, DEL and INS identifiers are also accepted.") @QueryParam(value = "allele") String allele, @ApiParam(value = "Genome/assembly ID. If not specified, all the genomes supported by the given beacons are queried. Note: For compatibility with conventions set by some of the existing beacons, both GRC or HG notation are accepted, case insensitive. Optional parameter.") @QueryParam(value = "ref") String ref) throws ClassNotFoundException {
        return beaconResponseService.queryBeacon(beaconId, chrom, pos, referenceAllele, allele, ref, user, ip);
    }

    @GET
    @ApiOperation(value = "Query beacons", notes = "Executes a query for a specific allele against beacons.", response = BeaconResponseDto.class, responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "bad request", response = Error.class), @ApiResponse(code = 404, message = "not found", response = Error.class), @ApiResponse(code = 500, message = "internal" + " server error", response = Error.class)})
    @Override
    public Collection<BeaconResponseDto> query(@ApiParam(value = "ID of the beacon.") @QueryParam(value = "beacon") String beaconIds, @ApiParam(value = "Chromosome ID. Accepted values: 1-22, X, Y, MT. Note: For compatibility with conventions set by some of the existing beacons, an arbitrary prefix is accepted as well (e.g. chr1 is equivalent to chrom1 and 1).") @QueryParam(value = "chrom") String chrom, @ApiParam(value = "Coordinate within a chromosome (0-based).") @QueryParam(value = "pos") Long pos, @ApiParam(value = "Any string of nucleotides A,C,T,G.") @QueryParam(value = "referenceAllele") String referenceAllele, @ApiParam(value = "Any string of nucleotides A,C,T,G or D, I for deletion and insertion, respectively. Note: For compatibility with conventions set by some of the existing beacons, DEL and INS identifiers are also accepted.") @QueryParam(value = "allele") String allele, @ApiParam(value = "Genome/assembly ID. If not specified, all the genomes supported by the given beacons are queried. Note: For compatibility with conventions set by some of the existing beacons, both GRC or HG notation are accepted, case insensitive. Optional parameter.") @QueryParam(value = "ref") String ref) throws ClassNotFoundException {
        Set<BeaconResponseDto> brs = new TreeSet<>(beaconResponseComparator);
        if (beaconIds == null) {
            brs.addAll(beaconResponseService.queryAll(chrom, pos, referenceAllele, allele, ref, user, ip));
        } else {
            brs.addAll(beaconResponseService.queryBeacons(parseMultipleParameterValues(beaconIds),
                                                          chrom,
                                                          pos,
                                                          referenceAllele,
                                                          allele,
                                                          ref,
                                                          user,
                                                          ip));
        }

        return brs;
    }
}
