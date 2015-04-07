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
package com.dnastack.bob.service.processor.impl;

import com.dnastack.bob.persistence.entity.Beacon;
import com.dnastack.bob.persistence.entity.Query;
import com.dnastack.bob.persistence.enumerated.Reference;
import com.dnastack.bob.service.converter.api.AlleleConverter;
import com.dnastack.bob.service.converter.api.ChromosomeConverter;
import com.dnastack.bob.service.converter.api.PositionConverter;
import com.dnastack.bob.service.converter.api.ReferenceConverter;
import com.dnastack.bob.service.converter.impl.EmptyAlleleConverter;
import com.dnastack.bob.service.converter.impl.EmptyChromosomeConverter;
import com.dnastack.bob.service.converter.impl.EmptyPositionConverter;
import com.dnastack.bob.service.converter.impl.EmptyReferenceConverter;
import com.dnastack.bob.service.fetcher.api.ResponseFetcher;
import com.dnastack.bob.service.parser.api.ResponseParser;
import com.dnastack.bob.service.processor.api.BeaconProcessor;
import com.dnastack.bob.service.requester.api.RequestConstructor;
import com.dnastack.bob.service.util.CdiBeanResolver;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import static com.dnastack.bob.service.util.Constants.REQUEST_TIMEOUT;

/**
 * Beacon service handling multiple genome specific queries.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Stateless
@Named
@Dependent
@LocalBean
public class ParallelBeaconProcessor implements BeaconProcessor, Serializable {

    private static final long serialVersionUID = 10L;

    @Inject
    private CdiBeanResolver resolver;

    private List<Future<String>> executeQueriesInParallel(Beacon beacon, Query query) {
        List<Future<String>> fs = new ArrayList<>();

        ResponseFetcher fetcher;
        RequestConstructor requester;
        ChromosomeConverter chromosomeConverter;
        ReferenceConverter referenceConverter;
        PositionConverter positionConverter;
        AlleleConverter alleleConverter;
        try {
            fetcher = (ResponseFetcher) resolver.resolve(beacon.getFetcher());
            requester = (RequestConstructor) resolver.resolve(beacon.getRequester());
            chromosomeConverter = (ChromosomeConverter) resolver.resolve(beacon.getChromosomeConverter());
            if (chromosomeConverter == null) {
                chromosomeConverter = (ChromosomeConverter) resolver.resolve(resolver.getClassId(EmptyChromosomeConverter.class));
            }
            referenceConverter = (ReferenceConverter) resolver.resolve(beacon.getReferenceConverter());
            if (referenceConverter == null) {
                referenceConverter = (ReferenceConverter) resolver.resolve(resolver.getClassId(EmptyReferenceConverter.class));
            }
            positionConverter = (PositionConverter) resolver.resolve(beacon.getPositionConverter());
            if (positionConverter == null) {
                positionConverter = (PositionConverter) resolver.resolve(resolver.getClassId(EmptyPositionConverter.class));
            }
            alleleConverter = (AlleleConverter) resolver.resolve(beacon.getAlleleConverter());
            if (alleleConverter == null) {
                alleleConverter = (AlleleConverter) resolver.resolve(resolver.getClassId(EmptyAlleleConverter.class));
            }
        } catch (ClassNotFoundException ex) {
            return fs;
        }

        if (query.getReference() == null) {
            // query all refs
            for (Reference ref : beacon.getSupportedReferences()) {
                String url = requester.getUrl(beacon, referenceConverter.convert(ref), chromosomeConverter.convert(query.getChromosome()), positionConverter.convert(query.getPosition()), alleleConverter.convert(query.getAllele()), null);
                Map<String, String> payload = requester.getPayload(beacon, referenceConverter.convert(ref), chromosomeConverter.convert(query.getChromosome()), positionConverter.convert(query.getPosition()), alleleConverter.convert(query.getAllele()), null);
                fs.add(fetcher.getQueryResponse(url, payload));
            }
        } else if (beacon.getSupportedReferences().contains(query.getReference())) {
            // query only the specified ref
            String url = requester.getUrl(beacon, referenceConverter.convert(query.getReference()), chromosomeConverter.convert(query.getChromosome()), positionConverter.convert(query.getPosition()), alleleConverter.convert(query.getAllele()), null);
            Map<String, String> payload = requester.getPayload(beacon, referenceConverter.convert(query.getReference()), chromosomeConverter.convert(query.getChromosome()), positionConverter.convert(query.getPosition()), alleleConverter.convert(query.getAllele()), null);
            fs.add(fetcher.getQueryResponse(url, payload));
        }

        return fs;
    }

    private List<Future<Boolean>> parseResultsInParallel(Beacon b, List<Future<String>> fs) {
        List<Future<Boolean>> bs = new ArrayList<>();
        for (Future<String> f : fs) {
            try {
                bs.add(((ResponseParser) resolver.resolve(b.getParser())).parseQueryResponse(b, f.get(REQUEST_TIMEOUT, TimeUnit.SECONDS)));
            } catch (InterruptedException | ExecutionException | TimeoutException | ClassNotFoundException ex) {
                // ignore
            }
        }

        return bs;
    }

    private Boolean collectResults(List<Future<Boolean>> bs) {
        Boolean res = null;

        for (Future<Boolean> b : bs) {
            Boolean r = null;
            try {
                r = b.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                // ignore, already null
            }
            if (r != null) {
                if (r) {
                    res = r;
                    break;
                } else {
                    if (res == null) {
                        res = r;
                    }
                }
            }
        }

        return res;
    }

    @Override
    @Asynchronous
    public Future<Boolean> executeQuery(Beacon beacon, Query query) {
        Boolean res = null;

        if (query != null) {
            res = collectResults(parseResultsInParallel(beacon, executeQueriesInParallel(beacon, query)));
        }

        return new AsyncResult<>(res);
    }
}
