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
package com.dnastack.bob.rest.app;

import com.dnastack.bob.rest.resource.*;
import com.dnastack.bob.rest.util.ExceptionHandler;
import com.dnastack.bob.rest.util.LoggingFilter;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
        return new HashSet<>(Arrays.asList(RestEndPointResourceImpl.class,
                                           BeaconResourceImpl.class,
                                           BeaconResponseResourceImpl.class,
                                           ReferenceResourceImpl.class,
                                           ChromosomeResourceImpl.class,
                                           AlleleResourceImpl.class,
                                           OrganizationResourceImpl.class,
                                           LoggingFilter.class,
                                           ExceptionHandler.class));
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
