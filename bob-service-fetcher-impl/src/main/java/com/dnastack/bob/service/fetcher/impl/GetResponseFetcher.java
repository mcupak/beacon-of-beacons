/* 
 * Copyright 2018 DNAstack.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dnastack.bob.service.fetcher.impl;

import com.dnastack.bob.service.fetcher.api.ResponseFetcher;
import com.dnastack.bob.service.fetcher.util.HttpUtils;
import org.apache.http.client.methods.HttpRequestBase;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Fetcher of beacon responses via HTTP GET.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Stateless
@Named
@Dependent
@Local(ResponseFetcher.class)
public class GetResponseFetcher implements ResponseFetcher, Serializable {

    private static final long serialVersionUID = -596515080054098873L;

    @Inject
    private HttpUtils httpUtils;

    @Override
    @Asynchronous
    public Future<String> getQueryResponse(String url, Map<String, String> payload, String requester) {
        String res = null;
        try {
            HttpRequestBase request = httpUtils.createRequest(url, false, null, requester);
            request.setHeader("Accept", "application/json, text/plain");
            res = httpUtils.executeRequest(request);
        } catch (UnsupportedEncodingException ex) {
            // ignore, already null
        }

        return new AsyncResult<>(res);
    }

}
