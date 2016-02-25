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

import com.dnastack.bob.rest.api.BeaconResponseResource;
import com.dnastack.bob.rest.comparator.BeaconResponseDtoComparator;
import com.dnastack.bob.service.api.BeaconResponseService;
import com.dnastack.bob.service.dto.BeaconResponseDto;
import com.dnastack.bob.service.dto.UserDto;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import static com.dnastack.bob.rest.util.IpExtractor.extractIpAddress;
import static com.dnastack.bob.rest.util.ParameterParser.parseMultipleParameterValues;

/**
 * Query rest resource.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Path("/responses")
@RequestScoped
@Named
public class BeaconResponseResourceImpl implements BeaconResponseResource {

    @Context
    private HttpServletRequest request;

    @Inject
    private BeaconResponseService beaconResponseService;

    @Inject
    private BeaconResponseDtoComparator beaconResponseComparator;

    private UserDto user;

    @PostConstruct
    private void init() {
        user = new UserDto(null, extractIpAddress(request));
    }

    @Override
    public BeaconResponseDto queryBeacon(String beaconId, String chrom, Long pos, String allele, String ref) throws ClassNotFoundException {
        return beaconResponseService.queryBeacon(beaconId, chrom, pos, allele, ref, user);
    }

    @Override
    public Collection<BeaconResponseDto> query(String beaconIds, String chrom, Long pos, String allele, String ref) throws ClassNotFoundException {
        Set<BeaconResponseDto> brs = new TreeSet<>(beaconResponseComparator);
        if (beaconIds == null) {
            brs.addAll(beaconResponseService.queryAll(chrom, pos, allele, ref, user));
        } else {
            brs.addAll(beaconResponseService.queryBeacons(parseMultipleParameterValues(beaconIds), chrom, pos, allele, ref, user));
        }

        return brs;
    }
}