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
 * Kaviar beacon service.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Processor
@Kaviar
public class KaviarBeaconProcessor extends AbstractBeaconProcessor {

    private static final long serialVersionUID = 30L;
    private static final String BASE_URL = "http://db.systemsbiology.net/kaviar/cgi-pub/beacon";
    private static final String PARAM_TEMPLATE = "?onebased=0&frz=%s&chr=%s&pos=%d&allele=%s";
    private static final Set<Reference> SUPPORTED_REFS = ImmutableSet.of(Reference.HG19, Reference.HG18);

    private String getQueryUrl(String ref, String chrom, Long pos, String allele) throws MalformedURLException {
        String params = String.format(PARAM_TEMPLATE, ref, chrom, pos, allele);

        return BASE_URL + params;
    }

    @Override
    @Asynchronous
    public Future<String> getQueryResponse(Beacon beacon, Query query) {
        String res = null;

        // should be POST, but the server accepts GET as well
        try {
            res = HttpUtils.executeRequest(HttpUtils.createRequest(getQueryUrl(query.getReference().toString(), query.getChromosome().toString(), QueryUtils.denormalizePosition(query.getPosition()), query.getAllele()), false, null));
        } catch (MalformedURLException | UnsupportedEncodingException ex) {
            // ignore, already null
        }

        return new AsyncResult<>(res);
    }

    @Override
    @Asynchronous
    public Future<Boolean> parseQueryResponse(Beacon b, String response) {
        Boolean res = ParsingUtils.parseYesNoCaseInsensitive(response);

        return new AsyncResult<>(res);
    }

    @Override
    public Set<Reference> getSupportedReferences() {
        return SUPPORTED_REFS;
    }
}
