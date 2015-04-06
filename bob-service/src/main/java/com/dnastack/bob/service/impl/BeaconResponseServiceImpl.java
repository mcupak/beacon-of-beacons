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
import com.dnastack.bob.persistence.api.QueryDao;
import com.dnastack.bob.persistence.entity.Beacon;
import com.dnastack.bob.persistence.entity.Query;
import com.dnastack.bob.persistence.enumerated.Chromosome;
import com.dnastack.bob.persistence.enumerated.Reference;
import com.dnastack.bob.service.api.BeaconResponseService;
import com.dnastack.bob.service.dto.BeaconResponseTo;
import com.dnastack.bob.service.lrg.Brca;
import com.dnastack.bob.service.lrg.Brca2;
import com.dnastack.bob.service.lrg.LrgConvertor;
import com.dnastack.bob.service.lrg.LrgLocus;
import com.dnastack.bob.service.lrg.LrgReference;
import com.dnastack.bob.service.processor.api.BeaconProcessor;
import com.dnastack.bob.service.processor.api.BeaconResponse;
import com.dnastack.bob.service.processor.util.QueryUtils;
import com.dnastack.bob.service.util.CdiBeanResolver;
import com.dnastack.bob.service.util.Entity2ToConvertor;
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
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.validation.Validator;

import static com.dnastack.bob.service.processor.util.Constants.REQUEST_TIMEOUT;

