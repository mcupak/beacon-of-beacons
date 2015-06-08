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
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Comparator of OrganizationDto objects. Performs case-insensitive comparison of names of OrganizationDto objects.
 * In case the names are equal, IDs are compared to distinguish similar organizations.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RequestScoped
@Named
@NameComparator
public class OrganizationDtoNameComparator implements OrganizationDtoComparator {

    @Inject
    @IdComparator
    private OrganizationDtoComparator idComparator;

    @Override
    public int compare(OrganizationDto o1, OrganizationDto o2) {
        if (o1 == null || o2 == null) {
            throw new NullPointerException("Organization is null.");
        }
        if (o1.getName() == null || o2.getName() == null) {
            throw new NullPointerException("Organization ID is null.");
        }

        int i = o1.getName().compareToIgnoreCase(o2.getName());
        if (i == 0) {
            i = idComparator.compare(o1, o2);
        }

        return i;
    }

}
