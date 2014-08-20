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
import com.dnastack.beacon.core.Bob;
import com.dnastack.beacon.log.Logged;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * Query rest resource.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Path("/beacons")
public class BeaconResource {

    @Inject
    private BeaconProvider beaconProvider;

    @Inject
    @Bob
    private Beacon bob;

    @Inject
    @AllBeacons
    private Set<Beacon> beacons;

    /**
     * Shows beacon of beacons details.
     *
     * @return bob
     */
    @Logged
    @GET
    @Produces({"application/json", "application/xml"})
    @Path("/bob")
    public Beacon showBob() {
        return bob;
    }

    /**
     * Shows beacon details.
     *
     * @param beaconId id of the beacon
     * @return beacon
     */
    @Logged
    @GET
    @Produces({"application/json", "application/xml"})
    @Path("/{beaconId}")
    public Beacon showBeacon(@PathParam("beaconId") String beaconId) {
        return beaconProvider.getBeacon(beaconId);
    }

    /**
     * Shows all the beacons.
     *
     * @return set of beacons
     */
    @Logged
    public Set<Beacon> showAll() {
        return Collections.unmodifiableSet(beacons);
    }

    /**
     * Shows all the beacons or a specific beacon as determined by a param.
     *
     * @param beaconId beacon ID
     * @return set of beacons
     */
    @Logged
    @GET
    @Produces({"application/json", "application/xml"})
    public Set<Beacon> show(@QueryParam("beacon") String beaconId) {
        Set<Beacon> bs = new HashSet<>();
        if (beaconId == null) {
            bs.addAll(showAll());
        } else if (beaconId.equals(bob.getId())) {
            bs.add(showBob());
        } else {
            bs.add(showBeacon(beaconId));
        }

        return bs;
    }

}
