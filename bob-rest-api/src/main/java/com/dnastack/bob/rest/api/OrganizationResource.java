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

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;

/**
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public interface OrganizationResource {

    /**
     * Creates an organization.
     *
     * @param uriInfo      URI info
     * @param organization organization
     * @return URI of the organization created
     */
    Response create(UriInfo uriInfo, OrganizationDto organization);

    /**
     * Removes an organization.
     *
     * @param uriInfo        URI info
     * @param organizationId ID of the organization
     * @return
     */
    Response delete(UriInfo uriInfo, String organizationId);

    /**
     * Shows all the organizations or a specific organization as determined by a param.
     *
     * @param organizationIds organization ID
     * @return set of organizations
     */
    Collection<OrganizationDto> show(String organizationIds);

    /**
     * Shows organization details.
     *
     * @param organizationId id of the organization
     * @return organization
     */
    OrganizationDto showOrganization(String organizationId);

    /**
     * Updates an organization.
     *
     * @param uriInfo        URI info
     * @param organizationId ID of the organization
     * @param organization   organization
     * @return updated organization
     */
    Response update(UriInfo uriInfo, String organizationId, OrganizationDto organization);

}
