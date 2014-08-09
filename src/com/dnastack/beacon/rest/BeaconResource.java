package com.dnastack.beacon.rest;

import com.dnastack.beacon.query.Beacon;
import com.dnastack.beacon.query.BeaconResponse;
import com.dnastack.beacon.query.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/beacon")
public class BeaconResource {

    @GET()
    @Produces("application/json")
    @Path("all")
    @QueryParam("")
    public BeaconResponse queryAll() {
        return new BeaconResponse(new Beacon("all", "all"), new Query(), true);
    }
}
