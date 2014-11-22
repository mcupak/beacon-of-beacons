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
package com.dnastack.beacon.service;

import com.dnastack.beacon.entity.Beacon;
import com.dnastack.beacon.entity.BeaconResponse;
import com.dnastack.beacon.util.QueryUtils;

/**
 * Sample implementation of a beacon service.
 * 
 * TODO: Replace this class with your actual beacon.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class SampleBeaconService implements BeaconService {

    private static final Beacon beacon = new Beacon("foo", "bar", "org", "sample beacon");

    @Override
    public BeaconResponse query(String chrom, Long pos, String allele, String ref) {
        BeaconResponse response = new BeaconResponse(beacon, QueryUtils.getQuery(chrom, pos, allele, ref), null);
        
        // generate a sample response
        if (pos % 2 == 0) {
            response.setResponse(true);
        } else {
            response.setResponse(false);
        }

        return response;
    }

    @Override
    public Beacon info() {
        return beacon;
    }

}
