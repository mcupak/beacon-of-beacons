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

import com.dnastack.bob.dto.BeaconResponseTo;
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

import static com.dnastack.bob.rest.BasicTest.readObject;
import static com.dnastack.bob.rest.util.BeaconResponseTestUtils.beaconsMatch;
import static com.dnastack.bob.rest.util.BeaconResponseTestUtils.queriesMatch;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test of responses.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RunWith(Arquillian.class)
@RunAsClient
public class BeaconResponsesTest extends BasicTest {

    public static final String QUERY_BEACON_TEMPLATE = "rest/responses?beacon=%s&chrom=%s&pos=%s&allele=%s";
    public static final String QUERY_BEACON_WITH_REF_TEMPLATE = "rest/responses?beacon=%s&chrom=%s&pos=%s&allele=%s&ref=%s";
    public static final String QUERY_TEMPLATE = "rest/responses?chrom=%s&pos=%s&allele=%s";
    public static final String QUERY_WITH_REF_TEMPLATE = "rest/responses?chrom=%s&pos=%s&allele=%s&ref=%s";
    private static final Set<String> BEACON_IDS = ImmutableSet.of("clinvar", "uniprot", "lovd", "ebi", "ncbi", "wtsi", "amplab", "kaviar", "broad", "bob");

    protected static String getUrl(String b, String[] params) {
        String res = null;

        if (params.length == 4) {
            if (params[3] == null) {
                res = String.format(QUERY_BEACON_TEMPLATE, b, params[0], params[1], params[2]);
            } else {
                res = String.format(QUERY_BEACON_WITH_REF_TEMPLATE, b, params[0], params[1], params[2], params[3]);
            }
        }

        return res;
    }

    protected static String getUrl(String[] params) {
        String res = null;

        if (params.length == 4) {
            if (params[3] == null) {
                res = String.format(QUERY_TEMPLATE, params[0], params[1], params[2]);
            } else {
                res = String.format(QUERY_WITH_REF_TEMPLATE, params[0], params[1], params[2], params[3]);
            }
        }

        return res;
    }

    @SuppressWarnings("unchecked")
    public static List<BeaconResponseTo> readResponses(String url) throws JAXBException, MalformedURLException {
        return (List<BeaconResponseTo>) readObject(BeaconResponseTo.class, url);
    }

    @Test
    public void testAllResponses(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] q = {"13", "32888799", "G", null};
        List<BeaconResponseTo> brs = readResponses(url.toExternalForm() + getUrl(q));

        Set<String> ids = new HashSet<>();
        for (BeaconResponseTo br : brs) {
            ids.add(br.getBeacon().getId());
        }
        for (String s : BEACON_IDS) {
            assertTrue(ids.contains(s));

        }

