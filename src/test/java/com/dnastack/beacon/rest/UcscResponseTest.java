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
import javax.xml.bind.JAXBException;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test of UCSC service.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RunWith(Arquillian.class)
public class UcscResponseTest extends AbstractResponseTest {

    private Beacon b;

    @Before
    public void setUp() {
        b = new Beacon("clinvar", "NCBI ClinVar");
    }

    @Override
    public void testFound(URL url) throws JAXBException, MalformedURLException {
        // intantionally blank
    }

    @Test
    @RunAsClient
    public void testFoundClinvar(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        b = new Beacon("clinvar", "NCBI ClinVar");
        Query q = new Query("1", 10042537L, "T");
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

    @Test
    @RunAsClient
    public void testFoundUniprot(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        b = new Beacon("uniprot", "UniProt");
        Query q = new Query("1", 977027L, "T");
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

    @Test
    @RunAsClient
    public void testFoundLovd(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        b = new Beacon("lovd", "Leiden Open Variation");
        Query q = new Query("1", 808921L, "T");
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

    @Test
    @RunAsClient
    @Override
    public void testStringAllele(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = new Query("1", 46402L, "TGT");
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());

        // unsupported
        assertNull(br.getResponse());
    }

    @Test
    @RunAsClient
    @Override
    public void testDel(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = new Query("1", 1002920L, "D");
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());

        // unsupported
        assertNull(br.getResponse());
    }

    @Test
    @RunAsClient
    @Override
    public void testIns(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = new Query("1", 46402L, "I");
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());

        // unsupported
        assertNull(br.getResponse());
    }

    @Test
    @RunAsClient
    @Override
    public void testNotFound(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = new Query("15", 41087869L, "C");
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertFalse(br.getResponse());
    }

    @Test
    @RunAsClient
    @Override
    public void testInvalidAllele(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = new Query("15", 41087869L, "DC");
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertNotEquals(q, br.getQuery());
        assertNull(br.getQuery().getAllele());
        assertNull(br.getResponse());
    }

    @Test
    @RunAsClient
    @Override
    public void testAlleleConversion(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = new Query("1", 1002921L, "g");
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertNotEquals(q, br.getQuery());
        assertEquals(q.getAllele().toUpperCase(), br.getQuery().getAllele());
        assertNotNull(br.getResponse());
    }

    @Test
    @RunAsClient
    @Override
    public void testChromConversion(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = new Query("chrom14", 106833420L, "A");
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertNotEquals(q, br.getQuery());
        assertEquals(q.getChromosome().substring(q.getChromosome().length() - 2, q.getChromosome().length()), br.getQuery().getChromosome());
        assertNotNull(br.getResponse());
    }

    @Test
    @RunAsClient
    @Override
    public void testInvalidChrom(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = new Query("30", 41087869L, "A");
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertNotEquals(q, br.getQuery());
        assertNull(br.getQuery().getChromosome());
        assertNull(br.getResponse());
    }

    @Test
    @RunAsClient
    @Override
    public void testChromX(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = new Query("X", 41087869L, "A");
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        System.out.println(br);
        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertFalse(br.getResponse());
    }

    @Test
    @RunAsClient
    @Override
    public void testChromMT(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = new Query("MT", 41087869L, "A");
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());

        // unsupported
        assertNull(br.getResponse());
    }

    @Override
    public void testDifferentGenome(URL url) throws JAXBException, MalformedURLException {
        // intentionally empty
    }
}
