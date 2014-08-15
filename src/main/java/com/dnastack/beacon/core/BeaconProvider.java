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
