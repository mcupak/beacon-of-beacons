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
package com.dnastack.beacon.dao;

import com.dnastack.beacon.dto.BeaconTo;
import com.dnastack.beacon.entity.Beacon;
import com.dnastack.beacon.processor.AmpLab;
import com.dnastack.beacon.processor.BeaconProcessor;
import com.dnastack.beacon.processor.Ebi;
import com.dnastack.beacon.processor.Kaviar;
import com.dnastack.beacon.processor.Ncbi;
import com.dnastack.beacon.processor.Ucsc;
import com.dnastack.beacon.processor.Wtsi;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Basic static mapper and holder of beacons and services.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Stateless
@LocalBean
public class BeaconDaoImpl implements BeaconDao, Serializable {

    private static final long serialVersionUID = 5L;

    private Set<Beacon> beacons;

    @Inject
    @Ucsc
    private BeaconProcessor ucscService;

    @Inject
    @Ebi
    private BeaconProcessor ebiService;

    @Inject
    @Ncbi
    private BeaconProcessor ncbiService;

    @Inject
    @Wtsi
    private BeaconProcessor wtsiService;

    @Inject
    @AmpLab
    private BeaconProcessor ampLabService;

    @Inject
    @Kaviar
    private BeaconProcessor kaviarService;

    @PostConstruct
    private void init() {
        this.beacons = new HashSet<>();
        beacons.add(new Beacon("clinvar", "NCBI ClinVar", ucscService));
        beacons.add(new Beacon("uniprot", "UniProt", ucscService));
        beacons.add(new Beacon("lovd", "Leiden Open Variation", ucscService));
        beacons.add(new Beacon("ebi", "EMBL-EBI", ebiService));
        beacons.add(new Beacon("ncbi", "NCBI", ncbiService));
        beacons.add(new Beacon("wtsi", "Wellcome Trust Sanger Institute", wtsiService));
        beacons.add(new Beacon("amplab", "AMPLab", ampLabService));
        beacons.add(new Beacon("kaviar", "Known VARiants", kaviarService));
    }

    @Override
    public Set<BeaconTo> getAllBeacons() {
        Set<BeaconTo> res = new HashSet<>();
        for (Beacon b : beacons) {
            res.add(new BeaconTo(b));
        }

        return res;
    }

    private Beacon findBeacon(String beaconId) {
        for (Beacon b : beacons) {
            if (b.getId().equals(beaconId)) {
                return b;
            }
        }

        return null;
    }

    @Override
    public BeaconTo getBeacon(String beaconId) {
        if (beaconId == null) {
            throw new NullPointerException("beaconId");
        }

        return new BeaconTo(findBeacon(beaconId));
    }

    @Override
    public BeaconTo getBob() {
        return new BeaconTo("bob", "beacon of beacons");
    }

    @Override
    public BeaconProcessor getProcessor(BeaconTo b) {
        return findBeacon(b.getId()).getProcessor();
    }

}
