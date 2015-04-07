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
package com.dnastack.bob.service.processor.impl;

import com.dnastack.bob.persistence.entity.Beacon;
import com.dnastack.bob.persistence.entity.Query;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.concurrent.Future;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

import static com.dnastack.bob.service.converter.util.ConvertUtils.denormalizeAllele;
import static com.dnastack.bob.service.converter.util.ConvertUtils.denormalizeAlleleToBrackets;
import static com.dnastack.bob.service.converter.util.ConvertUtils.denormalizeChromosomeToNumber;
import static com.dnastack.bob.service.converter.util.ConvertUtils.denormalizePosition;
import static com.dnastack.bob.service.fetcher.util.HttpUtils.createRequest;
import static com.dnastack.bob.service.fetcher.util.HttpUtils.executeRequest;

/**
 * EBI beacon service.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Stateless
@Named
@LocalBean
public class EbiBeaconProcessor  {

    private static final long serialVersionUID = 11L;
    private static final String BASE_URL = "http://wwwdev.ebi.ac.uk/eva/webservices/rest/v1/ga4gh/beacon";
    private static final String PARAM_TEMPLATE = "?referenceName=%d&start=%d&allele=%s";

    private String getQueryUrl(Integer chrom, Long pos, String allele) throws MalformedURLException {
        String params = String.format(PARAM_TEMPLATE, chrom, pos, allele);

        return BASE_URL + params;
    }

    @Asynchronous
    public Future<String> getQueryResponse(Beacon beacon, Query query) {
        String res = null;
        try {
            res = executeRequest(createRequest(getQueryUrl(denormalizeChromosomeToNumber(query.getChromosome()), denormalizePosition(query.getPosition()), denormalizeAlleleToBrackets(denormalizeAllele(query.getAllele()))), false, null));
        } catch (MalformedURLException | UnsupportedEncodingException ex) {
            // ignore, already null
        }

        return new AsyncResult<>(res);
    }

}
