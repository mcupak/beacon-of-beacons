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
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

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
        return payload.entrySet()
                      .stream()
                      .map((Entry<String, String> e) -> new BasicNameValuePair(e.getKey(), e.getValue()))
                      .collect(Collectors.toList());
    }

    @Override
    @Asynchronous
    public Future<String> getQueryResponse(String url, Map<String, String> payload, String requester) {
        String res = null;
        try {
            res = httpUtils.executeRequest(httpUtils.createRequest(url, true, getQueryPayload(payload), requester));
        } catch (UnsupportedEncodingException ex) {
            // ignore, already null
        }

        return new AsyncResult<>(res);
    }

}
