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
@Path("/responses")
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

    /**
     * Query the beacon of beacons.
     *
     * @param chrom chromosome
     * @param pos position
     * @param allele allele
     * @return list of beacon responses
     */
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

    /**
     * Query a given beacon
     *
     * @param beaconId beacon to query
     * @param chrom chromosome
     * @param pos position
     * @param allele allele
     * @return list of beacon responses
     */
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

    /**
     * Query all the beacons.
     *
     * @param chrom chromosome
     * @param pos position
     * @param allele allele
     * @return list of beacon responses
     */
    public List<BeaconResponse> queryAll(String chrom, Long pos, String allele) {
        List<BeaconResponse> brs = new ArrayList<>();
        for (Beacon b : beacons) {
            brs.add(queryBeacon(b.getId(), chrom, pos, allele));
        }
        return brs;
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
    @GET
    @Produces("application/json")
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
