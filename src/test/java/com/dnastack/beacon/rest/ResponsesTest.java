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
public class ResponsesTest extends BasicTest {

    public static final String QUERY_BEACON_TEMPLATE = "rest/responses?beacon=%s&chrom=%s&pos=%s&allele=%s";
    public static final String QUERY_TEMPLATE = "rest/responses?chrom=%s&pos=%s&allele=%s";
    private static Set<Beacon> beacons;

    public static String getUrl(Beacon b, Query q) {
        return String.format(QUERY_BEACON_TEMPLATE, b.getId(), q.getChromosome(), q.getPosition(), q.getAllele());
    }

    public static String getUrl(Query q) {
        return String.format(QUERY_TEMPLATE, q.getChromosome(), q.getPosition(), q.getAllele());
    }

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
    }

    @Test
    @RunAsClient
    public void testAllResponses(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = new Query("13", 32888798L, "G");
        List<BeaconResponse> brs = readResponses(url.toExternalForm() + getUrl(q));

        assertEquals(brs.size(), beacons.size());
        for (BeaconResponse br : brs) {
            assertTrue(beacons.contains(br.getBeacon()));
            assertEquals(q, br.getQuery());
        }
    }

    @Test
    @RunAsClient
    public void testResponsesFilteredForClinvar(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("clinvar", "NCBI ClinVar");
        Query q = new Query("1", 10042537L, "T");
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

    @Test
    @RunAsClient
    public void testResponsesFilteredForUniprot(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("uniprot", "UniProt");
        Query q = new Query("1", 977027L, "T");
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

    @Test
    @RunAsClient
    public void testResponsesFilteredForLovd(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("lovd", "Leiden Open Variation");
        Query q = new Query("1", 808921L, "T");
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

    @Test
    @RunAsClient
    public void testResponsesFilteredForAmplab(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("amplab", "AMPLab");
        Query q = new Query("15", 41087869L, "A");
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

    @Test
    @RunAsClient
    public void testResponsesFilteredForNcbi(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("ncbi", "NCBI");
        Query q = new Query("22", 17213589L, "TGTTA");
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

    @Test
    @RunAsClient
    public void testResponsesFilteredForEbi(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("ebi", "EMBL-EBI");
        Query q = new Query("13", 32888798L, "G");
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

    @Test
    @RunAsClient
    public void testResponsesFilteredForWtsi(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("wtsi", "Wellcome Trust Sanger Institute");
        Query q = new Query("X", 41087869L, "A");
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

    @Test
    @RunAsClient
    public void testResponsesFilteredForBob(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("bob", "beacon of beacons");
        Query q = new Query("13", 32888798L, "G");
        BeaconResponse br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

}
