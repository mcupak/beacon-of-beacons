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
package com.dnastack.bob.service.impl;

import com.dnastack.bob.persistence.api.BeaconDao;
import com.dnastack.bob.persistence.entity.Beacon;
import com.dnastack.bob.persistence.entity.Query;
import com.dnastack.bob.persistence.entity.User;
import com.dnastack.bob.persistence.enumerated.Chromosome;
import com.dnastack.bob.persistence.enumerated.Reference;
import com.dnastack.bob.service.api.BeaconResponseService;
import com.dnastack.bob.service.dto.BeaconResponseDto;
import com.dnastack.bob.service.dto.UserDto;
import com.dnastack.bob.service.lrg.Brca;
import com.dnastack.bob.service.lrg.Brca2;
import com.dnastack.bob.service.lrg.LrgConvertor;
import com.dnastack.bob.service.lrg.LrgLocus;
import com.dnastack.bob.service.lrg.LrgReference;
import com.dnastack.bob.service.processor.api.BeaconProcessor;
import com.dnastack.bob.service.processor.api.BeaconResponse;
import com.dnastack.bob.service.util.EntityDtoConverter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.validation.Validator;

import static com.dnastack.bob.service.util.Constants.REFERENCE_MAPPING;
import static com.dnastack.bob.service.util.Constants.REQUEST_TIMEOUT;
import static com.dnastack.bob.service.util.EntityDtoConverter.getUser;

