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
package com.dnastack.bob.service;

import com.dnastack.bob.log.Logged;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.enterprise.context.RequestScoped;

/**
 * Implementation of a service managing REST end points.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RequestScoped
public class RestEndPointServiceImpl implements RestEndPointService {

    private static final RestEndPoint beacons = new RestEndPoint("beacons", "beacons", "beacons");
    private static final RestEndPoint responses = new RestEndPoint("responses", "responses", "responses?chrom=14&pos=106833421&allele=A");

    @Logged
    @Override
    public Collection<RestEndPoint> showEndPoints(String url) {
        Set<RestEndPoint> reps = new HashSet<>();
        reps.add(new RestEndPoint(beacons.getId(), url + beacons.getBaseUrl(), url + beacons.getExample()));
        reps.add(new RestEndPoint(responses.getId(), url + responses.getBaseUrl(), url + responses.getExample()));

        return Collections.unmodifiableSet(reps);
    }

}
