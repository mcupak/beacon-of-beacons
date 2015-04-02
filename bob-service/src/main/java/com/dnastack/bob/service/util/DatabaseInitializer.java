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
package com.dnastack.bob.service.util;

import com.dnastack.bob.persistence.api.BeaconDao;
import com.dnastack.bob.persistence.api.OrganizationDao;
import com.dnastack.bob.persistence.entity.Beacon;
import com.dnastack.bob.persistence.entity.Organization;
import com.dnastack.bob.service.processor.impl.AmpLabBeaconProcessor;
import com.dnastack.bob.service.processor.impl.BeaconizerIntegerChromosomeBeaconProcessor;
import com.dnastack.bob.service.processor.impl.BeaconizerStringChromosomeBeaconProcessor;
import com.dnastack.bob.service.processor.impl.BroadInstituteBeaconProcessor;
import com.dnastack.bob.service.processor.impl.CafeVariomeBeaconProcessor;
import com.dnastack.bob.service.processor.impl.EbiBeaconProcessor;
import com.dnastack.bob.service.processor.impl.IcgcBeaconProcessor;
import com.dnastack.bob.service.processor.impl.KaviarBeaconProcessor;
import com.dnastack.bob.service.processor.impl.NcbiBeaconProcessor;
import com.dnastack.bob.service.processor.impl.UcscBeaconProcessor;
import com.dnastack.bob.service.processor.impl.UcscV2BeaconProcessor;
import com.dnastack.bob.service.processor.impl.WtsiBeaconProcessor;
import com.dnastack.bob.service.processor.util.ProcessorResolver;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.jboss.logging.Logger;

