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
package com.dnastack.bob.service.fetcher.impl;

import com.dnastack.bob.service.fetcher.api.ResponseFetcher;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Future;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import static com.dnastack.bob.service.fetcher.util.HttpUtils.createRequest;
import static com.dnastack.bob.service.fetcher.util.HttpUtils.executeRequest;

/**
 * Fetcher of beacon responses via HTTP POST.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Stateless
@Named
@Dependent
@LocalBean
public class PostResponseFetcher implements ResponseFetcher, Serializable {

    private static final long serialVersionUID = -7691864900796853059L;

    private List<NameValuePair> getQueryPayload(Map<String, String> payload) {
        List<NameValuePair> nvs = new ArrayList<>();

        for (Entry<String, String> e : payload.entrySet()) {
            nvs.add(new BasicNameValuePair(e.getKey(), e.getValue()));
        }

        return nvs;

    }

    @Override
    @Asynchronous
    public Future<String> getQueryResponse(String url, Map<String, String> payload) {
        String res = null;
        try {
            res = executeRequest(createRequest(url, true, getQueryPayload(payload)));
        } catch (UnsupportedEncodingException ex) {
            // ignore, already null
        }

        return new AsyncResult<>(res);
    }

}
