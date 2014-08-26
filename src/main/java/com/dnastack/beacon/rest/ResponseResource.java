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
package com.dnastack.beacon.rest;

import com.dnastack.beacon.core.AllBeacons;
import com.dnastack.beacon.core.Beacon;
import com.dnastack.beacon.core.BeaconProvider;
import com.dnastack.beacon.core.BeaconResponse;
import com.dnastack.beacon.core.Bob;
import com.dnastack.beacon.core.Query;
import com.dnastack.beacon.log.Logged;
import com.dnastack.beacon.util.QueryUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.inject.Inject;
import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Query rest resource.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Path("/responses")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
public class ResponseResource {

    @Inject
    private BeaconProvider beaconProvider;

    @Inject
    private QueryUtils queryUtils;

    @Inject
    @Bob
    private Beacon bob;

    @Inject
    @AllBeacons
    private Set<Beacon> beacons;

    @Inject
    private Validator validator;

    /**
     * Query the beacon of beacons.
     *
     * @param chrom chromosome
     * @param pos position
     * @param allele allele
     * @return list of beacon responses
     */
    @Logged
    @GET
    @Path("/bob")
    public BeaconResponse queryBob(@QueryParam("chrom") String chrom, @QueryParam("pos") Long pos, @QueryParam("allele") String allele) {
        Query q = queryUtils.normalizeQuery(new Query(chrom, pos, allele));
        BeaconResponse br = new BeaconResponse(bob, q, null);

        if (!validator.validate(q).isEmpty()) {
            return br;
        }
        br.setResponse(false);

        // execute queries in parallel
        List<Future<BeaconResponse>> futures = new ArrayList<>();
        for (Beacon b : beacons) {
            futures.add(beaconProvider.getService(b).executeQuery(b, q));
        }

        // collect results
        for (Future<BeaconResponse> r : futures) {
            Boolean res = null;
            try {
                res = r.get().getResponse();
            } catch (InterruptedException | ExecutionException ex) {
                // ignore, response already null
            }
            if (res != null && res) {
                br.setResponse(true);
            }
        }

        return br;
    }

    /**
     * Query a given beacon
     *
     * @param beaconId beacon to query
     * @param chrom chromosome
     * @param pos position
     * @param allele allele
     * @return list of beacon responses
     */
    @Logged
    @GET
    @Path("/{beaconId}")
    public BeaconResponse queryBeacon(@PathParam("beaconId") String beaconId, @QueryParam("chrom") String chrom, @QueryParam("pos") Long pos, @QueryParam("allele") String allele) {
        Query q = queryUtils.normalizeQuery(new Query(chrom, pos, allele));

        Beacon b = beaconProvider.getBeacon(beaconId);
        if (b == null) {
            // nonexisting beaconId param specified
            return new BeaconResponse(new Beacon(null, "invalid beacon"), q, null);
        }

        BeaconResponse br = new BeaconResponse(b, q, null);
        if (!validator.validate(q).isEmpty()) {
            return br;
        }

        try {
            br = beaconProvider.getService(b).executeQuery(b, q).get();
        } catch (InterruptedException | ExecutionException ex) {
            // ignore
        }
        return br;
    }

    /**
     * Query all the beacons.
     *
     * @param chrom chromosome
     * @param pos position
     * @param allele allele
     * @return collection of beacon responses
     */
    @Logged
    public Collection<BeaconResponse> queryAll(String chrom, Long pos, String allele) {
        Query q = queryUtils.normalizeQuery(new Query(chrom, pos, allele));

        // init to create a response for each beacon even if the query is invalid
        Map<Beacon, BeaconResponse> brs = new HashMap<>();
        for (Beacon b : beacons) {
            brs.put(b, new BeaconResponse(b, q, null));
        }

        // validate query
        if (!validator.validate(q).isEmpty()) {
            return brs.values();
        }

        // execute queries in parallel
        List<Future<BeaconResponse>> futures = new ArrayList<>();
        for (Beacon b : beacons) {
            futures.add(beaconProvider.getService(b).executeQuery(b, q));
        }

        // collect results
        for (Future<BeaconResponse> f : futures) {
            BeaconResponse br = null;
            try {
                br = f.get();
            } catch (InterruptedException | ExecutionException ex) {
                // ignore, response already null
            }
            if (br != null) {
                brs.get(br.getBeacon()).setResponse(br.getResponse());
            }
        }

        return brs.values();
    }

    /**
     * Query all the beacons or a specific beacon as determined by a param.
     *
     * @param beaconId beacon to query (optional)
     * @param chrom chromosome
     * @param pos position
     * @param allele allele
     * @return list of beacon responses
     */
    @Logged
    @GET
    public List<BeaconResponse> query(@QueryParam("beacon") String beaconId, @QueryParam("chrom") String chrom, @QueryParam("pos") Long pos, @QueryParam("allele") String allele) {
        List<BeaconResponse> brs = new ArrayList<>();
        if (beaconId == null) {
            brs.addAll(queryAll(chrom, pos, allele));
        } else if (beaconId.equals(bob.getId())) {
            brs.add(queryBob(chrom, pos, allele));
        } else {
            brs.add(queryBeacon(beaconId, chrom, pos, allele));
        }
        return brs;
    }
}
