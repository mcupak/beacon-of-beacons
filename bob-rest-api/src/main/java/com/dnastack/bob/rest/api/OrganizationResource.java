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

import com.dnastack.bob.service.dto.OrganizationDto;
import java.util.Collection;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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

/**
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public interface OrganizationResource {

    @POST
    Response create(@Context UriInfo uriInfo, OrganizationDto organization);

    @DELETE
    @Path(value = "/{organizationId}")
    Response delete(@Context UriInfo uriInfo, @PathParam(value = "organizationId") String organizationId);

    /**
     * Shows all the organizations or a specific organization as determined by a param.
     *
     * @param organizationIds organization ID
     *
     * @return set of organizations
     */
    @GET
    Collection<OrganizationDto> show(@QueryParam(value = "organization") String organizationIds);

    /**
     * Shows organization details.
     *
     * @param organizationId id of the organization
     *
     * @return organization
     */
    @GET
    @Path(value = "/{organizationId}")
    OrganizationDto showOrganization(@PathParam(value = "organizationId") String organizationId);

    @PUT
    @Path(value = "/{organizationId}")
    Response update(@Context UriInfo uriInfo, @PathParam(value = "organizationId") String organizationId, OrganizationDto organization);

}
