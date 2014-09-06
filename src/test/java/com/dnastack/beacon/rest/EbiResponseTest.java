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
import com.dnastack.beacon.core.Chromosome;
import com.dnastack.beacon.core.Query;
import com.dnastack.beacon.core.Reference;
import com.dnastack.beacon.util.QueryUtils;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.bind.JAXBException;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test of EBI service.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RunWith(Arquillian.class)
@RunAsClient
public class EbiResponseTest extends AbstractResponseTest {

    private Beacon b;

    @Before
    public void setUp() {
        b = new Beacon("ebi", "EMBL-EBI");
    }

    @Test
    @Override
    public void testAllRefsFound(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = QueryUtils.constructQuery("13", 32888799L, "G", null);
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

    @Test
    @Override
    public void testSpecificRefFound(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = QueryUtils.constructQuery("13", 32888799L, "G", "hg19");
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

    @Test
    @Override
    public void testSpecificRefNotFound(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = QueryUtils.constructQuery("13", 32888799L, "G", "hg38");
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse() == null || !br.getResponse());
    }

    @Test
    @Override
    public void testInvalidRef(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = QueryUtils.constructQuery("13", 32888799L, "G", "hg100");
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(br.getQuery().getChromosome(), Chromosome.CHR13);
        assertEquals(br.getQuery().getPosition(), new Long(32888799));
        assertEquals(br.getQuery().getAllele(), "G");
        assertEquals(br.getQuery().getReference(), null);
        assertTrue(br.getResponse());
    }

    @Test
    @Override
    public void testRefConversion(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = QueryUtils.constructQuery("13", 32888799L, "G", "grch37");
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(br.getQuery().getChromosome(), Chromosome.CHR13);
        assertEquals(br.getQuery().getPosition(), new Long(32888799));
        assertEquals(br.getQuery().getAllele(), "G");
        assertEquals(br.getQuery().getReference(), Reference.HG19);
        assertNotNull(br.getResponse());
    }

    @Test
    @Override
    public void testStringAllele(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = QueryUtils.constructQuery("1", 46403L, "TGT", null);
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

    @Test
    @Override
    public void testDel(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = QueryUtils.constructQuery("1", 1002921L, "D", null);
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

    @Test
    @Override
    public void testIns(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = QueryUtils.constructQuery("1", 46403L, "I", null);
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertTrue(br.getResponse());
    }

    @Test
    @Override
    public void testAllRefsNotFound(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = QueryUtils.constructQuery("15", 41087870L, "C", null);
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertFalse(br.getResponse());
    }

    @Test
    @Override
    public void testInvalidAllele(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = QueryUtils.constructQuery("15", 41087870L, "DC", null);
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(br.getQuery().getChromosome(), Chromosome.CHR15);
        assertEquals(br.getQuery().getPosition(), new Long(41087870));
        assertEquals(br.getQuery().getReference(), null);
        assertNull(br.getQuery().getAllele());
        assertNull(br.getResponse());
    }

    @Test
    @Override
    public void testAlleleConversion(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = QueryUtils.constructQuery("1", 1002922L, "DEL", null);
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(br.getQuery().getChromosome(), Chromosome.CHR1);
        assertEquals(br.getQuery().getPosition(), new Long(1002922));
        assertEquals(br.getQuery().getReference(), null);
        assertEquals(q.getAllele().substring(0, 1), br.getQuery().getAllele());
        assertNotNull(br.getResponse());
    }

    @Test
    @Override
    public void testChromConversion(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = QueryUtils.constructQuery("chrom14", 106833421L, "D", null);
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(br.getQuery().getPosition(), new Long(106833421));
        assertEquals(br.getQuery().getAllele(), "D");
        assertEquals(br.getQuery().getReference(), null);
        assertEquals(br.getQuery().getChromosome(), Chromosome.CHR14);

        assertNotNull(br.getResponse());
    }

    @Test
    @Override
    public void testInvalidChrom(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = QueryUtils.constructQuery("30", 41087870L, "A", null);
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(br.getQuery().getPosition(), new Long(41087870));
        assertEquals(br.getQuery().getAllele(), "A");
        assertEquals(br.getQuery().getReference(), null);
        assertNull(br.getQuery().getChromosome());
        assertNull(br.getResponse());
    }

    @Test
    @Override
    public void testChromX(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = QueryUtils.constructQuery("X", 41087870L, "A", null);
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        System.out.println(br);
        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertFalse(br.getResponse());
    }

    @Test
    @Override
    public void testChromMT(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Query q = QueryUtils.constructQuery("MT", 41087870L, "A", null);
        BeaconResponse br = readResponse(url.toExternalForm() + getUrl(b, q));

        assertNotNull(br);
        assertEquals(b, br.getBeacon());
        assertEquals(q, br.getQuery());
        assertFalse(br.getResponse());
    }

    @Override
    public void testDifferentGenome(URL url) throws JAXBException, MalformedURLException {
        // intentionally empty
    }
}
