package com.dnastack.beacon.rest;

import com.dnastack.beacon.core.AllBeacons;
import com.dnastack.beacon.core.Beacon;
import com.dnastack.beacon.core.BeaconProvider;
import com.dnastack.beacon.core.BeaconResponse;
import com.dnastack.beacon.core.Bob;
import com.dnastack.beacon.core.Query;
import com.dnastack.beacon.util.QueryUtils;
import java.util.ArrayList;
import java.util.List;
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
 * @author mcupak
 */
@Path("/query")
public class QueryBeaconResource {

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

    @GET
    @Produces("application/json")
    @Path("/bob")
    public BeaconResponse queryBob(@QueryParam("chrom") String chrom, @QueryParam("pos") Long pos, @QueryParam("allele") String allele) {
        Query q = queryUtils.normalizeQuery(new Query(chrom, pos, allele));
        BeaconResponse br = new BeaconResponse(bob, q, null);

        if (!queryUtils.isQueryValid(q)) {
            return br;
        }
        br.setResponse(false);

        // TODO: parallelize
        for (Beacon b : beacons) {
            Boolean res = beaconProvider.getService(b).executeQuery(b, q).getResponse();
            if (res != null && res) {
                br.setResponse(true);
            }
        }

        return br;
    }

    @GET
    @Produces("application/json")
    @Path("/all")
    public List<BeaconResponse> queryAll(@QueryParam("chrom") String chrom, @QueryParam("pos") Long pos, @QueryParam("allele") String allele) {
        List<BeaconResponse> brs = new ArrayList<>();
        for (Beacon b : beacons) {
            brs.add(queryBeacon(b.getId(), chrom, pos, allele));
        }
        return brs;
    }

    @GET
    @Produces("application/json")
    @Path("/{beaconId}")
    public BeaconResponse queryBeacon(@PathParam("beaconId") String beaconId, @QueryParam("chrom") String chrom, @QueryParam("pos") Long pos, @QueryParam("allele") String allele) {
        Query q = queryUtils.normalizeQuery(new Query(chrom, pos, allele));

        Beacon b = beaconProvider.getBeacon(beaconId);
        if (b == null) {
            // nonexisting beaconId param specified
            return new BeaconResponse(new Beacon(null, "invalid beacon"), q, null);
        }

        if (!queryUtils.isQueryValid(q)) {
            return new BeaconResponse(b, q, null);
        }

        return beaconProvider.getService(b).executeQuery(b, q);
    }
}