/**
 * Implementation of a service for managing beacon responses.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Stateless
@LocalBean
@Named
@Dependent
@Transactional
public class BeaconResponseServiceImpl implements BeaconResponseService, Serializable {

    private static final long serialVersionUID = 103L;

    @Inject
    private BeaconDao beaconDao;

    @Inject
    private CdiBeanResolver pr;

    @Inject
    private QueryDao queryDao;

    @Inject
    private Validator validator;

    @Inject
    @Brca
    private LrgConvertor brcaConvertor;

    @Inject
    @Brca2
    private LrgConvertor brca2Convertor;

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
    private Query prepareQuery(String chrom, Long pos, String allele, String ref) {
        Chromosome c = QueryUtils.normalizeChromosome(chrom);
        Reference r = QueryUtils.normalizeReference(ref);

        return new Query(c == null ? null : c, pos, QueryUtils.normalizeAllele(allele), r == null ? null : r);
    }

    private boolean queryNotNormalizedOrValid(Query q, String ref) {
        return (!(ref == null || ref.isEmpty()) && q.getReference() == null) || !validator.validate(q).isEmpty();
    }

    @Asynchronous
    private Future<Boolean> queryBeacon(Beacon b, Query q) throws ClassNotFoundException {
        Boolean total = null;

        if (b.getAggregator()) {
            total = false;

            // execute queries in parallel
            Map<Beacon, Future<Boolean>> futures = new HashMap<>();
            Set<Beacon> children = beaconDao.findDescendants(b, false, true, false, false);
            for (Beacon bt : children) {
                futures.put(bt, ((BeaconProcessor) pr.resolve(bt.getProcessor())).executeQuery(bt, q));
            }

            // collect results
            for (Entry<Beacon, Future<Boolean>> e : futures.entrySet()) {
                Boolean res = null;
                try {
                    res = e.getValue().get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                    // ignore, response already null
                }
                if (res != null && res) {
                    total = true;
                }
            }
        } else {
            try {
                total = ((BeaconProcessor) pr.resolve(b.getProcessor())).executeQuery(b, q).get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                // ignore
            }
        }

        return new AsyncResult<>(total);
    }

    private Map<Beacon, BeaconResponse> setUpBeaconResponseMapForBeacons(Collection<Beacon> bs, Query q) {
        Map<Beacon, BeaconResponse> brs = new HashMap<>();
        if (bs != null) {
            for (Beacon b : bs) {
                if (b != null) {
                    brs.put(b, new BeaconResponse(b, q, null));
                }
            }
        }

        return brs;
    }

    private Map<Beacon, BeaconResponse> setUpBeaconResponseMapForIds(Collection<String> beaconIds, Query q) {
        if (beaconIds == null) {
            return setUpBeaconResponseMapForBeacons(beaconDao.findByVisibility(true), q);
        }

        Set<Beacon> bs = new HashSet<>();
        for (String id : beaconIds) {
            Beacon b = beaconDao.findById(id);
            if (b != null && b.getVisible()) {
                bs.add(b);
            }
        }

        return setUpBeaconResponseMapForBeacons(bs, q);
    }

    private Map<Beacon, BeaconResponse> fillBeaconResponseMap(Map<Beacon, BeaconResponse> brs, Query q) throws ClassNotFoundException {
        // execute queries in parallel
        Map<Beacon, Future<Boolean>> futures = new HashMap<>();
        for (Beacon b : brs.keySet()) {
            futures.put(b, queryBeacon(b, q));
        }

        // collect results
        for (Entry<Beacon, Future<Boolean>> e : futures.entrySet()) {
            Boolean b = null;
            try {
                b = e.getValue().get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                // ignore, response already null
            }
            if (b != null) {
                brs.get(e.getKey()).setResponse(b);
            }
        }

        return brs;
    }

    private Query getQuery(String chrom, Long pos, String allele, String ref) {
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

        return prepareQuery(c, p, a, r);
    }

    private Multimap<Beacon, Beacon> setUpChildrenMultimap(Collection<Beacon> beacons) {
        Multimap<Beacon, Beacon> children = HashMultimap.create();
        for (Beacon b : beacons) {
            if (b.getAggregator()) {
                children.putAll(b, beaconDao.findDescendants(b, false, true, false, false));
            } else {
                children.put(b, b);
            }
        }
        return children;
    }

    private Map<Beacon, BeaconResponse> fillAggregateResponses(Map<Beacon, BeaconResponse> parentResponses, Map<Beacon, BeaconResponse> childrenResponses, Multimap<Beacon, Beacon> children, Query q) {
        Map<Beacon, BeaconResponse> res = new HashMap<>(parentResponses);

        for (Entry<Beacon, BeaconResponse> e : res.entrySet()) {
            BeaconResponse response = e.getValue();
            if (response == null) {
                response = new BeaconResponse(e.getKey(), q, null);
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

    private Collection<BeaconResponse> queryMultipleBeacons(Collection<String> beaconIds, String chrom, Long pos, String allele, String ref) throws ClassNotFoundException {
        Query q = getQuery(chrom, pos, allele, ref);

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
    public BeaconResponseTo queryBeacon(String beaconId, String chrom, Long pos, String allele, String ref) throws ClassNotFoundException {
        Query q = getQuery(chrom, pos, allele, ref);

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
            beacon.setProcessor(null);
            return Entity2ToConvertor.getBeaconResponseTo(new BeaconResponse(beacon, q, null));
        }

        BeaconResponse br = new BeaconResponse(b, q, null);
        if (queryNotNormalizedOrValid(q, ref)) {
            return Entity2ToConvertor.getBeaconResponseTo(br);
        }

        try {
            br.setResponse(queryBeacon(b, q).get(REQUEST_TIMEOUT, TimeUnit.SECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            // ignore, response already null
        }

        return Entity2ToConvertor.getBeaconResponseTo(br);
    }

    @Override
    public Collection<BeaconResponseTo> queryBeacons(Collection<String> beaconIds, String chrom, Long pos, String allele, String ref) throws ClassNotFoundException {
        if (beaconIds == null) {
            return new HashSet<>();
        }

        return Entity2ToConvertor.getBeaconResponseTos(queryMultipleBeacons(beaconIds, chrom, pos, allele, ref));
    }

    @Override
    public Collection<BeaconResponseTo> queryAll(String chrom, Long pos, String allele, String ref) throws ClassNotFoundException {
        return Entity2ToConvertor.getBeaconResponseTos(queryMultipleBeacons(null, chrom, pos, allele, ref));
    }

}
