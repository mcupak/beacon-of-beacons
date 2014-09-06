/*
 * The MIT License
 *
 * Copyright 2014 DNAstack.
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
package com.dnastack.beacon.rest;

import com.dnastack.beacon.core.Beacon;
import com.dnastack.beacon.core.BeaconResponse;
import com.dnastack.beacon.core.Query;
import com.dnastack.beacon.util.QueryUtils;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.bind.JAXBException;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test of responses.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RunWith(Arquillian.class)
@RunAsClient
public class ResponsesTest extends BasicTest {

    public static final String QUERY_BEACON_TEMPLATE = "rest/responses?beacon=%s&chrom=%s&pos=%s&allele=%s";
    public static final String QUERY_BEACON_WITH_REF_TEMPLATE = "rest/responses?beacon=%s&chrom=%s&pos=%s&allele=%s&ref=%s";
    public static final String QUERY_TEMPLATE = "rest/responses?chrom=%s&pos=%s&allele=%s";
    public static final String QUERY_WITH_REF_TEMPLATE = "rest/responses?chrom=%s&pos=%s&allele=%s&ref=%s";
    private static Set<Beacon> beacons;

    public static String getUrl(Beacon b, Query q) {
        String res;
        if (q.getReference() == null) {
            res = String.format(QUERY_BEACON_TEMPLATE, b.getId(), q.getChromosome(), q.getPosition(), q.getAllele());
        } else {
            res = String.format(QUERY_BEACON_WITH_REF_TEMPLATE, b.getId(), q.getChromosome(), q.getPosition(), q.getAllele(), q.getReference());
        }

        return res;
    }

    public static String getUrl(Query q) {
        String res;
        if (q.getReference() == null) {
            res = String.format(QUERY_TEMPLATE, q.getChromosome(), q.getPosition(), q.getAllele());
        } else {
            res = String.format(QUERY_WITH_REF_TEMPLATE, q.getChromosome(), q.getPosition(), q.getAllele(), q.getReference());
        }

        return res;
    }

    @SuppressWarnings("unchecked")
    public static List<BeaconResponse> readResponses(String url) throws JAXBException, MalformedURLException {
        return (List<BeaconResponse>) readObject(BeaconResponse.class, url);
    }

    @BeforeClass
    public static void setUp() {
        beacons = new HashSet<>();
        beacons.add(new Beacon("clinvar", "NCBI ClinVar"));
        beacons.add(new Beacon("uniprot", "UniProt"));
        beacons.add(new Beacon("lovd", "Leiden Open Variation"));
        beacons.add(new Beacon("ebi", "EMBL-EBI"));
        beacons.add(new Beacon("ncbi", "NCBI"));
        beacons.add(new Beacon("wtsi", "Wellcome Trust Sanger Institute"));
        beacons.add(new Beacon("amplab", "AMPLab"));
        beacons.add(new Beacon("kaviar", "Known VARiants"));
    }

    @Test
    public void testAllResponses(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = QueryUtils.constructQuery("13", 32888799L, "G", null);
        List<BeaconResponse> brs = readResponses(url.toExternalForm() + getUrl(q));

        assertEquals(brs.size(), beacons.size());
        for (BeaconResponse br : brs) {
            assertTrue(beacons.contains(br.getBeacon()));
            assertEquals(q, br.getQuery());
        }
    }

    @Test
    public void testAllResponsesWithSpecificRef(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = QueryUtils.constructQuery("13", 32888799L, "G", "hg19");
        List<BeaconResponse> brs = readResponses(url.toExternalForm() + getUrl(q));

        assertEquals(brs.size(), beacons.size());
        for (BeaconResponse br : brs) {
            System.out.println(br.toString());
            assertTrue(beacons.contains(br.getBeacon()));
            assertEquals(q, br.getQuery());
        }
    }

    @Test
    public void testSpecificResponseWithSpecificRef(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("clinvar", "NCBI ClinVar");
        Query q = QueryUtils.constructQuery("1", 10042538L, "T", "hg19");
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForClinvar(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("clinvar", "NCBI ClinVar");
        Query q = QueryUtils.constructQuery("1", 10042538L, "T", null);
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForUniprot(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("uniprot", "UniProt");
        Query q = QueryUtils.constructQuery("1", 977028L, "T", null);
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForLovd(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("lovd", "Leiden Open Variation");
        Query q = QueryUtils.constructQuery("1", 808922L, "T", null);
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForAmplab(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("amplab", "AMPLab");
        Query q = QueryUtils.constructQuery("15", 41087870L, "A", null);
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForKaviar(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("kaviar", "Known VARiants");
        Query q = QueryUtils.constructQuery("1", 808922L, "A", null);
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForNcbi(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("ncbi", "NCBI");
        Query q = QueryUtils.constructQuery("22", 17213590L, "TGTTA", null);
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForEbi(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("ebi", "EMBL-EBI");
        Query q = QueryUtils.constructQuery("13", 32888799L, "G", null);
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForWtsi(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("wtsi", "Wellcome Trust Sanger Institute");
        Query q = QueryUtils.constructQuery("X", 41087870L, "A", null);
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForBob(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("bob", "beacon of beacons");
        Query q = QueryUtils.constructQuery("13", 32888799L, "G", null);
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

}
