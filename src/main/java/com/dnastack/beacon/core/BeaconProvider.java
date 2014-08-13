/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dnastack.beacon.core;

import com.dnastack.beacon.service.AmpLabBeaconService;
import com.dnastack.beacon.service.EbiBeaconService;
import com.dnastack.beacon.service.NcbiBeaconService;
import com.dnastack.beacon.service.UcscBeaconService;
import com.dnastack.beacon.service.WtsiBeaconService;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Mapper and holder of beacons and services.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Named
@ApplicationScoped
public class BeaconProvider implements Serializable {

    private static final long serialVersionUID = 5L;

    private Map<Beacon, BeaconService> beaconMappping;

    @Inject
    private UcscBeaconService ucscService;

    @Inject
    private EbiBeaconService ebiService;

    @Inject
    private NcbiBeaconService ncbiService;

    @Inject
    private WtsiBeaconService wtsiService;

    @Inject
    private AmpLabBeaconService ampLabService;

    @PostConstruct
    private void initMapping() {
        this.beaconMappping = new HashMap<>();
        beaconMappping.put(new Beacon("clinvar", "NCBI ClinVar"), ucscService);
        beaconMappping.put(new Beacon("uniprot", "UniProt"), ucscService);
        beaconMappping.put(new Beacon("lovd", "Leiden Open Variation"), ucscService);
        beaconMappping.put(new Beacon("ebi", "EMBL-EBI"), ebiService);
        beaconMappping.put(new Beacon("ncbi", "NCBI"), ncbiService);
        beaconMappping.put(new Beacon("wtsi", "Wellcome Trust Sanger Institute"), wtsiService);
        beaconMappping.put(new Beacon("amplab", "AMPLab"), ampLabService);
    }

    /**
     * Retrieve the service implementation compatible with a given beacon.
     *
     * @param beacon beacon
     * @return service
     */
    public BeaconService getService(Beacon beacon) {
        return beaconMappping.get(beacon);
    }

    /**
     * Retrieve all the beacons.
     *
     * @return collection of beacons
     */
    @Produces
    @AllBeacons
    public Set<Beacon> getBeacons() {
        return beaconMappping.keySet();
    }

    /**
     * Find beacon by its ID.
     *
     * @param beaconId beacon ID
     * @return beacon with the given ID
     */
    public Beacon getBeacon(String beaconId) {
        if (beaconId == null) {
            throw new NullPointerException("beaconId");
        }

        for (Beacon b : beaconMappping.keySet()) {
            if (b.getId().equals(beaconId)) {
                return b;
            }
        }

        return null;
    }

    @Produces
    @Bob
    public Beacon getBob() {
        return new Beacon("bob", "beacon of beacons");
    }
}
