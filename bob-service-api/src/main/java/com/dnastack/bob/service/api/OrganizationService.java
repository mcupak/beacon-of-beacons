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
package com.dnastack.bob.service.api;

import com.dnastack.bob.service.dto.OrganizationDto;

import java.util.Collection;

/**
 * Service managing organizations.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public interface OrganizationService {

    /**
     * Retrieves organization details, provided it has visible beacons.
     *
     * @param id id of the organization
     * @return organization
     */
    OrganizationDto find(String id);

    /**
     * Creates a new organization.
     *
     * @param organization
     * @return
     */
    OrganizationDto create(OrganizationDto organization);

    /**
     * Updates an organization.
     *
     * @param id           ID of the organization to update
     * @param organization template
     * @return
     */
    OrganizationDto update(String id, OrganizationDto organization);

    /**
     * Removes organization.
     *
     * @param id ID of the organization to delete
     */
    void delete(String id);

    /**
     * Retrieves organizations with specified IDs and visible beacons.
     *
     * @param ids collection of organization ids
     * @return collection of organizations
     */
    Collection<OrganizationDto> find(Collection<String> ids);

    /**
     * Retrieves all the organizations with visible beacons.
     *
     * @return collection of organizations
     */
    Collection<OrganizationDto> find();

}
