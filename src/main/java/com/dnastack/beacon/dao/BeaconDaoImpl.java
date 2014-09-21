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
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Basic static mapper and holder of beacons and services.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@ApplicationScoped
public class BeaconDaoImpl implements BeaconDao, Serializable {

    private static final long serialVersionUID = 5L;

    private Set<Beacon> beacons;
    private Multimap<BeaconTo, BeaconTo> aggregations;

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

    private void setUpBeacons() {
        this.beacons = new HashSet<>();

        // set up bob
        Beacon bob = new Beacon("bob", "Beacon of Beacons", null, true);

        // set up regular beacons
        Beacon clinvar = new Beacon("clinvar", "ClinVar", ucscService, true);
        Beacon uniprot = new Beacon("uniprot", "UniProt", ucscService, true);
        Beacon lovd = new Beacon("lovd", "Leiden Open Variation", ucscService, true);
        Beacon ebi = new Beacon("ebi", "EMBL-EBI", ebiService, true);
        Beacon ncbi = new Beacon("ncbi", "NCBI", ncbiService, true);
        Beacon wtsi = new Beacon("wtsi", "Wellcome Trust Sanger Institute", wtsiService, true);
        Beacon amplab = new Beacon("amplab", "AMPLab", ampLabService, true);
        Beacon kaviar = new Beacon("kaviar", "Known VARiants", kaviarService, true);

        // set up aggregators
        Beacon pathogenic = new Beacon("pathogenic", "Pathogenic", null, true);
        lovd.addAggregator(pathogenic);
        clinvar.addAggregator(pathogenic);

        // add beacons ot collection
        beacons.add(bob);
        beacons.add(pathogenic);

        beacons.add(clinvar);
        beacons.add(uniprot);
        beacons.add(lovd);
        beacons.add(ebi);
        beacons.add(ncbi);
        beacons.add(wtsi);
        beacons.add(amplab);
        beacons.add(kaviar);

        // point all regular beacons to bob
        for (Beacon b : beacons) {
            if (b.getProcessor() != null) {
                b.addAggregator(bob);
            }
        }
    }

    private void computeAggregations() {
        aggregations = HashMultimap.create();

        for (Beacon b : beacons) {
            if (b.getProcessor() != null) {
                for (Beacon parent : b.getAggregators()) {
                    if (parent.getProcessor() == null) {
                        aggregations.put(new BeaconTo(parent), new BeaconTo(b));
                    }
                }
            }
        }
    }

    @PostConstruct
    private void init() {
        setUpBeacons();
        computeAggregations();
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
    public Collection<BeaconTo> getAllBeacons() {
        Set<BeaconTo> res = new HashSet<>();

        return res;
    }

    @Override
    public Collection<BeaconTo> getAggregatingBeacons() {
        Set<BeaconTo> res = new HashSet<>();
        for (Beacon b : beacons) {
            if (b.getProcessor() == null) {
                res.add(new BeaconTo(b));
            }
        }

        return res;
    }

    @Override
    public Collection<BeaconTo> getRegularBeacons() {
        Set<BeaconTo> res = new HashSet<>();
        for (Beacon b : beacons) {
            if (b.getProcessor() != null) {
                res.add(new BeaconTo(b));
            }
        }
        return res;
    }

    @Override
    public Collection<BeaconTo> getVisibleBeacons() {
        Set<BeaconTo> res = new HashSet<>();
        for (Beacon b : beacons) {
            if (b.isVisible()) {
                res.add(new BeaconTo(b));
            }
        }
        return res;
    }

    @Override
    public Collection<BeaconTo> getHiddenBeacons() {
        Set<BeaconTo> res = new HashSet<>();
        for (Beacon b : beacons) {
            if (!b.isVisible()) {
                res.add(new BeaconTo(b));
            }
        }
        return res;
    }

    @Override
    public BeaconTo getBeacon(String beaconId) {
        if (beaconId == null) {
            throw new NullPointerException("beaconId");
        }

        return new BeaconTo(findBeacon(beaconId));
    }

    @Override
    public BeaconTo getVisibleBeacon(String beaconId) {
        if (beaconId == null) {
            throw new NullPointerException("beaconId");
        }

        Beacon b = findBeacon(beaconId);
        if (b == null) {
            return null;
        }

        return b.isVisible() ? new BeaconTo(b) : null;
    }

    @Override
    public BeaconProcessor getProcessor(BeaconTo b) {
        return findBeacon(b.getId()).getProcessor();
    }

    @Override
    public boolean isAgregator(BeaconTo b) {
        return findBeacon(b.getId()).getProcessor() == null;
    }

    @Override
    public Collection<BeaconTo> getAggregators(BeaconTo b) {
        Set<Beacon> bs = findBeacon(b.getId()).getAggregators();

        Set<BeaconTo> res = new HashSet<>();
        for (Beacon a : bs) {
            res.add(new BeaconTo(a));
        }

        return res;
    }

    @Override
    public Collection<BeaconTo> getAggregatees(BeaconTo b) {
        return Collections.unmodifiableCollection(aggregations.get(b));
    }

}
