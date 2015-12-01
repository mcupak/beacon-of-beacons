/*
 * The MIT License
 *
 * Copyright 2014 DNAstack.
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
package com.dnastack.bob.rest.comparator;

import com.dnastack.bob.service.dto.OrganizationDto;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import lombok.NonNull;

/**
 * Comparator of OrganizationDto objects. Performs case-insensitive comparison of IDs of OrganizationDto objects.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RequestScoped
@Named
@IdComparator
public class OrganizationDtoIdComparator implements OrganizationDtoComparator {

    @Override
    public int compare(@NonNull OrganizationDto o1, @NonNull OrganizationDto o2) {
        if (o1.getId() == null || o2.getId() == null) {
            throw new NullPointerException("Organization ID is null.");
        }
        return o1.getId().compareToIgnoreCase(o2.getId());
    }

}
