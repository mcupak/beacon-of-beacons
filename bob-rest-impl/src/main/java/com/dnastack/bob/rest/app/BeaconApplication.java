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
package com.dnastack.bob.rest.app;

import com.dnastack.bob.rest.resource.AlleleResourceImpl;
import com.dnastack.bob.rest.resource.BeaconResourceImpl;
import com.dnastack.bob.rest.resource.BeaconResponseResourceImpl;
import com.dnastack.bob.rest.resource.ChromosomeResourceImpl;
import com.dnastack.bob.rest.resource.OrganizationResourceImpl;
import com.dnastack.bob.rest.resource.ReferenceResourceImpl;
import com.dnastack.bob.rest.resource.RestEndPointResourceImpl;
import com.dnastack.bob.rest.util.ExceptionHandler;
import com.dnastack.bob.rest.util.LoggingFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;

/**
 * REST application.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@ApplicationPath("")
public class BeaconApplication extends Application {

    private Set<Object> singletons;

    @Override
    public Set<Class<?>> getClasses() {
        return new HashSet<>(Arrays.asList(RestEndPointResourceImpl.class, BeaconResourceImpl.class, BeaconResponseResourceImpl.class, ReferenceResourceImpl.class, ChromosomeResourceImpl.class, AlleleResourceImpl.class, OrganizationResourceImpl.class, LoggingFilter.class, ExceptionHandler.class));
    }

    @Override
    public Set<Object> getSingletons() {
        if (singletons == null) {
            CorsFilter corsFilter = new CorsFilter();
            corsFilter.getAllowedOrigins().add("*");
            corsFilter.setAllowCredentials(true);
            corsFilter.setAllowedMethods("GET, POST, PUT, DELETE, OPTIONS, HEAD");
//            corsFilter.setAllowedHeaders("origin, content-type, accept, authorization");
            corsFilter.setCorsMaxAge(1209600);

            singletons = new HashSet<>();
            singletons.add(corsFilter);
        }
        return Collections.unmodifiableSet(singletons);
    }
}
