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
