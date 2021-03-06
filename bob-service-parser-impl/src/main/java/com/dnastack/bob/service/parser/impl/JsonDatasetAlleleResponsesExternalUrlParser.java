/*
 * The MIT License
 *
 * Copyright 2018 Max Barkley (max@dnastack.com).
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
package com.dnastack.bob.service.parser.impl;

import com.dnastack.bob.persistence.entity.Beacon;
import com.dnastack.bob.service.parser.api.ExternalUrlParser;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.dnastack.bob.service.parser.util.ParseUtils.REQUEST_TIMEOUT;
import static com.dnastack.bob.service.parser.util.ParseUtils.parseStringFromJson;

/**
 * Parses external URL from a JSON field in a datasetAlleleResults array.
 *
 * @author Max Barkley (max@dnastack.com)
 * @version 1.0
 */
@Stateless
@Named
@Dependent
@Local(ExternalUrlParser.class)
public class JsonDatasetAlleleResponsesExternalUrlParser implements ExternalUrlParser, Serializable {

    public static final String EXTERNAL_URL_FIELD = "externalUrl";
    public static final String RESPONSE_FIELD = "datasetAlleleResponses";
    private static final long serialVersionUID = 1L;

    @Asynchronous
    @Override
    public Future<String> parse(Beacon b, Future<String> response) {
        String res = null;
        try {
            res = parseStringFromJson(response.get(REQUEST_TIMEOUT, TimeUnit.SECONDS),
                                      RESPONSE_FIELD,
                                      EXTERNAL_URL_FIELD);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            // ignore
        }

        return new AsyncResult<>(res);
    }
}
