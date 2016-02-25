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

import com.dnastack.bob.rest.api.OrganizationResource;
import com.dnastack.bob.rest.comparator.NameComparator;
import com.dnastack.bob.rest.comparator.OrganizationDtoComparator;
import com.dnastack.bob.service.api.OrganizationService;
import com.dnastack.bob.service.dto.OrganizationDto;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import static com.dnastack.bob.rest.util.ParameterParser.parseMultipleParameterValues;

/**
 * Organization rest resource.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Path("/organizations")
@RequestScoped
@Named
public class OrganizationResourceImpl implements OrganizationResource {

    @Inject
    private OrganizationService organizationService;

    @Inject
    @NameComparator
    private OrganizationDtoComparator organizationComparator;

    @Override
    public OrganizationDto showOrganization(String organizationId) {
        OrganizationDto b = organizationService.findWithVisibleBeacons(organizationId);
        if (b == null) {
            throw new NotFoundException("Cannot find organization with ID: " + organizationId);
        }
        return b;
    }

    @Override
    public Collection<OrganizationDto> show(String organizationIds) {
        Set<OrganizationDto> bs = new TreeSet<>(organizationComparator);
        if (organizationIds == null) {
            bs.addAll(organizationService.findWithVisibleBeacons());
        } else {
            bs.addAll(organizationService.findWithVisibleBeacons(parseMultipleParameterValues(organizationIds)));
        }

        return bs;
    }

    @Override
    public Response create(UriInfo uriInfo, OrganizationDto organization) {
        OrganizationDto o = organizationService.create(organization);
        return Response.created(URI.create(uriInfo.getAbsolutePath() + "/" + o.getId())).build();
    }

    @Override
    public Response update(UriInfo uriInfo, String organizationId, OrganizationDto organization) {
        OrganizationDto o = organizationService.update(organizationId, organization);
        return Response.ok(o).build();
    }

    @Override
    public Response delete(UriInfo uriInfo, String organizationId) {
        organizationService.delete(organizationId);
        return Response.ok().build();
    }
}
