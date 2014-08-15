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
package com.dnastack.beacon.service;

import com.dnastack.beacon.core.Beacon;
import com.dnastack.beacon.core.BeaconResponse;
import com.dnastack.beacon.core.BeaconService;
import com.dnastack.beacon.core.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.ejb.AsyncResult;

/**
 * Abstract beacon service handling multiple genome specific queries.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public abstract class GenomeAwareBeaconService implements BeaconService, Serializable {

    private static final long serialVersionUID = 10L;

    protected abstract String[] getRefs();

    @Override
    public Future<BeaconResponse> executeQuery(Beacon beacon, Query query) {
        BeaconResponse res = new BeaconResponse(beacon, query, null);

        // execute queries in parallel
        List<Future<String>> fs = new ArrayList<>();
        for (String ref : getRefs()) {
            fs.add(getQueryResponse(beacon, query, ref));
        }

        // parse queries in parallel
        List<Future<Boolean>> bs = new ArrayList<>();
        for (Future<String> f : fs) {
            try {
                bs.add(parseQueryResponse(f.get()));
            } catch (InterruptedException | ExecutionException ex) {
                // ignore
            }
        }

        // collect results
        for (Future<Boolean> b : bs) {
            Boolean r = null;
            try {
                r = b.get();
            } catch (InterruptedException | ExecutionException ex) {
                // ignore, already null
            }
            if (r != null) {
                if (r) {
                    res.setResponse(r);
                    break;
                } else {
                    if (res.getResponse() == null) {
                        res.setResponse(r);
                    }
                }
            }
        }

        return new AsyncResult<>(res);
    }
}
