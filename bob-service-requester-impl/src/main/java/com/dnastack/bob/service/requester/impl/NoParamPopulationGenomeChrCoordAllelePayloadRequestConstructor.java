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
import java.util.HashMap;
import java.util.Map;

/**
 * Request constructor using base beacon URL with custom payload based on population, genome, chromosome, coordinates
 * and alleles.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Named
public class NoParamPopulationGenomeChrCoordAllelePayloadRequestConstructor implements RequestConstructor, Serializable {

    private static final long serialVersionUID = -1948686874610313749L;

    @Override
    public String getUrl(String template, String beacon, String ref, String chrom, Long pos, String allele, String dataset) {
        return template;
    }

    @Override
    public Map<String, String> getPayload(String template, String beacon, String ref, String chrom, Long pos, String allele, String dataset) {
        Map<String, String> res = new HashMap<>();
        res.put("population", "1000genomes");
        res.put("genome", ref);
        res.put("chr", chrom);
        res.put("coord", pos.toString());
        res.put("allele", allele);

        return res;
    }

}
