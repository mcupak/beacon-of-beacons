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
package com.dnastack.bob.processor;

import com.dnastack.bob.entity.Beacon;
import com.dnastack.bob.entity.Query;
import com.dnastack.bob.entity.Reference;
import com.dnastack.bob.util.HttpUtils;
import com.dnastack.bob.util.ParsingUtils;
import com.dnastack.bob.util.QueryUtils;
import com.google.common.collect.ImmutableSet;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * AMPLab beacon service.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Processor
@AmpLab
public class AmpLabBeaconProcessor extends AbstractBeaconProcessor {

    private static final long serialVersionUID = 10L;
    private static final String BASE_URL = "http://beacon.eecs.berkeley.edu/beacon.php";
    private static final String CHROM_TEMPLATE = "chr%s";
    private static final Set<Reference> SUPPORTED_REFS = ImmutableSet.of(Reference.HG18, Reference.HG19, Reference.HG38);

    private List<NameValuePair> getQueryData(String ref, String chrom, Long pos, String allele) {
        List<NameValuePair> nvs = new ArrayList<>();
        // so far there is only 1 population
        nvs.add(new BasicNameValuePair("population", "1000genomes"));
        nvs.add(new BasicNameValuePair("genome", ref));
        nvs.add(new BasicNameValuePair("chr", chrom));
        nvs.add(new BasicNameValuePair("coord", pos.toString()));
        nvs.add(new BasicNameValuePair("allele", allele));

        return nvs;
    }

    @Override
    @Asynchronous
    public Future<String> getQueryResponse(Beacon beacon, Query query) {
        String res = null;
        try {
            res = HttpUtils.executeRequest(HttpUtils.createRequest(BASE_URL, true, getQueryData(query.getReference().toString(), QueryUtils.denormalizeChromosome(CHROM_TEMPLATE, query.getChromosome()), QueryUtils.denormalizePosition(query.getPosition()), QueryUtils.denormalizeAllele(query.getAllele()))));
        } catch (UnsupportedEncodingException ex) {
            // ignore, already null
        }

        return new AsyncResult<>(res);
    }

    @Override
    @Asynchronous
    public Future<Boolean> parseQueryResponse(Beacon b, String response) {
        Boolean res = ParsingUtils.parseContainsStringCaseInsensitive(response, "beacon found", "beacon cannot find");

        return new AsyncResult<>(res);
    }

    @Override
    public Set<Reference> getSupportedReferences() {
        return SUPPORTED_REFS;
    }
}
