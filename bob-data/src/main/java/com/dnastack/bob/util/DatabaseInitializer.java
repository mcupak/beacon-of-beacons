/*
 * The MIT License
 *
 * Copyright 2015 DNAstack.
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
package com.dnastack.bob.util;

import com.dnastack.bob.persistence.api.BeaconDao;
import com.dnastack.bob.persistence.api.OrganizationDao;
import com.dnastack.bob.persistence.entity.Beacon;
import com.dnastack.bob.persistence.entity.Organization;
import com.dnastack.bob.processor.AmpLabBeaconProcessor;
import com.dnastack.bob.processor.BeaconizerIntegerChromosomeBeaconProcessor;
import com.dnastack.bob.processor.BeaconizerStringChromosomeBeaconProcessor;
import com.dnastack.bob.processor.CafeVariomeBeaconProcessor;
import com.dnastack.bob.processor.EbiBeaconProcessor;
import com.dnastack.bob.processor.KaviarBeaconProcessor;
import com.dnastack.bob.processor.NcbiBeaconProcessor;
import com.dnastack.bob.processor.UcscBeaconProcessor;
import com.dnastack.bob.processor.WtsiBeaconProcessor;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Singleton
@Startup
public class DatabaseInitializer {

    @Inject
    private OrganizationDao organizationDao;

    @Inject
    private BeaconDao beaconDao;

    @Inject
    private ProcessorResolver pr;

    @PostConstruct
    public void init() {
        if (beaconDao.countAll() == 0) {
            Set<Beacon> beacons = new HashSet<>();

            // set up bob
            Organization ga4gh = new Organization();
            ga4gh.setId("ga4gh");
            ga4gh.setName("Global Alliance for Genomics and Health");
            organizationDao.save(ga4gh);
            Beacon bob = new Beacon("bob", "Beacon of Beacons", ga4gh, null, true);
            beaconDao.save(bob);

            // set up regular beacons
            Organization ucsc = new Organization();
            ucsc.setId("ucsc");
            ucsc.setName("UCSC");
            organizationDao.save(ucsc);
            Beacon clinvarBeacon = new Beacon("clinvar", "ClinVar", ucsc, pr.getProcessorId(UcscBeaconProcessor.class), true);
            beaconDao.save(clinvarBeacon);
            Beacon uniprotBeacon = new Beacon("uniprot", "UniProt", ucsc, pr.getProcessorId(UcscBeaconProcessor.class), true);
            beaconDao.save(uniprotBeacon);
            Beacon lovdBeacon = new Beacon("lovd", "Leiden Open Variation", ucsc, pr.getProcessorId(UcscBeaconProcessor.class), true);
            beaconDao.save(lovdBeacon);

            Organization ebi = new Organization();
            ebi.setId("ebi");
            ebi.setName("EBI");
            organizationDao.save(ebi);
            Beacon ebiBeacon = new Beacon("ebi", "EMBL-EBI", ebi, pr.getProcessorId(EbiBeaconProcessor.class), true);
            beaconDao.save(ebiBeacon);

            Organization ncbi = new Organization();
            ncbi.setId("ncbi");
            ncbi.setName("NCBI");
            organizationDao.save(ncbi);
            Beacon ncbiBeacon = new Beacon("ncbi", "NCBI", ncbi, pr.getProcessorId(NcbiBeaconProcessor.class), true);
            beaconDao.save(ncbiBeacon);

            Organization wtsi = new Organization();
            wtsi.setId("wtsi");
            wtsi.setName("WTSI");
            organizationDao.save(wtsi);
            Beacon wtsiBeacon = new Beacon("wtsi", "Wellcome Trust Sanger Institute", wtsi, pr.getProcessorId(WtsiBeaconProcessor.class), true);
            beaconDao.save(wtsiBeacon);

            Organization amplab = new Organization();
            amplab.setId("amplab");
            amplab.setName("AMPLab, University of California");
            organizationDao.save(amplab);
            Beacon amplabBeacon = new Beacon("amplab", "AMPLab", amplab, pr.getProcessorId(AmpLabBeaconProcessor.class), true);
            beaconDao.save(amplabBeacon);

            Organization isb = new Organization();
            isb.setId("isb");
            isb.setName("Institute for Systems Biology");
            organizationDao.save(isb);
            Beacon kaviar = new Beacon("kaviar", "Known VARiants", isb, pr.getProcessorId(KaviarBeaconProcessor.class), true);
            beaconDao.save(kaviar);

            Organization google = new Organization();
            google.setId("google");
            google.setName("Google");
            organizationDao.save(google);
            Beacon platinum = new Beacon("platinum", "Illumina Platinum Genomes", google, pr.getProcessorId(BeaconizerStringChromosomeBeaconProcessor.class), true);
            beaconDao.save(platinum);
            Beacon thousandGenomes = new Beacon("thousandgenomes", "1000 Genomes Project", google, pr.getProcessorId(BeaconizerIntegerChromosomeBeaconProcessor.class), true);
            beaconDao.save(thousandGenomes);
            Beacon thousandGenomesPhase3 = new Beacon("thousandgenomes-phase3", "1000 Genomes Project - Phase 3", google, pr.getProcessorId(BeaconizerIntegerChromosomeBeaconProcessor.class), true);
            beaconDao.save(thousandGenomesPhase3);

            Organization curoverse = new Organization();
            curoverse.setId("curoverse");
            curoverse.setName("Curoverse");
            organizationDao.save(curoverse);
            Beacon curoverseBeacon = new Beacon("curoverse", "PGP", curoverse, pr.getProcessorId(BeaconizerIntegerChromosomeBeaconProcessor.class), true);
            beaconDao.save(curoverseBeacon);
            Beacon curoverseRefBeacon = new Beacon("curoverse-ref", "GA4GH Example Data", curoverse, pr.getProcessorId(BeaconizerIntegerChromosomeBeaconProcessor.class), true);
            beaconDao.save(curoverseRefBeacon);

            Organization leicester = new Organization();
            leicester.setId("leicester");
            leicester.setName("University of Leicester");
            organizationDao.save(leicester);
            Beacon cafeVariomeCentral = new Beacon("cafe-central", "Cafe Variome Central", leicester, pr.getProcessorId(CafeVariomeBeaconProcessor.class), true);
            beaconDao.save(cafeVariomeCentral);
            Beacon cafeCardioKit = new Beacon("cafe-cardiokit", "Cafe CardioKit", leicester, pr.getProcessorId(CafeVariomeBeaconProcessor.class), true);
            beaconDao.save(cafeCardioKit);

//            // set up aggregators
//            Beacon google = new Beacon("google", "Google Genomics Public Data", null, true, "Google");
//            platinum.addAggregator(google);
//            thousandGenomes.addAggregator(google);
//            thousandGenomesPhase3.addAggregator(google);
//
//            Beacon cafeVariome = new Beacon("cafe-variome", "Cafe Variome", null, true, "University of Leicester");
//            cafeVariomeCentral.addAggregator(cafeVariome);
//            cafeCardioKit.addAggregator(cafeVariome);
//
//            Beacon broad = new Beacon("broad", "Broad Institute", broadInstituteService, true, "Broad Institute");
//
//            Beacon icgc = new Beacon("icgc", "ICGC", icgcService, true, "Ontario Institute for Cancer Research");
//
//            // add beacons ot collection
//            beacons.add(bob);
//
//            beacons.add(clinvarBeacon);
//            beacons.add(uniprotBeacon);
//            beacons.add(lovdBeacon);
//            beacons.add(ebiBeacon);
//            beacons.add(ncbiBeacon);
//            beacons.add(google);
//            beacons.add(google);
//            beacons.add(kaviar);
//
//            beacons.add(google);
//            beacons.add(platinum);
//            beacons.add(thousandGenomes);
//            beacons.add(thousandGenomesPhase3);
//
//            beacons.add(curoverse);
//            beacons.add(curoverseRefBeacon);
//
//            beacons.add(cafeVariome);
//            beacons.add(cafeVariomeCentral);
//            beacons.add(cafeCardioKit);
//
//            beacons.add(broad);
//
//            beacons.add(icgc);
//
//            // point all regular beacons to bob
//            for (Beacon b : beacons) {
//                if (b.getProcessor() != null) {
//                    b.addAggregator(bob);
//                }
//            }
        }
    }
}
