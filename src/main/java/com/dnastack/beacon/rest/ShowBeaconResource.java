package com.dnastack.beacon.rest;

import com.dnastack.beacon.core.AllBeacons;
import com.dnastack.beacon.core.Beacon;
import com.dnastack.beacon.core.BeaconProvider;
import com.dnastack.beacon.core.Bob;
import java.util.Collections;
import java.util.Set;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Query rest resource.
 *
 * @author mcupak
 */
@Path("/show")
public class ShowBeaconResource {

    @Inject
    private BeaconProvider beaconProvider;

    @Inject
    @Bob
    private Beacon bob;

    @Inject
    @AllBeacons
    private Set<Beacon> beacons;

    @GET
    @Produces("application/json")
    @Path("/bob")
    public Beacon showBob() {
        return bob;
    }

    @GET
    @Produces("application/json")
    @Path("/all")
    public Set<Beacon> showAll() {
        return Collections.unmodifiableSet(beacons);
    }

    @GET
    @Produces("application/json")
    @Path("/{beaconId}")
    public Beacon showBeacon(@PathParam("beaconId") String beaconId) {
        return beaconProvider.getBeacon(beaconId);
    }
}