        for (BeaconResponseTo br : brs) {
            assertTrue(queriesMatch(br.getQuery(), q));
        }
    }

    @Test
    public void testAllResponsesWithSpecificRef(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] q = {"13", "32888799", "G", "hg19"};
        List<BeaconResponseTo> brs = readResponses(url.toExternalForm() + getUrl(q));

        Set<String> ids = new HashSet<>();
        for (BeaconResponseTo br : brs) {
            ids.add(br.getBeacon().getId());
        }
        for (String s : BEACON_IDS) {
            assertTrue(ids.contains(s));

        }

        for (BeaconResponseTo br : brs) {
            assertTrue(queriesMatch(br.getQuery(), q));
        }
    }

    @Test
    public void testSpecificResponseWithSpecificRef(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String b = "lovd";
        String[] q = {"1", "808922", "A", "hg19"};
        BeaconResponseTo br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), b));
        assertTrue(queriesMatch(br.getQuery(), q));
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForClinvar(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String b = "clinvar";
        String[] q = {"1", "10042538", "T", null};
        BeaconResponseTo br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), b));
        assertTrue(queriesMatch(br.getQuery(), q));
        assertFalse(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForUniprot(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String b = "uniprot";
        String[] q = {"1", "977028", "T", null};
        BeaconResponseTo br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), b));
        assertTrue(queriesMatch(br.getQuery(), q));
        assertFalse(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForLovd(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String b = "lovd";
        String[] q = {"1", "808922", "A", null};
        BeaconResponseTo br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), b));
        assertTrue(queriesMatch(br.getQuery(), q));
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForAmplab(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String b = "amplab";
        String[] q = {"15", "41087870", "A", null};
        BeaconResponseTo br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), b));
        assertTrue(queriesMatch(br.getQuery(), q));
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForKaviar(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String b = "kaviar";
        String[] q = {"1", "808922", "A", null};
        BeaconResponseTo br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), b));
        assertTrue(queriesMatch(br.getQuery(), q));
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForNcbi(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String b = "ncbi";
        String[] q = {"22", "17213590", "TGTTA", null};
        BeaconResponseTo br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), b));
        assertTrue(queriesMatch(br.getQuery(), q));
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForEbi(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String b = "ebi";
        String[] q = {"13", "32888799", "G", null};
        BeaconResponseTo br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), b));
        assertTrue(queriesMatch(br.getQuery(), q));
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForWtsi(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String b = "wtsi";
        String[] q = {"1", "808922", "A", null};
        BeaconResponseTo br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), b));
        assertTrue(queriesMatch(br.getQuery(), q));
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForCafeVariome(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String b = "cafe-variome";
        String[] q = {"2", "179612321", "T", null};
        BeaconResponseTo br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), b));
        assertTrue(queriesMatch(br.getQuery(), q));
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForBroad(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String b = "broad";
        String[] q = {"1", "13417", "CGAGA", null};
        BeaconResponseTo br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), b));
        assertTrue(queriesMatch(br.getQuery(), q));
        assertTrue(br.getResponse());
    }

    @Test
    public void testResponsesFilteredForBob(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String b = "bob";
        String[] q = {"13", "32888799", "G", null};
        BeaconResponseTo br = readResponses(url.toExternalForm() + getUrl(b, q)).get(0);

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), b));
        assertTrue(queriesMatch(br.getQuery(), q));
        assertTrue(br.getResponse());
    }

    @Test
    public void testMultipleResponsesFiltered(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String id1 = "amplab";
        String id2 = "clinvar";
        String[] q = {"13", "32888799", "G", null};
        List<BeaconResponseTo> brs = readResponses(url.toExternalForm() + getUrl("[" + id1 + "," + id2 + "]", q));

        assertNotNull(brs);
        assertTrue(brs.size() == 2);
        assertTrue(beaconsMatch(brs.get(0).getBeacon(), id1) || beaconsMatch(brs.get(0).getBeacon(), id2));
        assertTrue(beaconsMatch(brs.get(1).getBeacon(), id1) || beaconsMatch(brs.get(1).getBeacon(), id2));
        assertTrue(queriesMatch(brs.get(0).getQuery(), q));
        assertTrue(queriesMatch(brs.get(1).getQuery(), q));
        assertNotNull(brs.get(0).getResponse());
        assertNotNull(brs.get(1).getResponse());
    }

    @Test
    public void testMultipleResponsesOfAggregatorsFiltered(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String id1 = "bob";
        String id2 = "cafe-variome";
        String[] q = {"2", "179393691", "T", null};
        List<BeaconResponseTo> brs = readResponses(url.toExternalForm() + getUrl("[" + id1 + "," + id2 + "]", q));

        assertNotNull(brs);
        assertTrue(brs.size() == 2);
        assertTrue(beaconsMatch(brs.get(0).getBeacon(), id1) || beaconsMatch(brs.get(0).getBeacon(), id2));
        assertTrue(beaconsMatch(brs.get(1).getBeacon(), id1) || beaconsMatch(brs.get(1).getBeacon(), id2));
        assertTrue(queriesMatch(brs.get(0).getQuery(), q));
        assertTrue(queriesMatch(brs.get(1).getQuery(), q));
        assertNotNull(brs.get(0).getResponse());
        assertNotNull(brs.get(1).getResponse());
    }

}