/**
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Singleton
@Startup
@Transactional
public class DatabaseInitializer {
    
    @Inject
    private OrganizationDao organizationDao;
    
    @Inject
    private BeaconDao beaconDao;
    
    @Inject
    private ProcessorResolver pr;
    
    @Inject
    private Logger logger;

    // TODO: remove this methods as it is dangerous and does tnot handle foreign keys well
    private void clean() {
        logger.debug("Cleaning DB...");
        List<Beacon> beacons = new ArrayList<>();
        do {
            beacons = beaconDao.findAll();
            for (Beacon b : beacons) {
                try {
                    beaconDao.delete(b.getId());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } while (!beacons.isEmpty());
        
        List<Organization> orgs = new ArrayList<>();
        do {
            orgs = organizationDao.findAll();
            for (Organization b : orgs) {
                try {
                    organizationDao.delete(b.getId());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } while (!orgs.isEmpty());
    }
    
    private void insertInitialData() {
        logger.debug("Initializing DB...");
        try {
            // set up bob
            Organization ga4gh = new Organization();
            ga4gh.setId("ga4gh");
            ga4gh.setName("Global Alliance for Genomics and Health");
            organizationDao.save(ga4gh);
            Beacon bob = new Beacon();
            bob.setId("bob");
            bob.setName("Beacon of Beacons");
            bob.setOrganization(ga4gh);
            bob.setVisible(true);
            bob.setProcessor(null);
            bob.setEnabled(true);
            bob.setUrl("http://beacon-dnastack.rhcloud.com/");
            beaconDao.save(bob);

            // set up regular beacons
            Organization ucsc = new Organization();
            ucsc.setId("ucsc");
            ucsc.setName("UCSC");
            organizationDao.save(ucsc);
            Beacon ucscBeacon = new Beacon();
            ucscBeacon.setId("ucsc");
            ucscBeacon.setName("UCSC");
            ucscBeacon.setOrganization(ucsc);
            ucscBeacon.setVisible(true);
            ucscBeacon.setProcessor(null);
            ucscBeacon.setEnabled(true);
            ucscBeacon.setUrl("http://hgwdev-max.cse.ucsc.edu/cgi-bin/beacon/query");
            beaconDao.save(ucscBeacon);
            Beacon clinvarBeacon = new Beacon();
            clinvarBeacon.setId("clinvar");
            clinvarBeacon.setName("ClinVar");
            clinvarBeacon.setOrganization(ucsc);
            clinvarBeacon.setVisible(true);
            clinvarBeacon.setProcessor(pr.getProcessorId(UcscBeaconProcessor.class));
            clinvarBeacon.setEnabled(true);
            clinvarBeacon.setUrl("http://hgwdev-max.cse.ucsc.edu/cgi-bin/beacon/query");
            beaconDao.save(clinvarBeacon);
            Beacon uniprotBeacon = new Beacon();
            uniprotBeacon.setId("uniprot");
            uniprotBeacon.setName("UniProt");
            uniprotBeacon.setOrganization(ucsc);
            uniprotBeacon.setVisible(true);
            uniprotBeacon.setProcessor(pr.getProcessorId(UcscBeaconProcessor.class));
            uniprotBeacon.setEnabled(true);
            uniprotBeacon.setUrl("http://hgwdev-max.cse.ucsc.edu/cgi-bin/beacon/query");
            beaconDao.save(uniprotBeacon);
            Beacon lovdBeacon = new Beacon();
            lovdBeacon.setId("lovd");
            lovdBeacon.setName("Leiden Open Variation");
            lovdBeacon.setOrganization(ucsc);
            lovdBeacon.setVisible(true);
            lovdBeacon.setProcessor(pr.getProcessorId(UcscV2BeaconProcessor.class));
            lovdBeacon.setEnabled(true);
            lovdBeacon.setUrl("http://genome.ucsc.edu/cgi-bin/hgBeacon/query");
            beaconDao.save(lovdBeacon);
            Beacon hgmdBeacon = new Beacon();
            hgmdBeacon.setId("hgmd");
            hgmdBeacon.setName("Biobase - HGMD");
            hgmdBeacon.setOrganization(ucsc);
            hgmdBeacon.setVisible(true);
            hgmdBeacon.setProcessor(pr.getProcessorId(UcscV2BeaconProcessor.class));
            hgmdBeacon.setEnabled(true);
            hgmdBeacon.setUrl("http://genome.ucsc.edu/cgi-bin/hgBeacon/query");
            hgmdBeacon.setDescription("HGMD gives out only positions and ignores alleles.");
            beaconDao.save(hgmdBeacon);
            beaconDao.addRelationship(hgmdBeacon, ucscBeacon);
            beaconDao.addRelationship(clinvarBeacon, ucscBeacon);
            beaconDao.addRelationship(lovdBeacon, ucscBeacon);
            beaconDao.addRelationship(uniprotBeacon, ucscBeacon);
            
            Organization ebi = new Organization();
            ebi.setId("ebi");
            ebi.setName("EBI");
            organizationDao.save(ebi);
            Beacon ebiBeacon = new Beacon();
            ebiBeacon.setId("ebi");
            ebiBeacon.setName("EMBL-EBI");
            ebiBeacon.setOrganization(ebi);
            ebiBeacon.setVisible(true);
            ebiBeacon.setProcessor(pr.getProcessorId(EbiBeaconProcessor.class));
            ebiBeacon.setEnabled(true);
            ebiBeacon.setUrl("http://wwwdev.ebi.ac.uk/eva/webservices/rest/v1/ga4gh/beacon");
            beaconDao.save(ebiBeacon);
            
            Organization ncbi = new Organization();
            ncbi.setId("ncbi");
            ncbi.setName("NCBI");
            organizationDao.save(ncbi);
            Beacon ncbiBeacon = new Beacon();
            ncbiBeacon.setId("ncbi");
            ncbiBeacon.setName("NCBI");
            ncbiBeacon.setOrganization(ncbi);
            ncbiBeacon.setVisible(true);
            ncbiBeacon.setProcessor(pr.getProcessorId(NcbiBeaconProcessor.class));
            ncbiBeacon.setEnabled(true);
            ncbiBeacon.setUrl("http://www.ncbi.nlm.nih.gov/projects/genome/beacon/beacon.cgi");
            beaconDao.save(ncbiBeacon);
            
            Organization wtsi = new Organization();
            wtsi.setId("wtsi");
            wtsi.setName("WTSI");
            organizationDao.save(wtsi);
            Beacon wtsiBeacon = new Beacon();
            wtsiBeacon.setId("wtsi");
            wtsiBeacon.setName("Wellcome Trust Sanger Institute");
            wtsiBeacon.setOrganization(wtsi);
            wtsiBeacon.setVisible(true);
            wtsiBeacon.setProcessor(pr.getProcessorId(WtsiBeaconProcessor.class));
            wtsiBeacon.setEnabled(true);
            wtsiBeacon.setUrl("http://www.sanger.ac.uk/sanger/GA4GH_Beacon");
            beaconDao.save(wtsiBeacon);
            
            Organization amplab = new Organization();
            amplab.setId("amplab");
            amplab.setName("AMPLab, University of California");
            organizationDao.save(amplab);
            Beacon amplabBeacon = new Beacon();
            amplabBeacon.setId("amplab");
            amplabBeacon.setName("AMPLab");
            amplabBeacon.setOrganization(amplab);
            amplabBeacon.setVisible(true);
            amplabBeacon.setProcessor(pr.getProcessorId(AmpLabBeaconProcessor.class));
            amplabBeacon.setEnabled(true);
            amplabBeacon.setUrl("http://beacon.eecs.berkeley.edu/beacon.php");
            beaconDao.save(amplabBeacon);
            
            Organization isb = new Organization();
            isb.setId("isb");
            isb.setName("Institute for Systems Biology");
            organizationDao.save(isb);
            Beacon kaviar = new Beacon();
            kaviar.setId("kaviar");
            kaviar.setName("Known VARiants");
            kaviar.setOrganization(isb);
            kaviar.setVisible(true);
            kaviar.setProcessor(pr.getProcessorId(KaviarBeaconProcessor.class));
            kaviar.setEnabled(true);
            kaviar.setUrl("http://db.systemsbiology.net/kaviar/cgi-pub/beacon");
            beaconDao.save(kaviar);
            
            Organization google = new Organization();
            google.setId("google");
            google.setName("Google");
            organizationDao.save(google);
            Beacon platinum = new Beacon();
            platinum.setId("platinum");
            platinum.setName("Illumina Platinum Genomes");
            platinum.setOrganization(google);
            platinum.setVisible(true);
            platinum.setProcessor(pr.getProcessorId(BeaconizerStringChromosomeBeaconProcessor.class));
            platinum.setEnabled(true);
            platinum.setUrl("http://dnastack.com/p/beacon/");
            beaconDao.save(platinum);
            Beacon thousandGenomes = new Beacon();
            thousandGenomes.setId("thousandgenomes");
            thousandGenomes.setName("1000 Genomes Project");
            thousandGenomes.setOrganization(google);
            thousandGenomes.setVisible(true);
            thousandGenomes.setProcessor(pr.getProcessorId(BeaconizerIntegerChromosomeBeaconProcessor.class));
            thousandGenomes.setEnabled(true);
            thousandGenomes.setUrl("http://dnastack.com/p/beacon/");
            beaconDao.save(thousandGenomes);
            Beacon thousandGenomesPhase3 = new Beacon();
            thousandGenomesPhase3.setId("thousandgenomes-phase3");
            thousandGenomesPhase3.setName("1000 Genomes Project - Phase 3");
            thousandGenomesPhase3.setOrganization(google);
            thousandGenomesPhase3.setVisible(true);
            thousandGenomesPhase3.setProcessor(pr.getProcessorId(BeaconizerIntegerChromosomeBeaconProcessor.class));
            thousandGenomesPhase3.setEnabled(true);
            thousandGenomesPhase3.setUrl("http://dnastack.com/p/beacon/");
            beaconDao.save(thousandGenomesPhase3);
            
            Organization curoverse = new Organization();
            curoverse.setId("curoverse");
            curoverse.setName("Curoverse");
            organizationDao.save(curoverse);
            Beacon curoverseBeacon = new Beacon();
            curoverseBeacon.setId("curoverse");
            curoverseBeacon.setName("PGP");
            curoverseBeacon.setOrganization(curoverse);
            curoverseBeacon.setVisible(true);
            curoverseBeacon.setProcessor(pr.getProcessorId(BeaconizerIntegerChromosomeBeaconProcessor.class));
            curoverseBeacon.setEnabled(true);
            curoverseBeacon.setUrl(null);
            beaconDao.save(curoverseBeacon);
            Beacon curoverseRefBeacon = new Beacon();
            curoverseRefBeacon.setId("curoverse-ref");
            curoverseRefBeacon.setName("GA4GH Example Data");
            curoverseRefBeacon.setOrganization(curoverse);
            curoverseRefBeacon.setVisible(true);
            curoverseRefBeacon.setProcessor(pr.getProcessorId(BeaconizerIntegerChromosomeBeaconProcessor.class));
            curoverseRefBeacon.setEnabled(true);
            curoverseRefBeacon.setUrl("http://dnastack.com/p/beacon/");
            beaconDao.save(curoverseRefBeacon);
            
            Organization leicester = new Organization();
            leicester.setId("leicester");
            leicester.setName("University of Leicester");
            organizationDao.save(leicester);
            Beacon cafeVariomeCentral = new Beacon();
            cafeVariomeCentral.setId("cafe-central");
            cafeVariomeCentral.setName("Cafe Variome Central");
            cafeVariomeCentral.setOrganization(leicester);
            cafeVariomeCentral.setVisible(true);
            cafeVariomeCentral.setProcessor(pr.getProcessorId(CafeVariomeBeaconProcessor.class));
            cafeVariomeCentral.setEnabled(true);
            cafeVariomeCentral.setUrl("http://beacon.cafevariome.org/query");
            beaconDao.save(cafeVariomeCentral);
            Beacon cafeCardioKit = new Beacon();
            cafeCardioKit.setId("cafe-cardiokit");
            cafeCardioKit.setName("Cafe CardioKit");
            cafeCardioKit.setOrganization(leicester);
            cafeCardioKit.setVisible(true);
            cafeCardioKit.setProcessor(pr.getProcessorId(CafeVariomeBeaconProcessor.class));
            cafeCardioKit.setEnabled(true);
            cafeCardioKit.setUrl("http://beacon.cafevariome.org/query");
            beaconDao.save(cafeCardioKit);
            
            Organization broadInstitute = new Organization();
            broadInstitute.setId("broad");
            broadInstitute.setName("Broad Institute");
            organizationDao.save(broadInstitute);
            Beacon broad = new Beacon();
            broad.setId("broad");
            broad.setName("Broad Institute");
            broad.setOrganization(broadInstitute);
            broad.setVisible(true);
            broad.setProcessor(pr.getProcessorId(BroadInstituteBeaconProcessor.class));
            broad.setEnabled(true);
            broad.setUrl("http://broad-beacon.broadinstitute.org:8090/dev/beacon/query");
            beaconDao.save(broad);
            
            Organization icgc = new Organization();
            icgc.setId("icgc");
            icgc.setName("Ontario Institute for Cancer Research");
            organizationDao.save(icgc);
            Beacon icgcBeacon = new Beacon();
            icgcBeacon.setId("icgc");
            icgcBeacon.setName("ICGC");
            icgcBeacon.setOrganization(icgc);
            icgcBeacon.setVisible(true);
            icgcBeacon.setProcessor(pr.getProcessorId(IcgcBeaconProcessor.class));
            icgcBeacon.setEnabled(true);
            icgcBeacon.setUrl("https://dcc.icgc.org/api/v1/beacon/query");
            beaconDao.save(icgcBeacon);
            
            Beacon googleBeacon = new Beacon();
            googleBeacon.setId("google");
            googleBeacon.setName("Google Genomics Public Data");
            googleBeacon.setOrganization(google);
            googleBeacon.setVisible(true);
            googleBeacon.setProcessor(null);
            googleBeacon.setEnabled(true);
            googleBeacon.setUrl("http://dnastack.com/p/beacon/");
            beaconDao.save(googleBeacon);
            beaconDao.addRelationship(platinum, googleBeacon);
            beaconDao.update(platinum);
            beaconDao.addRelationship(thousandGenomes, googleBeacon);
            beaconDao.update(thousandGenomes);
            beaconDao.addRelationship(thousandGenomesPhase3, googleBeacon);
            beaconDao.update(thousandGenomesPhase3);
            
            Beacon cafeVariome = new Beacon();
            cafeVariome.setId("cafe-variome");
            cafeVariome.setName("Cafe Variome");
            cafeVariome.setOrganization(leicester);
            cafeVariome.setVisible(true);
            cafeVariome.setProcessor(null);
            cafeVariome.setEnabled(true);
            cafeVariome.setUrl("http://beacon.cafevariome.org/query");
            beaconDao.save(cafeVariome);
            beaconDao.addRelationship(cafeVariomeCentral, cafeVariome);
            beaconDao.update(cafeVariomeCentral);
            beaconDao.addRelationship(cafeCardioKit, cafeVariome);
            beaconDao.update(cafeCardioKit);

            // point all regular beacons to bob
            List<Beacon> beacons = beaconDao.findAll();
            for (Beacon b : beacons) {
                if (b.getProcessor() != null) {
                    beaconDao.addRelationship(b, bob);
                    beaconDao.update(b);
                }
            }
        } catch (Exception ex) {
            // failed to initialize, continue with an empty DB
            ex.printStackTrace();
        }
    }
    
    @PostConstruct
    public void init() {
        clean();
        insertInitialData();
    }
}
