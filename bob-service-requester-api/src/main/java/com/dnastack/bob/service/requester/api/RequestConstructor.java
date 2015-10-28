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
package com.dnastack.bob.service.requester.api;

import java.util.Map;

/**
 * Query URL generator.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public interface RequestConstructor {

    /**
     * Generates query URL for a given beacon.
     *
     * @param template beacon url template
     * @param beacon   beacon
     * @param ref      reference genome
     * @param chrom    chromosome
     * @param pos      position
     * @param allele   allele
     * @param dataset  dataset
     *
     * @return URL
     */
    String getUrl(String template, String beacon, String ref, String chrom, Long pos, String allele, String dataset);

    /**
     * Generates query request payload.
     *
     * @param template beacon url template
     * @param beacon   beacon
     * @param ref      reference genome
     * @param chrom    chromosome
     * @param pos      position
     * @param allele   allele
     * @param dataset  dataset
     *
     * @return payload key-value pairs
     */
    Map<String, String> getPayload(String template, String beacon, String ref, String chrom, Long pos, String allele, String dataset);

}
