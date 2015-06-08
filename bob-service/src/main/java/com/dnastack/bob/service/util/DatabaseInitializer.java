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
import com.dnastack.bob.persistence.enumerated.Reference;
import com.dnastack.bob.service.converter.impl.BracketsAlleleConverter;
import com.dnastack.bob.service.converter.impl.ChrPrefixChromosomeConverter;
import com.dnastack.bob.service.converter.impl.EmptyAlleleConverter;
import com.dnastack.bob.service.converter.impl.EmptyChromosomeConverter;
import com.dnastack.bob.service.converter.impl.EmptyPositionConverter;
import com.dnastack.bob.service.converter.impl.EmptyReferenceConverter;
import com.dnastack.bob.service.converter.impl.GrChReferenceConverter;
import com.dnastack.bob.service.converter.impl.IncrementPositionConverter;
import com.dnastack.bob.service.converter.impl.LongNameAlleleConverter;
import com.dnastack.bob.service.converter.impl.LowerCaseChromosomeConverter;
import com.dnastack.bob.service.converter.impl.NumberChromosomeConverter;
import com.dnastack.bob.service.fetcher.impl.GetResponseFetcher;
import com.dnastack.bob.service.fetcher.impl.PostResponseFetcher;
import com.dnastack.bob.service.parser.impl.JsonCafeResponseParser;
import com.dnastack.bob.service.parser.impl.JsonExistsGtResponseParser;
import com.dnastack.bob.service.parser.impl.JsonExistsResponseParser;
import com.dnastack.bob.service.parser.impl.JsonResponseExistsNullAsFalseResponseParser;
import com.dnastack.bob.service.parser.impl.JsonResponseExistsResponseParser;
import com.dnastack.bob.service.parser.impl.StringFoundResponseParser;
import com.dnastack.bob.service.parser.impl.StringTrueFalseResponseParser;
import com.dnastack.bob.service.parser.impl.StringYesNoRefResponseParser;
import com.dnastack.bob.service.parser.impl.StringYesNoResponseParser;
import com.dnastack.bob.service.requester.impl.BeaconChromPosAlleleRequestConstructor;
import com.dnastack.bob.service.requester.impl.ChromPosAlleleRequestConstructor;
import com.dnastack.bob.service.requester.impl.NoParamCustomPayloadRequestConstructor;
import com.dnastack.bob.service.requester.impl.RefChromPosAlleleRequestConstructor;
import java.util.ArrayList;
import java.util.EnumSet;
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
    private CdiBeanResolver cdiResolver;

    @Inject
    private EjbResolver ejbResolver;

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
            // set up regular beacons
            Organization dnastack = new Organization();
            dnastack.setId("dnastack");
            dnastack.setName("DNAstack");
            dnastack.setUrl("http://dnastack.com/");
            organizationDao.save(dnastack);

            Organization ucsc = new Organization();
            ucsc.setId("ucsc");
            ucsc.setName("UCSC");
            organizationDao.save(ucsc);

            Beacon lovdBeacon = new Beacon();
            lovdBeacon.setId("lovd");
            lovdBeacon.setName("Leiden Open Variation");
            lovdBeacon.setOrganization(ucsc);
            lovdBeacon.setVisible(true);
            lovdBeacon.setAggregator(false);
            lovdBeacon.setParser(ejbResolver.getClassId(JsonResponseExistsResponseParser.class));
            lovdBeacon.setFetcher(ejbResolver.getClassId(GetResponseFetcher.class));
            lovdBeacon.setRequester(cdiResolver.getClassId(BeaconChromPosAlleleRequestConstructor.class));
            lovdBeacon.setReferenceConverter(cdiResolver.getClassId(EmptyReferenceConverter.class));
            lovdBeacon.setChromosomeConverter(cdiResolver.getClassId(EmptyChromosomeConverter.class));
            lovdBeacon.setPositionConverter(cdiResolver.getClassId(EmptyPositionConverter.class));
            lovdBeacon.setAlleleConverter(cdiResolver.getClassId(EmptyAlleleConverter.class));
            lovdBeacon.setEnabled(true);
            lovdBeacon.setUrl("http://genome.ucsc.edu/cgi-bin/hgBeacon/query?dataset=%s&chromosome=%s&position=%d&alternateBases=%s");
            lovdBeacon.setSupportedReferences(EnumSet.of(Reference.HG19));
            beaconDao.save(lovdBeacon);
            Beacon hgmdBeacon = new Beacon();
            hgmdBeacon.setId("hgmd");
            hgmdBeacon.setName("Biobase - HGMD");
            hgmdBeacon.setOrganization(ucsc);
            hgmdBeacon.setVisible(true);
            hgmdBeacon.setAggregator(false);
            hgmdBeacon.setParser(ejbResolver.getClassId(JsonResponseExistsResponseParser.class));
            hgmdBeacon.setFetcher(ejbResolver.getClassId(GetResponseFetcher.class));
            hgmdBeacon.setRequester(cdiResolver.getClassId(BeaconChromPosAlleleRequestConstructor.class));
            hgmdBeacon.setReferenceConverter(cdiResolver.getClassId(EmptyReferenceConverter.class));
            hgmdBeacon.setChromosomeConverter(cdiResolver.getClassId(EmptyChromosomeConverter.class));
            hgmdBeacon.setPositionConverter(cdiResolver.getClassId(EmptyPositionConverter.class));
            hgmdBeacon.setAlleleConverter(cdiResolver.getClassId(EmptyAlleleConverter.class));
            hgmdBeacon.setEnabled(true);
            hgmdBeacon.setUrl("http://genome.ucsc.edu/cgi-bin/hgBeacon/query?dataset=%s&chromosome=%s&position=%d&alternateBases=%s");
            hgmdBeacon.setDescription("HGMD gives out only positions and ignores alleles.");
            hgmdBeacon.setSupportedReferences(EnumSet.of(Reference.HG19));
            beaconDao.save(hgmdBeacon);

            Organization ebi = new Organization();
            ebi.setId("ebi");
            ebi.setName("EBI");
            organizationDao.save(ebi);
            Beacon ebiBeacon = new Beacon();
            ebiBeacon.setId("ebi");
            ebiBeacon.setName("EMBL-EBI");
            ebiBeacon.setOrganization(ebi);
            ebiBeacon.setVisible(true);
            ebiBeacon.setAggregator(false);
            ebiBeacon.setParser(ejbResolver.getClassId(JsonExistsResponseParser.class));
            ebiBeacon.setFetcher(ejbResolver.getClassId(GetResponseFetcher.class));
            ebiBeacon.setRequester(cdiResolver.getClassId(ChromPosAlleleRequestConstructor.class));
            ebiBeacon.setReferenceConverter(cdiResolver.getClassId(EmptyReferenceConverter.class));
            ebiBeacon.setChromosomeConverter(cdiResolver.getClassId(NumberChromosomeConverter.class));
            ebiBeacon.setPositionConverter(cdiResolver.getClassId(IncrementPositionConverter.class));
            ebiBeacon.setAlleleConverter(cdiResolver.getClassId(BracketsAlleleConverter.class));
            ebiBeacon.setEnabled(true);
            ebiBeacon.setUrl("http://wwwdev.ebi.ac.uk/eva/webservices/rest/v1/ga4gh/beacon?referenceName=%s&start=%d&allele=%s");
            ebiBeacon.setSupportedReferences(EnumSet.of(Reference.HG19));
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
            ncbiBeacon.setAggregator(false);
            ncbiBeacon.setParser(ejbResolver.getClassId(JsonExistsGtResponseParser.class));
            ncbiBeacon.setFetcher(ejbResolver.getClassId(GetResponseFetcher.class));
            ncbiBeacon.setRequester(cdiResolver.getClassId(RefChromPosAlleleRequestConstructor.class));
            ncbiBeacon.setReferenceConverter(cdiResolver.getClassId(GrChReferenceConverter.class));
            ncbiBeacon.setChromosomeConverter(cdiResolver.getClassId(EmptyChromosomeConverter.class));
            ncbiBeacon.setPositionConverter(cdiResolver.getClassId(IncrementPositionConverter.class));
            ncbiBeacon.setAlleleConverter(cdiResolver.getClassId(EmptyAlleleConverter.class));
            ncbiBeacon.setEnabled(true);
            ncbiBeacon.setUrl("http://www.ncbi.nlm.nih.gov/projects/genome/beacon/beacon.cgi?ref=%s&chrom=%s&pos=%d&allele=%s");
            ncbiBeacon.setSupportedReferences(EnumSet.of(Reference.HG18, Reference.HG19, Reference.HG38));
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
            wtsiBeacon.setAggregator(false);
            wtsiBeacon.setParser(ejbResolver.getClassId(StringYesNoRefResponseParser.class));
            wtsiBeacon.setFetcher(ejbResolver.getClassId(GetResponseFetcher.class));
            wtsiBeacon.setRequester(cdiResolver.getClassId(ChromPosAlleleRequestConstructor.class));
            wtsiBeacon.setReferenceConverter(cdiResolver.getClassId(EmptyReferenceConverter.class));
            wtsiBeacon.setChromosomeConverter(cdiResolver.getClassId(EmptyChromosomeConverter.class));
            wtsiBeacon.setPositionConverter(cdiResolver.getClassId(IncrementPositionConverter.class));
            wtsiBeacon.setAlleleConverter(cdiResolver.getClassId(EmptyAlleleConverter.class));
            wtsiBeacon.setEnabled(true);
            wtsiBeacon.setUrl("http://www.sanger.ac.uk/sanger/GA4GH_Beacon?src=all&chr=%s&pos=%d&all=%s");
            wtsiBeacon.setSupportedReferences(EnumSet.of(Reference.HG19));
            beaconDao.save(wtsiBeacon);

            Organization bio = new Organization();
            bio.setId("bioreference");
            bio.setName("BioReference Laboratories");
            organizationDao.save(bio);
            Beacon bioReference = new Beacon();
            bioReference.setId("bioreference");
            bioReference.setName("BioReference");
            bioReference.setDescription("The content able to be queried by this beacon comes from GeneDx - providing all high-confidence variants detected in both affected and unaffected samples sent for WES testing at GeneDx.");
            bioReference.setOrganization(bio);
            bioReference.setVisible(true);
            bioReference.setAggregator(false);
            bioReference.setParser(ejbResolver.getClassId(StringYesNoRefResponseParser.class));
            bioReference.setFetcher(ejbResolver.getClassId(GetResponseFetcher.class));
            bioReference.setRequester(cdiResolver.getClassId(ChromPosAlleleRequestConstructor.class));
            bioReference.setReferenceConverter(cdiResolver.getClassId(EmptyReferenceConverter.class));
            bioReference.setChromosomeConverter(cdiResolver.getClassId(EmptyChromosomeConverter.class));
            bioReference.setPositionConverter(cdiResolver.getClassId(IncrementPositionConverter.class));
            bioReference.setAlleleConverter(cdiResolver.getClassId(EmptyAlleleConverter.class));
            bioReference.setEnabled(true);
            bioReference.setUrl("http://beacon.bioreference.com/beacon/rest/responses/bioreference?src=all&chrom=%s&pos=%d&allele=%s");
            bioReference.setSupportedReferences(EnumSet.of(Reference.HG19));
            beaconDao.save(bioReference);

            Organization amplab = new Organization();
            amplab.setId("amplab");
            amplab.setName("AMPLab, University of California");
            organizationDao.save(amplab);
            Beacon amplabBeacon = new Beacon();
            amplabBeacon.setId("amplab");
            amplabBeacon.setName("AMPLab");
            amplabBeacon.setOrganization(amplab);
            amplabBeacon.setVisible(true);
            amplabBeacon.setAggregator(false);
            amplabBeacon.setParser(ejbResolver.getClassId(StringFoundResponseParser.class));
            amplabBeacon.setRequester(cdiResolver.getClassId(NoParamCustomPayloadRequestConstructor.class));
            amplabBeacon.setFetcher(ejbResolver.getClassId(PostResponseFetcher.class));
            amplabBeacon.setReferenceConverter(cdiResolver.getClassId(EmptyReferenceConverter.class));
            amplabBeacon.setChromosomeConverter(cdiResolver.getClassId(ChrPrefixChromosomeConverter.class));
            amplabBeacon.setPositionConverter(cdiResolver.getClassId(EmptyPositionConverter.class));
            amplabBeacon.setAlleleConverter(cdiResolver.getClassId(LongNameAlleleConverter.class));
            amplabBeacon.setEnabled(true);
            amplabBeacon.setUrl("http://beacon.eecs.berkeley.edu/beacon.php");
            amplabBeacon.setSupportedReferences(EnumSet.of(Reference.HG18, Reference.HG19, Reference.HG38));
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
            kaviar.setAggregator(false);
            kaviar.setParser(ejbResolver.getClassId(StringYesNoResponseParser.class));
            kaviar.setFetcher(ejbResolver.getClassId(GetResponseFetcher.class));
            kaviar.setRequester(cdiResolver.getClassId(RefChromPosAlleleRequestConstructor.class));
            kaviar.setReferenceConverter(cdiResolver.getClassId(EmptyReferenceConverter.class));
            kaviar.setChromosomeConverter(cdiResolver.getClassId(EmptyChromosomeConverter.class));
            kaviar.setPositionConverter(cdiResolver.getClassId(EmptyPositionConverter.class));
            kaviar.setAlleleConverter(cdiResolver.getClassId(EmptyAlleleConverter.class));
            kaviar.setEnabled(true);
            kaviar.setUrl("http://db.systemsbiology.net/kaviar/cgi-pub/beacon?onebased=0&frz=%s&chr=%s&pos=%d&allele=%s");
            kaviar.setSupportedReferences(EnumSet.of(Reference.HG19, Reference.HG18));
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
            platinum.setAggregator(false);
            platinum.setParser(ejbResolver.getClassId(JsonExistsResponseParser.class));
            platinum.setFetcher(ejbResolver.getClassId(GetResponseFetcher.class));
            platinum.setRequester(cdiResolver.getClassId(BeaconChromPosAlleleRequestConstructor.class));
            platinum.setReferenceConverter(cdiResolver.getClassId(EmptyReferenceConverter.class));
            platinum.setChromosomeConverter(cdiResolver.getClassId(EmptyChromosomeConverter.class));
            platinum.setPositionConverter(cdiResolver.getClassId(EmptyPositionConverter.class));
            platinum.setAlleleConverter(cdiResolver.getClassId(EmptyAlleleConverter.class));
            platinum.setEnabled(true);
            platinum.setUrl("http://dnastack.com/p/beacon/%s?chromosome=chr%s&coordinate=%d&allele=%s");
            platinum.setSupportedReferences(EnumSet.of(Reference.HG19));
            beaconDao.save(platinum);
            Beacon thousandGenomes = new Beacon();
            thousandGenomes.setId("thousandgenomes");
            thousandGenomes.setName("1000 Genomes Project");
            thousandGenomes.setOrganization(google);
            thousandGenomes.setVisible(true);
            thousandGenomes.setAggregator(false);
            thousandGenomes.setParser(ejbResolver.getClassId(JsonExistsResponseParser.class));
            thousandGenomes.setFetcher(ejbResolver.getClassId(GetResponseFetcher.class));
            thousandGenomes.setRequester(cdiResolver.getClassId(BeaconChromPosAlleleRequestConstructor.class));
            thousandGenomes.setReferenceConverter(cdiResolver.getClassId(EmptyReferenceConverter.class));
            thousandGenomes.setChromosomeConverter(cdiResolver.getClassId(EmptyChromosomeConverter.class));
            thousandGenomes.setPositionConverter(cdiResolver.getClassId(EmptyPositionConverter.class));
            thousandGenomes.setAlleleConverter(cdiResolver.getClassId(EmptyAlleleConverter.class));
            thousandGenomes.setEnabled(true);
            thousandGenomes.setUrl("http://dnastack.com/p/beacon/%s?chromosome=%s&coordinate=%d&allele=%s");
            thousandGenomes.setSupportedReferences(EnumSet.of(Reference.HG19));
            beaconDao.save(thousandGenomes);
            Beacon thousandGenomesPhase3 = new Beacon();
            thousandGenomesPhase3.setId("thousandgenomes-phase3");
            thousandGenomesPhase3.setName("1000 Genomes Project - Phase 3");
            thousandGenomesPhase3.setOrganization(google);
            thousandGenomesPhase3.setVisible(true);
            thousandGenomesPhase3.setAggregator(false);
            thousandGenomesPhase3.setParser(ejbResolver.getClassId(JsonExistsResponseParser.class));
            thousandGenomesPhase3.setFetcher(ejbResolver.getClassId(GetResponseFetcher.class));
            thousandGenomesPhase3.setRequester(cdiResolver.getClassId(BeaconChromPosAlleleRequestConstructor.class));
            thousandGenomesPhase3.setReferenceConverter(cdiResolver.getClassId(EmptyReferenceConverter.class));
            thousandGenomesPhase3.setChromosomeConverter(cdiResolver.getClassId(EmptyChromosomeConverter.class));
            thousandGenomesPhase3.setPositionConverter(cdiResolver.getClassId(EmptyPositionConverter.class));
            thousandGenomesPhase3.setAlleleConverter(cdiResolver.getClassId(EmptyAlleleConverter.class));
            thousandGenomesPhase3.setEnabled(true);
            thousandGenomesPhase3.setUrl("http://dnastack.com/p/beacon/%s?chromosome=%s&coordinate=%d&allele=%s");
            thousandGenomesPhase3.setSupportedReferences(EnumSet.of(Reference.HG19));
            beaconDao.save(thousandGenomesPhase3);

            Organization curoverse = new Organization();
            curoverse.setId("curoverse");
            curoverse.setName("Curoverse");
            organizationDao.save(curoverse);
            Beacon curoverseBeacon = new Beacon();
            curoverseBeacon.setId("curoverse");
            curoverseBeacon.setName("Personal Genome Project");
            curoverseBeacon.setOrganization(curoverse);
            curoverseBeacon.setVisible(true);
            curoverseBeacon.setAggregator(false);
            curoverseBeacon.setDescription("http://lightning-dev4.curoverse.com/beacon/people");
            curoverseBeacon.setHomePage("http://lightning-dev4.curoverse.com/beacon/");
            curoverseBeacon.setEmail("nancy@curoverse.com");
            curoverseBeacon.setParser(ejbResolver.getClassId(StringTrueFalseResponseParser.class));
            curoverseBeacon.setFetcher(ejbResolver.getClassId(GetResponseFetcher.class));
            curoverseBeacon.setRequester(cdiResolver.getClassId(RefChromPosAlleleRequestConstructor.class));
            curoverseBeacon.setReferenceConverter(cdiResolver.getClassId(EmptyReferenceConverter.class));
            curoverseBeacon.setChromosomeConverter(cdiResolver.getClassId(NumberChromosomeConverter.class));
            curoverseBeacon.setPositionConverter(cdiResolver.getClassId(EmptyPositionConverter.class));
            curoverseBeacon.setAlleleConverter(cdiResolver.getClassId(EmptyAlleleConverter.class));
            curoverseBeacon.setEnabled(true);
            curoverseBeacon.setUrl("http://lightning-dev4.curoverse.com/beacon/botsearch?refgen=%s&chrom=%s&coord=%d&allele=%s&pop=Personal%%20Genome%%20Project");
            curoverseBeacon.setSupportedReferences(EnumSet.of(Reference.HG19));
            beaconDao.save(curoverseBeacon);

            Organization leicester = new Organization();
            leicester.setId("leicester");
            leicester.setName("University of Leicester");
            organizationDao.save(leicester);
            Beacon cafeVariomeCentral = new Beacon();
            cafeVariomeCentral.setId("cafe-central");
            cafeVariomeCentral.setName("Cafe Variome Central");
            cafeVariomeCentral.setOrganization(leicester);
            cafeVariomeCentral.setVisible(true);
            cafeVariomeCentral.setAggregator(false);
            cafeVariomeCentral.setParser(ejbResolver.getClassId(JsonCafeResponseParser.class));
            cafeVariomeCentral.setFetcher(ejbResolver.getClassId(GetResponseFetcher.class));
            cafeVariomeCentral.setRequester(cdiResolver.getClassId(ChromPosAlleleRequestConstructor.class));
            cafeVariomeCentral.setReferenceConverter(cdiResolver.getClassId(EmptyReferenceConverter.class));
            cafeVariomeCentral.setChromosomeConverter(cdiResolver.getClassId(LowerCaseChromosomeConverter.class));
            cafeVariomeCentral.setPositionConverter(cdiResolver.getClassId(IncrementPositionConverter.class));
            cafeVariomeCentral.setAlleleConverter(cdiResolver.getClassId(EmptyAlleleConverter.class));
            cafeVariomeCentral.setEnabled(true);
            cafeVariomeCentral.setUrl("http://beacon.cafevariome.org/query?chrom=chr%s&pos=%d&allele=%s");
            cafeVariomeCentral.setSupportedReferences(EnumSet.of(Reference.HG19));
            beaconDao.save(cafeVariomeCentral);
            Beacon cafeCardioKit = new Beacon();
            cafeCardioKit.setId("cafe-cardiokit");
            cafeCardioKit.setName("Cafe CardioKit");
            cafeCardioKit.setOrganization(leicester);
            cafeCardioKit.setVisible(true);
            cafeCardioKit.setAggregator(false);
            cafeCardioKit.setParser(ejbResolver.getClassId(JsonCafeResponseParser.class));
            cafeCardioKit.setFetcher(ejbResolver.getClassId(GetResponseFetcher.class));
            cafeCardioKit.setRequester(cdiResolver.getClassId(ChromPosAlleleRequestConstructor.class));
            cafeCardioKit.setReferenceConverter(cdiResolver.getClassId(EmptyReferenceConverter.class));
            cafeCardioKit.setChromosomeConverter(cdiResolver.getClassId(LowerCaseChromosomeConverter.class));
            cafeCardioKit.setPositionConverter(cdiResolver.getClassId(IncrementPositionConverter.class));
            cafeCardioKit.setAlleleConverter(cdiResolver.getClassId(EmptyAlleleConverter.class));
            cafeCardioKit.setEnabled(true);
            cafeCardioKit.setUrl("http://beacon.cafevariome.org/query?chrom=chr%s&pos=%d&allele=%s");
            cafeCardioKit.setSupportedReferences(EnumSet.of(Reference.HG19));
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
            broad.setAggregator(false);
            broad.setParser(ejbResolver.getClassId(StringYesNoResponseParser.class));
            broad.setRequester(cdiResolver.getClassId(RefChromPosAlleleRequestConstructor.class));
            broad.setFetcher(ejbResolver.getClassId(GetResponseFetcher.class));
            broad.setReferenceConverter(cdiResolver.getClassId(EmptyReferenceConverter.class));
            broad.setChromosomeConverter(cdiResolver.getClassId(EmptyChromosomeConverter.class));
            broad.setPositionConverter(cdiResolver.getClassId(IncrementPositionConverter.class));
            broad.setAlleleConverter(cdiResolver.getClassId(EmptyAlleleConverter.class));
            broad.setEnabled(true);
            broad.setUrl("http://broad-beacon.broadinstitute.org:8090/dev/beacon/query?ref=%s&chrom=%s&pos=%d&allele=%s");
            broad.setSupportedReferences(EnumSet.of(Reference.HG19));
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
            icgcBeacon.setAggregator(false);
            icgcBeacon.setParser(ejbResolver.getClassId(JsonResponseExistsNullAsFalseResponseParser.class));
            icgcBeacon.setFetcher(ejbResolver.getClassId(GetResponseFetcher.class));
            icgcBeacon.setRequester(cdiResolver.getClassId(RefChromPosAlleleRequestConstructor.class));
            icgcBeacon.setReferenceConverter(cdiResolver.getClassId(GrChReferenceConverter.class));
            icgcBeacon.setChromosomeConverter(cdiResolver.getClassId(EmptyChromosomeConverter.class));
            icgcBeacon.setPositionConverter(cdiResolver.getClassId(IncrementPositionConverter.class));
            icgcBeacon.setAlleleConverter(cdiResolver.getClassId(EmptyAlleleConverter.class));
            icgcBeacon.setEnabled(true);
            icgcBeacon.setUrl("https://dcc.icgc.org/api/v1/beacon/query?reference=%s&chromosome=%s&position=%d&allele=%s&dataset=");
            icgcBeacon.setSupportedReferences(EnumSet.of(Reference.HG19));
            beaconDao.save(icgcBeacon);

            Beacon ucscBeacon = new Beacon();
            ucscBeacon.setId("ucsc");
            ucscBeacon.setName("UCSC");
            ucscBeacon.setOrganization(ucsc);
            ucscBeacon.setVisible(true);
            ucscBeacon.setEnabled(true);
            ucscBeacon.setAggregator(true);
            ucscBeacon.setUrl("http://hgwdev-max.cse.ucsc.edu/cgi-bin/beacon/query");
            ucscBeacon.setSupportedReferences(EnumSet.noneOf(Reference.class));
            ucscBeacon.setParser(null);
            beaconDao.save(ucscBeacon);

            beaconDao.addRelationship(hgmdBeacon, ucscBeacon);
            beaconDao.addRelationship(lovdBeacon, ucscBeacon);

            Beacon googleBeacon = new Beacon();
            googleBeacon.setId("google");
            googleBeacon.setName("Google Genomics Public Data");
            googleBeacon.setOrganization(google);
            googleBeacon.setVisible(true);
            googleBeacon.setAggregator(true);
            googleBeacon.setEnabled(true);
            googleBeacon.setUrl("http://dnastack.com/p/beacon/");
            googleBeacon.setSupportedReferences(EnumSet.noneOf(Reference.class));
            googleBeacon.setParser(null);

            beaconDao.save(googleBeacon);
            beaconDao.addRelationship(platinum, googleBeacon);
            beaconDao.addRelationship(thousandGenomes, googleBeacon);
            beaconDao.addRelationship(thousandGenomesPhase3, googleBeacon);

            Beacon cafeVariome = new Beacon();
            cafeVariome.setId("cafe-variome");
            cafeVariome.setName("Cafe Variome");
            cafeVariome.setOrganization(leicester);
            cafeVariome.setVisible(true);
            cafeVariome.setAggregator(true);
            cafeVariome.setEnabled(true);
            cafeVariome.setUrl("http://beacon.cafevariome.org/query");
            cafeVariome.setSupportedReferences(EnumSet.noneOf(Reference.class));
            beaconDao.save(cafeVariome);
            beaconDao.addRelationship(cafeVariomeCentral, cafeVariome);
            beaconDao.addRelationship(cafeCardioKit, cafeVariome);

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
            bob.setAggregator(true);
            bob.setEnabled(true);
            bob.setUrl("http://beacon-dnastack.rhcloud.com/");
            bob.setSupportedReferences(EnumSet.noneOf(Reference.class));
            beaconDao.save(bob);

            // point all regular beacons to bob
            List<Beacon> beacons = beaconDao.findByAggregation(false);
            for (Beacon b : beacons) {
                beaconDao.addRelationship(b, bob);
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