/**
 * Implementation of a service for managing beacon responses.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Stateless
@Local(BeaconResponseService.class)
@Named
@Dependent
@Transactional
public class BeaconResponseServiceImpl implements BeaconResponseService, Serializable {

    private static final long serialVersionUID = 103L;

    @Inject
    private BeaconDao beaconDao;

    @Inject
    private BeaconProcessor beaconProcessor;

    @Inject
    private Validator validator;

    @Inject
    @Brca
    private LrgConvertor brcaConvertor;

    @Inject
    @Brca2
    private LrgConvertor brca2Convertor;

    private Chromosome normalizeChromosome(String chrom) {
        // parse chrom value
        if (chrom != null) {
            String orig = chrom.toUpperCase();
            for (Chromosome c : Chromosome.values()) {
                if (orig.endsWith(c.toString())) {
                    return c;
                }
            }
        }

        return null;
    }

    private String normalizeAllele(String allele) {
        if (allele == null || allele.isEmpty()) {
            return null;
        }

        String res = allele.toUpperCase();
        if (res.equals("DEL") || res.equals("INS")) {
            return res.substring(0, 1);
        }
        if (Pattern.matches("([D,I])|([A,C,T,G]+)", res)) {
            return res;
        }

        return null;
    }

    private Reference normalizeReference(String ref) {
        if (ref == null || ref.isEmpty()) {
            return null;
        }

        for (Reference s : REFERENCE_MAPPING.keySet()) {
            if (s.toString().equalsIgnoreCase(ref)) {
                return s;
            }
        }
        for (Entry<Reference, String> e : REFERENCE_MAPPING.entrySet()) {
            if (e.getValue().equalsIgnoreCase(ref)) {
                return e.getKey();
            }
        }

        return null;
    }

    /**
     * Obtains a canonical query object without persisting.
     *
     * @param chrom  chromosome
     * @param pos    position
     * @param allele allele
     * @param ref    genome
     *
     * @return normalized query
     */
    private Query prepareQuery(String chrom, Long pos, String allele, String ref, User u) {
        Chromosome c = normalizeChromosome(chrom);
        Reference r = normalizeReference(ref);

        return Query.builder().chromosome(c == null ? null : c).position(pos).allele(normalizeAllele(allele)).reference(r == null ? null : r).user(u).build();
    }

    private boolean queryNotNormalizedOrValid(Query q, String ref) {
        return (!(ref == null || ref.isEmpty()) && q.getReference() == null) || !validator.validate(q).isEmpty();
    }

    @Asynchronous
    private Future<BeaconResponse> queryBeacon(Beacon b, Query q) throws ClassNotFoundException {
        BeaconResponse total = new BeaconResponse();

        if (b.getAggregator()) {
            total.setResponse(false);

            // execute queries in parallel
            Map<Beacon, Future<BeaconResponse>> futures = new HashMap<>();
            Set<Beacon> children = beaconDao.findDescendants(b, false, true, false, false);
            children.stream().forEach((Beacon bt) -> {
                futures.put(bt, beaconProcessor.executeQuery(bt, q));
            });

            // collect results
            for (Entry<Beacon, Future<BeaconResponse>> e : futures.entrySet()) {
                BeaconResponse res = null;
                try {
                    res = e.getValue().get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                    // ignore, response already null
                }
                if (res != null && res.getResponse() != null && res.getResponse()) {
                    // hide everything other than reponses for aggregates
                    total.setResponse(true);
                }
            }
        } else {
            try {
                total = beaconProcessor.executeQuery(b, q).get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                // ignore
            }
        }

        return new AsyncResult<>(total);
    }

    private Map<Beacon, BeaconResponse> setUpBeaconResponseMapForBeacons(Collection<Beacon> bs, Query q) {
        Map<Beacon, BeaconResponse> brs = new HashMap<>();
        if (bs != null) {
            bs.stream().filter((Beacon b) -> {
                return b != null;
            }).forEach((b) -> {
                brs.put(b, BeaconResponse.builder().beacon(b).query(q).response(null).build());
            });
        }

        return brs;
    }

    private Map<Beacon, BeaconResponse> setUpBeaconResponseMapForIds(Collection<String> beaconIds, Query q) {
        if (beaconIds == null) {
            return setUpBeaconResponseMapForBeacons(beaconDao.findByVisibility(true), q);
        }

        Set<Beacon> bs = new HashSet<>();
        beaconIds.stream().map((String id) -> beaconDao.findById(id)).filter((b) -> (b != null && b.getVisible())).forEach((b) -> {
            bs.add(b);
        });

        return setUpBeaconResponseMapForBeacons(bs, q);
    }

    private Map<Beacon, BeaconResponse> fillBeaconResponseMap(Map<Beacon, BeaconResponse> brs, Query q) throws ClassNotFoundException {
        // execute queries in parallel
        Map<Beacon, Future<BeaconResponse>> futures = new HashMap<>();
        for (Beacon b : brs.keySet()) {
            futures.put(b, queryBeacon(b, q));
        }

        // collect results
        futures.entrySet().stream().forEach((Entry<Beacon, Future<BeaconResponse>> e) -> {
            BeaconResponse b = null;
            try {
                b = e.getValue().get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                // ignore, response already null
            }
            if (b != null) {
                brs.get(e.getKey()).setResponse(b.getResponse());
                brs.get(e.getKey()).setExternalUrl(b.getExternalUrl());
            }
        });

        return brs;
    }

    private Query getQuery(String chrom, Long pos, String allele, String ref, UserDto u) {
        LrgConvertor l = null;
        String c = chrom;
        Long p = pos;
        String r = ref;
        String a = allele;

        if (ref != null && ref.equalsIgnoreCase(LrgReference.LRG.toString())) {
            if (chrom.equalsIgnoreCase(LrgLocus.LRG_292.toString())) {
                l = brcaConvertor;
            } else if (chrom.equalsIgnoreCase(LrgLocus.LRG_293.toString())) {
                l = brca2Convertor;
            }

            if (l != null) {
                c = l.getChromosome().toString();
                p = l.getPosition(pos);
                r = l.getReference().toString();
            }
        }

        return prepareQuery(c, p, a, r, getUser(u));
    }

    private Multimap<Beacon, Beacon> setUpChildrenMultimap(Collection<Beacon> beacons) {
        Multimap<Beacon, Beacon> children = HashMultimap.create();
        beacons.stream().forEach((Beacon b) -> {
            if (b.getAggregator()) {
                children.putAll(b, beaconDao.findDescendants(b, false, true, false, false));
            } else {
                children.put(b, b);
            }
        });
        return children;
    }

    private Map<Beacon, BeaconResponse> fillAggregateResponses(Map<Beacon, BeaconResponse> parentResponses, Map<Beacon, BeaconResponse> childrenResponses, Multimap<Beacon, Beacon> children, Query q) {
        Map<Beacon, BeaconResponse> res = new HashMap<>(parentResponses);

        for (Entry<Beacon, BeaconResponse> e : res.entrySet()) {
            BeaconResponse response = e.getValue();
            if (response == null) {
                response = BeaconResponse.builder().beacon(e.getKey()).query(q).response(null).build();
            }

            Collection<Beacon> cs = children.get(e.getKey());
            boolean notAllResponsesNull = false;
            for (Beacon c : cs) {
                BeaconResponse cr = childrenResponses.get(c);
                if (cr != null && cr.getResponse() != null) {
                    notAllResponsesNull = true;
                    if (cr.getResponse()) {
                        response.setResponse(true);
                        break;
                    }
                }
            }
            if (notAllResponsesNull && response.getResponse() == null) {
                response.setResponse(false);
            }

            e.setValue(response);
        }

        return res;
    }

    private Collection<BeaconResponse> queryMultipleBeacons(Collection<String> beaconIds, String chrom, Long pos, String allele, String ref, UserDto u) throws ClassNotFoundException {
        Query q = getQuery(chrom, pos, allele, ref, u);

        // init to create a response for each beacon even if the query is invalid
        Map<Beacon, BeaconResponse> brs = setUpBeaconResponseMapForIds(beaconIds, q);

        // validate query
        if (queryNotNormalizedOrValid(q, ref)) {
            return brs.values();
        }

        // construct map of atomic nodes covered by aggregates
        Multimap<Beacon, Beacon> children = setUpChildrenMultimap(brs.keySet());
        // obtain children's responses
        Map<Beacon, BeaconResponse> childrenResponses = fillBeaconResponseMap(setUpBeaconResponseMapForBeacons(new HashSet<>(children.values()), q), q);

        // aggregate
        return fillAggregateResponses(brs, childrenResponses, children, q).values();
    }

    @Override
    public BeaconResponseDto queryBeacon(String beaconId, String chrom, Long pos, String allele, String ref, UserDto onBehalfOf) throws ClassNotFoundException {
        Query q = getQuery(chrom, pos, allele, ref, onBehalfOf);

        Beacon b = beaconDao.findById(beaconId);
        if (b == null || !b.getVisible()) {
            // nonexisting beaconId param specified
            Beacon beacon = new Beacon();
            beacon.setId("null");
            beacon.setName("invalid beacon");
            beacon.setOrganization(null);
            beacon.setUrl(null);
            beacon.setEnabled(false);
            beacon.setVisible(false);
            beacon.setAggregator(true);
            return EntityDtoConverter.getBeaconResponseDto(BeaconResponse.builder().beacon(beacon).query(q).response(null).build());
        }

        BeaconResponse br = BeaconResponse.builder().beacon(b).query(q).response(null).build();
        if (queryNotNormalizedOrValid(q, ref)) {
            return EntityDtoConverter.getBeaconResponseDto(br);
        }

        try {
            BeaconResponse inter = queryBeacon(b, q).get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
            br.setResponse(inter.getResponse());
            br.setExternalUrl(inter.getExternalUrl());
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            // ignore, response already null
        }

        return EntityDtoConverter.getBeaconResponseDto(br);
    }

    @Override
    public Collection<BeaconResponseDto> queryBeacons(Collection<String> beaconIds, String chrom, Long pos, String allele, String ref, UserDto onBehalfOf) throws ClassNotFoundException {
        if (beaconIds == null) {
            return new HashSet<>();
        }

        return EntityDtoConverter.getBeaconResponseDtos(queryMultipleBeacons(beaconIds, chrom, pos, allele, ref, onBehalfOf));
    }

    @Override
    public Collection<BeaconResponseDto> queryAll(String chrom, Long pos, String allele, String ref, UserDto onBehalfOf) throws ClassNotFoundException {
        return EntityDtoConverter.getBeaconResponseDtos(queryMultipleBeacons(null, chrom, pos, allele, ref, onBehalfOf));
    }

}
