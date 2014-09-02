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
 * Test of beacons info.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RunWith(Arquillian.class)
@RunAsClient
public class BeaconsTest extends BasicTest {

    public static final String BEACONS_TEMPLATE = "rest/beacons";
    public static final String BEACONS_FILTERED_TEMPLATE = "rest/beacons?beacon=%s";
    public static final String BEACON_TEMPLATE = "rest/beacons/%s";
    private static Set<Beacon> beacons;

    public static String getUrl() {
        return BEACONS_TEMPLATE;
    }

    public static String getUrl(Beacon b, boolean param) {
        return String.format(param ? BEACONS_FILTERED_TEMPLATE : BEACON_TEMPLATE, b.getId());
    }

    public static List<Beacon> readBeacons(String url) throws JAXBException, MalformedURLException {
        return (List<Beacon>) readObject(Beacon.class, url);
    }

    public static Beacon readBeacon(String url) throws JAXBException, MalformedURLException {
        return (Beacon) readObject(Beacon.class, url);
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
        beacons.add(new Beacon("kaviar", "Kaviar2"));
    }

    @Test
    public void testAllBeacons(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        List<Beacon> bs = readBeacons(url.toExternalForm() + getUrl());

        assertEquals(bs.size(), beacons.size());
        for (Beacon b : bs) {
            assertTrue(beacons.contains(b));
        }
    }

    @Test
    public void testBeaconsFilteredForClinvar(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("clinvar", "NCBI ClinVar");
        Beacon c = readBeacons(url.toExternalForm() + getUrl(b, true)).get(0);

        assertNotNull(c);
        assertEquals(b, c);
    }

    @Test
    public void testBeaconsFilteredForUniprot(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("uniprot", "UniProt");
        Beacon c = readBeacons(url.toExternalForm() + getUrl(b, true)).get(0);

        assertNotNull(c);
        assertEquals(b, c);
    }

    @Test
    public void testBeaconsFilteredForLovd(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("lovd", "Leiden Open Variation");
        Beacon c = readBeacons(url.toExternalForm() + getUrl(b, true)).get(0);

        assertNotNull(c);
        assertEquals(b, c);
    }

    @Test
    public void testBeaconsFilteredForAmplab(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("amplab", "AMPLab");
        Beacon c = readBeacons(url.toExternalForm() + getUrl(b, true)).get(0);

        assertNotNull(c);
        assertEquals(b, c);
    }

    @Test
    public void testBeaconsFilteredForNcbi(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("ncbi", "NCBI");
        Beacon c = readBeacons(url.toExternalForm() + getUrl(b, true)).get(0);

        assertNotNull(c);
        assertEquals(b, c);
    }

    @Test
    public void testBeaconsFilteredForEbi(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("ebi", "EMBL-EBI");
        Beacon c = readBeacons(url.toExternalForm() + getUrl(b, true)).get(0);

        assertNotNull(c);
        assertEquals(b, c);
    }

    @Test
    public void testBeaconsFilteredForWtsi(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("wtsi", "Wellcome Trust Sanger Institute");
        Beacon c = readBeacons(url.toExternalForm() + getUrl(b, true)).get(0);

        assertNotNull(c);
        assertEquals(b, c);
    }

    @Test
    public void testBeaconsFilteredForBob(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("bob", "beacon of beacons");
        Beacon c = readBeacons(url.toExternalForm() + getUrl(b, true)).get(0);

        assertNotNull(c);
        assertEquals(b, c);
    }

    @Test
    public void testBeaconClinvar(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("clinvar", "NCBI ClinVar");
        Beacon c = readBeacon(url.toExternalForm() + getUrl(b, false));

        assertNotNull(c);
        assertEquals(b, c);
    }

    @Test
    public void testBeaconUniprot(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("uniprot", "UniProt");
        Beacon c = readBeacon(url.toExternalForm() + getUrl(b, false));

        assertNotNull(c);
        assertEquals(b, c);
    }

    @Test
    public void testBeaconLovd(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("lovd", "Leiden Open Variation");
        Beacon c = readBeacon(url.toExternalForm() + getUrl(b, false));

        assertNotNull(c);
        assertEquals(b, c);
    }

    @Test
    public void testBeaconAmplab(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("amplab", "AMPLab");
        Beacon c = readBeacon(url.toExternalForm() + getUrl(b, false));

        assertNotNull(c);
        assertEquals(b, c);
    }

    @Test
    public void testBeaconNcbi(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("ncbi", "NCBI");
        Beacon c = readBeacon(url.toExternalForm() + getUrl(b, false));

        assertNotNull(c);
        assertEquals(b, c);
    }

    @Test
    public void testBeaconEbi(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("ebi", "EMBL-EBI");
        Beacon c = readBeacon(url.toExternalForm() + getUrl(b, false));

        assertNotNull(c);
        assertEquals(b, c);
    }

    @Test
    public void testBeaconWtsi(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("wtsi", "Wellcome Trust Sanger Institute");
        Beacon c = readBeacon(url.toExternalForm() + getUrl(b, false));

        assertNotNull(c);
        assertEquals(b, c);
    }

    @Test
    public void testBeaconBob(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Beacon b = new Beacon("bob", "beacon of beacons");
        Beacon c = readBeacon(url.toExternalForm() + getUrl(b, false));

        assertNotNull(c);
        assertEquals(b, c);
    }
}
