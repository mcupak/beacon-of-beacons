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
package com.dnastack.bob.service.fetcher.util;

import lombok.extern.java.Log;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

/**
 * Util methods for querying over HTTP.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Named
@Dependent
@Log
public class HttpUtils {

    private static final int REQUEST_TIMEOUT = 30;
    private CloseableHttpClient httpClient;

    @PostConstruct
    private void init() {
        RequestConfig config = RequestConfig.custom()
                                            .setSocketTimeout(REQUEST_TIMEOUT * 1000)
                                            .setConnectTimeout(REQUEST_TIMEOUT * 1000)
                                            .setConnectionRequestTimeout(REQUEST_TIMEOUT * 1000)
                                            .build();
        httpClient = HttpClients.custom().setDefaultRequestConfig(config).build();
    }

    @PreDestroy
    private void close() {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException ex) {
                log.severe(ex.getMessage());
            }
        }
    }

    private HttpGet createGet(String url) {
        HttpGet httpGet;
        httpGet = new HttpGet(url);

        return httpGet;
    }

    private HttpPost createPost(String url, List<NameValuePair> data) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new UrlEncodedFormEntity(data));

        return httpPost;
    }

    private String getForwardedFor(String ip) {
        final String token = "for=";
        String res = "";
        if (ip == null || ip.isEmpty()) {
            res = token + "unknown";
        } else {
            String[] sources = ip.split(",");
            for (String s : sources) {
                String part = (s.contains(":") && !s.startsWith("\"")) ? "\"" + s + "\"" : s;
                res += token + part + ",";
            }
            if (res.endsWith(",")) {
                res = res.substring(0, res.length() - 1);
            }
        }
        return res;
    }

    /**
     * Creates a GET/POST request object.
     *
     * @param url       url
     * @param post      true if we want to create a POST, false for GET
     * @param data      payload (only needed for POST)
     * @param requester client's IP
     * @return request
     * @throws UnsupportedEncodingException
     */
    public HttpRequestBase createRequest(String url, boolean post, List<NameValuePair> data, String requester) throws UnsupportedEncodingException {
        HttpRequestBase request = (post) ? createPost(url, data) : createGet(url);
        request.setHeader("Forwarded", getForwardedFor(requester));

        return request;
    }

    /**
     * Executes GET/POST and obtain the response.
     *
     * @param request request
     * @return response
     */
    public String executeRequest(HttpRequestBase request) {
        String response = null;
        CloseableHttpResponse res = null;
        try {
            res = httpClient.execute(request);
            HttpEntity entity = res.getEntity();
            response = (entity == null) ? null : EntityUtils.toString(entity);
        } catch (IOException ex) {
            log.severe(Arrays.asList(ex.getStackTrace()).toString());
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (IOException ex) {
                log.severe(Arrays.asList(ex.getStackTrace()).toString());
            }
        }
        return response;
    }
}
