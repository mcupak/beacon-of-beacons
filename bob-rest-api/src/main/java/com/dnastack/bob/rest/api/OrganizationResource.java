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
