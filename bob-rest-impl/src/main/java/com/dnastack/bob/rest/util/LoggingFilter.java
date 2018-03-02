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
package com.dnastack.bob.rest.util;

import lombok.extern.java.Log;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.dnastack.bob.rest.util.IpExtractor.extractIpAddress;

/**
 * Filter logging IP addresses requests coming to the REST API.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Provider
@Log
public class LoggingFilter implements ContainerRequestFilter, Serializable {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss Z");
    private static final long serialVersionUID = 4233460164771834012L;

    @Context
    private HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String method = request.getMethod();
        String url = request.getRequestURI();
        String q = request.getQueryString();
        if (q != null) {
            url += "?";
            url += q;
        }

        String ip = extractIpAddress(request);

        log.info(String.format("%s : Request from %s: %s %s", DATE_FORMAT.format(new Date()), ip, method, url));
    }
}
