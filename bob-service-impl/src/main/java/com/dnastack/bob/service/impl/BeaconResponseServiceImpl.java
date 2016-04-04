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
import com.dnastack.bob.persistence.api.BeaconResponseDao;
import com.dnastack.bob.persistence.api.QueryDao;
import com.dnastack.bob.persistence.api.UserDao;
import com.dnastack.bob.persistence.entity.Beacon;
import com.dnastack.bob.persistence.entity.BeaconResponse;
import com.dnastack.bob.persistence.entity.Query;
import com.dnastack.bob.persistence.entity.User;
import com.dnastack.bob.persistence.enumerated.Chromosome;
import com.dnastack.bob.persistence.enumerated.Reference;
import com.dnastack.bob.service.api.BeaconResponseService;
import com.dnastack.bob.service.dto.BeaconResponseDto;
import com.dnastack.bob.service.dto.UserDto;
import com.dnastack.bob.service.lrg.*;
import com.dnastack.bob.service.mapper.api.BeaconResponseMapper;
import com.dnastack.bob.service.mapper.api.UserMapper;
import com.dnastack.bob.service.processor.api.BeaconProcessor;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.validation.Validator;
import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import static com.dnastack.bob.service.converter.util.ConvertUtils.REFERENCE_MAPPING;


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

    private static final int REQUEST_TIMEOUT = 30;

    @Inject
    private BeaconDao beaconDao;

    @Inject
    private BeaconResponseDao beaconResponseDao;

    @Inject
    private QueryDao queryDao;

    @Inject
    private UserDao userDao;

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

    @Inject
    private BeaconResponseMapper beaconResponseMapper;

    @Inject
    private UserMapper userMapper;

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
     * @return normalized query
     */
    private Query prepareQuery(String chrom, Long pos, String allele, String ref, User u, String ip) {
        Chromosome c = normalizeChromosome(chrom);
        Reference r = normalizeReference(ref);

        return Query.builder().chromosome(c == null ? null : c).position(pos).allele(normalizeAllele(allele)).reference(r == null ? null : r).user(u).ip(ip).submitted(new Date()).build();
    }

    private boolean queryNotNormalizedOrValid(Query q, String ref) {
        return (!(ref == null || ref.isEmpty()) && q.getReference() == null) || !validator.validate(q).isEmpty();
    }

    private User setUpUser(UserDto u) {
        if (u == null) {
            return null;
        }

        User user = null;
        if (u.getUserName() != null) {
            List<User> found = userDao.findByUserName(u.getUserName());
            user = found == null || found.isEmpty() ? null : found.get(0);
        }
        if (user == null) {
            user = userMapper.mapDtoToEntity(u);
        }

        return user;
    }

    private Query setUpQuery(String chrom, Long pos, String allele, String ref, UserDto u, String ip) {
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

        return prepareQuery(c, p, a, r, setUpUser(u), ip);
    }

    private Query saveQuery(Query q) {
        return queryDao.save(q);
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
                brs.get(e.getKey()).setInfo(b.getInfo());
            }
        });

        return brs;
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
            String externalUrl = null;
            Map<String, String> info = null;
            for (Beacon c : cs) {
                BeaconResponse cr = childrenResponses.get(c);
                if (cr != null && cr.getResponse() != null) {
                    notAllResponsesNull = true;
                    externalUrl = cr.getExternalUrl();
                    info = cr.getInfo();
                    if (cr.getResponse()) {
                        response.setResponse(true);
                        break;
                    }
                }
            }
            if (notAllResponsesNull && response.getResponse() == null) {
                response.setResponse(false);
            }
            if (e.getKey().getAggregator() == null || !e.getKey().getAggregator()) {
                response.setExternalUrl(externalUrl);
                response.setInfo(info);
            }

            e.setValue(response);
        }

        return res;
    }

    @Asynchronous
    private Future<BeaconResponse> queryBeacon(Beacon b, Query q) throws ClassNotFoundException {
        BeaconResponse total = new BeaconResponse();
        BeaconResponse logged;

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
                BeaconResponse childLogged;
                try {
                    res = e.getValue().get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
                    childLogged = beaconResponseMapper.mapEntityToEntity(res);
                } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                    childLogged = BeaconResponse.builder().response(null).build();
                }
                childLogged.setBeacon(e.getKey());
                childLogged.setQuery(q);
                beaconResponseDao.save(childLogged);

                if (res != null && res.getResponse() != null && res.getResponse()) {
                    // hide everything other than reponses for aggregates
                    total.setResponse(true);
                }
            }
            logged = beaconResponseMapper.mapEntityToEntity(total);
        } else {
            try {
                total = beaconProcessor.executeQuery(b, q).get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
                logged = beaconResponseMapper.mapEntityToEntity(total);
            } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                logged = BeaconResponse.builder().response(null).build();
            }
        }

        logged.setBeacon(b);
        logged.setQuery(q);
        beaconResponseDao.save(logged);

        return new AsyncResult<>(total);
    }

    private Collection<BeaconResponse> queryMultipleBeacons(Collection<String> beaconIds, String chrom, Long pos, String allele, String ref, UserDto u, String ip) throws ClassNotFoundException {
        Query q = setUpQuery(chrom, pos, allele, ref, u, ip);

        // init to create a response for each beacon even if the query is invalid
        Map<Beacon, BeaconResponse> brs = setUpBeaconResponseMapForIds(beaconIds, q);

        // validate query
        if (queryNotNormalizedOrValid(q, ref)) {
            return brs.values();
        }
        q = saveQuery(q);

        // construct map of atomic nodes covered by aggregates
        Multimap<Beacon, Beacon> children = setUpChildrenMultimap(brs.keySet());
        // obtain children's responses
        Map<Beacon, BeaconResponse> childrenResponses = fillBeaconResponseMap(setUpBeaconResponseMapForBeacons(new HashSet<>(children.values()), q), q);

        // aggregate
        return fillAggregateResponses(brs, childrenResponses, children, q).values();
    }

    @Override
    public BeaconResponseDto queryBeacon(String beaconId, String chrom, Long pos, String allele, String ref, UserDto onBehalfOf, String ip) throws ClassNotFoundException {
        Query q = setUpQuery(chrom, pos, allele, ref, onBehalfOf, ip);

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
            return beaconResponseMapper.mapEntityToDto(BeaconResponse.builder().beacon(beacon).query(q).response(null).build(), false);
        }

        BeaconResponse br = BeaconResponse.builder().beacon(b).query(q).response(null).build();
        if (queryNotNormalizedOrValid(q, ref)) {
            return beaconResponseMapper.mapEntityToDto(br, false);
        }
        q = saveQuery(q);

        try {
            BeaconResponse inter = queryBeacon(b, q).get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
            br.setResponse(inter.getResponse());
            br.setExternalUrl(inter.getExternalUrl());
            br.setInfo(inter.getInfo());
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            // ignore, response already null
        }

        return beaconResponseMapper.mapEntityToDto(br, false);
    }

    @Override
    public Collection<BeaconResponseDto> queryBeacons(Collection<String> beaconIds, String chrom, Long pos, String allele, String ref, UserDto onBehalfOf, String ip) throws ClassNotFoundException {
        if (beaconIds == null) {
            return new HashSet<>();
        }

        return beaconResponseMapper.mapEntitiesToDtos(queryMultipleBeacons(beaconIds, chrom, pos, allele, ref, onBehalfOf, ip), false);
    }

    @Override
    public Collection<BeaconResponseDto> queryAll(String chrom, Long pos, String allele, String ref, UserDto onBehalfOf, String ip) throws ClassNotFoundException {
        return beaconResponseMapper.mapEntitiesToDtos(queryMultipleBeacons(null, chrom, pos, allele, ref, onBehalfOf, ip), false);
    }

}
