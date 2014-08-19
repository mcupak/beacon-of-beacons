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
package com.dnastack.beacon.core;

import com.dnastack.beacon.service.AmpLab;
import com.dnastack.beacon.service.Ebi;
import com.dnastack.beacon.service.Ncbi;
import com.dnastack.beacon.service.Ucsc;
import com.dnastack.beacon.service.Wtsi;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 * Mapper and holder of beacons and services.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Stateless
@LocalBean
public class BeaconProvider implements Serializable {

    private static final long serialVersionUID = 5L;

    private Set<Beacon> beacons;

    @Inject
    @Ucsc
    private BeaconService ucscService;

    @Inject
    @Ebi
    private BeaconService ebiService;

    @Inject
    @Ncbi
    private BeaconService ncbiService;

    @Inject
    @Wtsi
    private BeaconService wtsiService;

    @Inject
    @AmpLab
    private BeaconService ampLabService;

    @PostConstruct
    private void initMapping() {
        this.beacons = new HashSet<>();
        beacons.add(new Beacon("clinvar", "NCBI ClinVar"));
        beacons.add(new Beacon("uniprot", "UniProt"));
        beacons.add(new Beacon("lovd", "Leiden Open Variation"));
        beacons.add(new Beacon("ebi", "EMBL-EBI"));
        beacons.add(new Beacon("ncbi", "NCBI"));
        beacons.add(new Beacon("wtsi", "Wellcome Trust Sanger Institute"));
        beacons.add(new Beacon("amplab", "AMPLab"));
    }

    /**
     * Retrieve the service implementation compatible with a given beacon.
     *
     * @param beacon beacon
     * @return service
     */
    public BeaconService getService(Beacon beacon) {
        switch (beacon.getId()) {
            case "clinvar":
                return ucscService;
            case "uniprot":
                return ucscService;
            case "lovd":
                return ucscService;
            case "ebi":
                return ebiService;
            case "ncbi":
                return ncbiService;
            case "wtsi":
                return wtsiService;
            case "amplab":
                return ampLabService;
            default:
                return null;
        }
    }

    /**
     * Retrieve all the beacons.
     *
     * @return collection of beacons
     */
    @Produces
    @AllBeacons
    public Set<Beacon> getBeacons() {
        return Collections.unmodifiableSet(beacons);
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

        for (Beacon b : beacons) {
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
