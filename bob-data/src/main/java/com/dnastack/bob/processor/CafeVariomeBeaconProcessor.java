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
import java.net.MalformedURLException;
import java.util.Set;
import java.util.concurrent.Future;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;

/**
 * Cafe Variome beacon service.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Processor
@CafeVariome
public class CafeVariomeBeaconProcessor extends AbstractBeaconProcessor {

    private static final long serialVersionUID = 12L;
    private static final String BASE_URL = "http://beacon.cafevariome.org/query";
    private static final String PARAM_TEMPLATE = "?chrom=%s&pos=%d&allele=%s";
    private static final Set<Reference> SUPPORTED_REFS = ImmutableSet.of(Reference.HG19);
    private static final String BEACON_PREFIX = "cafe-";
    private static final String RESPONSE_FIELD = "response";

    private String getQueryUrl(String chrom, Long pos, String allele) throws MalformedURLException {
        String params = String.format(PARAM_TEMPLATE, chrom, pos, allele);

        return BASE_URL + params;
    }

    private String getJsonFieldName(Beacon b) {
        String res = null;
        if (b.getId().startsWith(BEACON_PREFIX)) {
            res = b.getId().substring(BEACON_PREFIX.length()) + "_" + RESPONSE_FIELD;
        } else {
            res = b.getId() + "_" + RESPONSE_FIELD;
        }
        return res;
    }

    @Override
    @Asynchronous
    public Future<String> getQueryResponse(Beacon beacon, Query query) {
        String res = null;

        try {
            res = HttpUtils.executeRequest(HttpUtils.createRequest(getQueryUrl(QueryUtils.denormalizeChromosome("chr%s", query.getChromosome()).toLowerCase(), query.getPosition(), query.getAllele()), false, null));
        } catch (MalformedURLException | UnsupportedEncodingException ex) {
            // ignore, already null
        }

        return new AsyncResult<>(res);
    }

    @Override
    @Asynchronous
    public Future<Boolean> parseQueryResponse(Beacon b, String response) {
        Boolean res = ParsingUtils.parseBooleanFromJson(response, RESPONSE_FIELD, getJsonFieldName(b));

        return new AsyncResult<>(res);
    }

    @Override
    public Set<Reference> getSupportedReferences() {
        return SUPPORTED_REFS;
    }
}
