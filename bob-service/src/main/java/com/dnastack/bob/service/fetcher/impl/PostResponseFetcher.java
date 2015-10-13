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
import com.dnastack.bob.service.fetcher.util.HttpUtils;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * Fetcher of beacon responses via HTTP POST.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Stateless
@Named
@Dependent
@Local(ResponseFetcher.class)
public class PostResponseFetcher implements ResponseFetcher, Serializable {

    private static final long serialVersionUID = -7691864900796853059L;

    @Inject
    private HttpUtils httpUtils;

    private List<NameValuePair> getQueryPayload(Map<String, String> payload) {
        return payload.entrySet().stream().map((Entry<String, String> e) -> new BasicNameValuePair(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    @Override
    @Asynchronous
    public Future<String> getQueryResponse(String url, Map<String, String> payload, String requester) {
        String res = null;
        try {
            System.out.println(getQueryPayload(payload).toString());
            res = httpUtils.executeRequest(httpUtils.createRequest(url, true, getQueryPayload(payload), requester));
        } catch (UnsupportedEncodingException ex) {
            // ignore, already null
        }

        return new AsyncResult<>(res);
    }

}
