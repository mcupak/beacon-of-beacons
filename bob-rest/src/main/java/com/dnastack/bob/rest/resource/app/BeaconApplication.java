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
package com.dnastack.bob.rest.resource.app;

import com.dnastack.bob.rest.resource.AllleleResource;
import com.dnastack.bob.rest.resource.BeaconResource;
import com.dnastack.bob.rest.resource.BeaconResponseResource;
import com.dnastack.bob.rest.resource.ChromosomeResource;
import com.dnastack.bob.rest.resource.ReferenceResource;
import com.dnastack.bob.rest.resource.RestEndPointResource;
import com.dnastack.bob.rest.util.LoggingFilter;
import com.dnastack.bob.rest.util.CORSFilter;
import com.dnastack.bob.rest.util.ExceptionHandler;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * REST application.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@ApplicationPath("/rest")
public class BeaconApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return new HashSet<>(Arrays.asList(RestEndPointResource.class, BeaconResource.class, BeaconResponseResource.class, ReferenceResource.class, ChromosomeResource.class, AllleleResource.class, LoggingFilter.class, CORSFilter.class, ExceptionHandler.class));
    }
}
