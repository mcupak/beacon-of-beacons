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
package com.dnastack.bob.service.requester.impl;

import com.dnastack.bob.service.requester.api.RequestConstructor;

import javax.inject.Named;
import java.io.Serializable;
import java.util.Map;

/**
 * Request constructor using URL with chrom, pos and beacon ID params. Useful for beacons which don't support filtering
 * by alleles.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Named
public class BeaconRefChromPosRequestConstructor implements RequestConstructor, Serializable {

    private static final long serialVersionUID = -3217571143261453662L;

    @Override
    public String getUrl(String template, String beacon, String ref, String chrom, Long pos, String allele, String dataset) {
        return String.format(template, beacon, ref, chrom, pos);
    }

    @Override
    public Map<String, String> getPayload(String template, String beacon, String ref, String chrom, Long pos, String allele, String dataset) {
        return null;
    }

}
