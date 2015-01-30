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
package com.dnastack.bob.rest;

import com.dnastack.bob.dto.BeaconTo;
import com.google.common.collect.ImmutableSet;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.bind.JAXBException;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
    public static final Set<String> BEACON_IDS = ImmutableSet.of("clinvar", "uniprot", "lovd", "ebi", "ncbi", "wtsi", "amplab", "kaviar", "broad", "bob");

    public static String getUrl() {
        return BEACONS_TEMPLATE;
    }

    public static String getUrl(String beaconId, boolean param) {
        return String.format(param ? BEACONS_FILTERED_TEMPLATE : BEACON_TEMPLATE, beaconId);
    }

    @SuppressWarnings("unchecked")
    public static List<BeaconTo> readBeacons(String url) throws JAXBException, MalformedURLException {
        return (List<BeaconTo>) readObject(BeaconTo.class, url);
    }

    public static BeaconTo readBeacon(String url) throws JAXBException, MalformedURLException {
        return (BeaconTo) readObject(BeaconTo.class, url);
    }

    @Test
    public void testAllBeacons(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        List<BeaconTo> bs = readBeacons(url.toExternalForm() + getUrl());
        Set<String> ids = new HashSet<>();
        for (BeaconTo b : bs) {
            ids.add(b.getId());
        }

        for (String s : BEACON_IDS) {
            assertTrue(ids.contains(s));
        }
    }

    @Test
    public void testBeaconsFiltered(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        for (String id : BEACON_IDS) {
            List<BeaconTo> beacons = readBeacons(url.toExternalForm() + getUrl(id, true));

            assertNotNull(beacons);
            assertEquals(beacons.get(0).getId(), id);
        }
    }

    @Test
    public void testMultipleBeaconsFiltered(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String id1 = "clinvar";
        String id2 = "amplab";
        List<BeaconTo> beacons = readBeacons(url.toExternalForm() + getUrl("[" + id1 + "," + id2 + "]", true));

        assertNotNull(beacons);
        assertTrue(beacons.size() == 2);
        assertTrue((beacons.get(0).getId().equals(id1) || beacons.get(0).getId().equals(id2)) && (beacons.get(1).getId().equals(id1) || beacons.get(0).getId().equals(id2)));
    }

    @Test
    public void testBeacon(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        for (String id : BEACON_IDS) {
            BeaconTo b = readBeacon(url.toExternalForm() + getUrl(id, false));

            assertNotNull(b);
            assertEquals(id, b.getId());
        }
    }
}
