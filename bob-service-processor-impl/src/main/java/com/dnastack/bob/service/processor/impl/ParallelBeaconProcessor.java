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
package com.dnastack.bob.service.processor.impl;

import com.dnastack.bob.persistence.entity.Beacon;
import com.dnastack.bob.persistence.entity.BeaconResponse;
import com.dnastack.bob.persistence.entity.Query;
import com.dnastack.bob.persistence.enumerated.Reference;
import com.dnastack.bob.service.converter.api.*;
import com.dnastack.bob.service.converter.impl.*;
import com.dnastack.bob.service.fetcher.api.ResponseFetcher;
import com.dnastack.bob.service.parser.api.ExternalUrlParser;
import com.dnastack.bob.service.parser.api.MetadataParser;
import com.dnastack.bob.service.parser.api.ResponseParser;
import com.dnastack.bob.service.processor.api.BeaconProcessor;
import com.dnastack.bob.service.processor.util.CdiBeanResolver;
import com.dnastack.bob.service.processor.util.EjbResolver;
import com.dnastack.bob.service.requester.api.RequestConstructor;
import lombok.*;
import lombok.experimental.Builder;
import lombok.extern.java.Log;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

import static com.dnastack.bob.service.processor.util.ErrorUtils.getErrorMessage;

/**
 * Beacon service handling multiple genome specific queries.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Stateless
@Named
@Dependent
@Log
@Local(BeaconProcessor.class)
public class ParallelBeaconProcessor implements BeaconProcessor, Serializable {

    private static final long serialVersionUID = 10L;
    private static final int REQUEST_TIMEOUT = 30;

    @Inject
    private CdiBeanResolver cdiResolver;

    @Inject
    private EjbResolver ejbResolver;

    @ToString
    @EqualsAndHashCode
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuppressWarnings("deprecation")
    private static class FutureBeaconResponse {

        private Future<Boolean> response = null;
        private Future<String> externalUrl = null;
        private Future<Map<String, String>> info = null;
    }

    private List<Future<String>> executeQueriesInParallel(Beacon beacon, Query query) {
        List<Future<String>> fs = new ArrayList<>();

        ResponseFetcher fetcher;
        RequestConstructor requester;
        ChromosomeConverter chromosomeConverter;
        ReferenceConverter referenceConverter;
        PositionConverter positionConverter;
        AlleleConverter alleleConverter;
        BeaconConverter beaconConverter;
        try {
            fetcher = (ResponseFetcher) ejbResolver.resolve(beacon.getFetcher());
            requester = (RequestConstructor) cdiResolver.resolve(beacon.getRequester());
            chromosomeConverter = (ChromosomeConverter) cdiResolver.resolve(beacon.getChromosomeConverter());
            if (chromosomeConverter == null) {
                chromosomeConverter = (ChromosomeConverter) cdiResolver.resolve(cdiResolver.getClassId(
                        EmptyChromosomeConverter.class));
            }
            referenceConverter = (ReferenceConverter) cdiResolver.resolve(beacon.getReferenceConverter());
            if (referenceConverter == null) {
                referenceConverter = (ReferenceConverter) cdiResolver.resolve(cdiResolver.getClassId(
                        EmptyReferenceConverter.class));
            }
            positionConverter = (PositionConverter) cdiResolver.resolve(beacon.getPositionConverter());
            if (positionConverter == null) {
                positionConverter = (PositionConverter) cdiResolver.resolve(cdiResolver.getClassId(
                        EmptyPositionConverter.class));
            }
            alleleConverter = (AlleleConverter) cdiResolver.resolve(beacon.getAlleleConverter());
            if (alleleConverter == null) {
                alleleConverter = (AlleleConverter) cdiResolver.resolve(cdiResolver.getClassId(EmptyAlleleConverter.class));
            }
            beaconConverter = (BeaconConverter) cdiResolver.resolve(beacon.getBeaconConverter());
            if (beaconConverter == null) {
                beaconConverter = (BeaconConverter) cdiResolver.resolve(cdiResolver.getClassId(BeaconIdBeaconConverter.class));
            }
        } catch (ClassNotFoundException | NamingException ex) {
            return fs;
        }

        String ip = query.getIp();
        if (query.getReference() == null) {
            // query all refs
            for (Reference ref : beacon.getSupportedReferences()) {
                String url = requester.getUrl(beacon.getUrl(),
                                              beaconConverter.convert(beacon),
                                              referenceConverter.convert(ref),
                                              chromosomeConverter.convert(query.getChromosome()),
                                              positionConverter.convert(query.getPosition()),
                                              alleleConverter.convert(query.getReferenceAllele()),
                                              alleleConverter.convert(query.getAllele()),
                                              null);
                Map<String, String> payload = requester.getPayload(beacon.getUrl(),
                                                                   beaconConverter.convert(beacon),
                                                                   referenceConverter.convert(ref),
                                                                   chromosomeConverter.convert(query.getChromosome()),
                                                                   positionConverter.convert(query.getPosition()),
                                                                   alleleConverter.convert(query.getReferenceAllele()),
                                                                   alleleConverter.convert(query.getAllele()),
                                                                   null);
                fs.add(fetcher.getQueryResponse(url, payload, ip));
            }
        } else if (beacon.getSupportedReferences().contains(query.getReference())) {
            // query only the specified ref
            String url = requester.getUrl(beacon.getUrl(),
                                          beaconConverter.convert(beacon),
                                          referenceConverter.convert(query.getReference()),
                                          chromosomeConverter.convert(query.getChromosome()),
                                          positionConverter.convert(query.getPosition()),
                                          alleleConverter.convert(query.getReferenceAllele()),
                                          alleleConverter.convert(query.getAllele()),
                                          null);
            Map<String, String> payload = requester.getPayload(beacon.getUrl(),
                                                               beaconConverter.convert(beacon),
                                                               referenceConverter.convert(query.getReference()),
                                                               chromosomeConverter.convert(query.getChromosome()),
                                                               positionConverter.convert(query.getPosition()),
                                                               alleleConverter.convert(query.getReferenceAllele()),
                                                               alleleConverter.convert(query.getAllele()),
                                                               null);
            fs.add(fetcher.getQueryResponse(url, payload, ip));
        }

        return fs;
    }

    private List<FutureBeaconResponse> parseResultsInParallel(Beacon b, List<Future<String>> fs) {
        List<FutureBeaconResponse> bs = new ArrayList<>();
        if (b != null) {
            fs.stream().forEach((Future<String> f) -> {
                try {
                    ResponseParser rp = (ResponseParser) ejbResolver.resolve(b.getResponseParser());
                    ExternalUrlParser eup = (ExternalUrlParser) ejbResolver.resolve(b.getExternalUrlParser());
                    MetadataParser mp = (MetadataParser) ejbResolver.resolve(b.getMetadataParser());

                    FutureBeaconResponse fbr = FutureBeaconResponse.builder()
                                                                   .response(rp == null ? null : rp.parse(b, f))
                                                                   .externalUrl(eup == null ? null : eup.parse(b, f))
                                                                   .info(mp == null ? null : mp.parse(b, f))
                                                                   .build();
                    bs.add(fbr);
                } catch (Exception ex) {
                    log.severe(getErrorMessage(ex));
                }
            });
        }

        return bs;
    }

    private BeaconResponse collectResults(List<FutureBeaconResponse> bs) {
        BeaconResponse res = new BeaconResponse();

        for (FutureBeaconResponse fbr : bs) {
            Boolean response = null;
            String url = null;
            Map<String, String> info = null;
            try {
                response = fbr.getResponse() == null ? null : fbr.getResponse().get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
                url = fbr.getExternalUrl() == null ? null : fbr.getExternalUrl().get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
                info = fbr.getInfo() == null ? null : fbr.getInfo().get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                log.log(Level.SEVERE, "An error occurred while collecting results for a FutureBeaconResponse", ex);
            }
            if (response != null) {
                if (response) {
                    res.setResponse(response);
                    res.setExternalUrl(url);
                    res.setInfo(info);
                } else {
                    if (res.getResponse() == null) {
                        res.setResponse(response);
                        res.setExternalUrl(url);
                        res.setInfo(info);
                    }
                }
            }
        }

        return res;
    }

    @Override
    @Asynchronous
    public Future<BeaconResponse> executeQuery(Beacon beacon, Query query) {
        BeaconResponse res = null;
        if (query != null) {
            res = collectResults(parseResultsInParallel(beacon, executeQueriesInParallel(beacon, query)));
        }

        return new AsyncResult<>(res);
    }
}
