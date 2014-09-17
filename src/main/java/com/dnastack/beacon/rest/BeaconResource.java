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

import com.dnastack.beacon.dto.BeaconTo;
import com.dnastack.beacon.log.Logged;
import com.dnastack.beacon.service.BeaconService;
import java.net.HttpURLConnection;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

/**
 * Query rest resource.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Path("/beacons")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
public class BeaconResource {

    @Inject
    private BeaconService beaconService;

    /**
     * Shows beacon details.
     *
     * @param beaconId id of the beacon
     *
     * @return beacon
     */
    @GET
    @Path("/{beaconId}")
    public BeaconTo showBeacon(@PathParam("beaconId") String beaconId) {
        BeaconTo b = beaconService.getBeacon(beaconId);
        if (b == null) {
            throw new WebApplicationException(HttpURLConnection.HTTP_NOT_FOUND);
        }
        return b;
    }

    /**
     * Shows all the beacons or a specific beacon as determined by a param.
     *
     * @param beaconId beacon ID
     *
     * @return set of beacons
     */
    @Logged
    @GET
    public Collection<BeaconTo> show(@QueryParam("beacon") String beaconId) {
        Set<BeaconTo> bs = new HashSet<>();
        if (beaconId == null) {
            bs.addAll(beaconService.getAll());
        } else {
            bs.add(beaconService.getBeacon(beaconId));
        }

        return bs;
    }
}
