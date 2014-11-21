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
import com.dnastack.bob.dto.ReferenceTo;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.bind.JAXBException;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.dnastack.bob.rest.util.BeaconResponseTestUtils.beaconsMatch;
import static com.dnastack.bob.rest.util.BeaconResponseTestUtils.getNonMachingFields;
import static com.dnastack.bob.rest.util.BeaconResponseTestUtils.queriesMatch;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test of WTSI service.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RunWith(Arquillian.class)
@RunAsClient
public class WtsiResponseTest extends AbstractResponseTest {

    private static final String BEACON = "wtsi";

    @Test
    @Override
    public void testAllRefsFound(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"1", "808922", "A", null};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(queriesMatch(br.getQuery(), query));
        assertTrue(br.getResponse());
    }

    @Test
    @Override
    public void testSpecificRefFound(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"1", "808922", "A", "hg19"};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(queriesMatch(br.getQuery(), query));
        assertTrue(br.getResponse());
    }

    @Test
    @Override
    public void testSpecificRefNotFound(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"1", "808922", "A", "hg38"};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(queriesMatch(br.getQuery(), query));
        assertTrue(br.getResponse() == null || !br.getResponse());
    }

    @Test
    @Override
    public void testInvalidRef(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"1", "808922", "A", "hg100"};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(getNonMachingFields(br.getQuery(), query).size() == 1);
        assertEquals(br.getQuery().getReference(), null);
        assertNull(br.getResponse());
    }

    @Test
    @Override
    public void testRefConversion(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"1", "808922", "A", "grch37"};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(getNonMachingFields(br.getQuery(), query).size() == 1);
        assertEquals(br.getQuery().getReference(), ReferenceTo.HG19);
        assertNotNull(br.getResponse());
    }

    @Test
    public void testFoundRef(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"13", "32888798", "A", null};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(queriesMatch(br.getQuery(), query));

        // ref response is false
        assertFalse(br.getResponse());
    }

    @Test
    @Override
    public void testStringAllele(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"1", "46403", "TGT", null};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(queriesMatch(br.getQuery(), query));

        // unsupported
        assertFalse(br.getResponse());
    }

    @Test
    @Override
    public void testDel(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"1", "1002921", "D", null};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(queriesMatch(br.getQuery(), query));

        // unsupported
        assertFalse(br.getResponse());
    }

    @Test
    @Override
    public void testIns(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"1", "46403", "I", null};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(queriesMatch(br.getQuery(), query));

        // unsupported
        assertFalse(br.getResponse());
    }

    @Test
    @Override
    public void testAllRefsNotFound(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"13", "32888799", "A", null};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(queriesMatch(br.getQuery(), query));
        assertFalse(br.getResponse());
    }

    @Test
    @Override
    public void testInvalidAllele(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"15", "41087870", "DC", null};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(getNonMachingFields(br.getQuery(), query).size() == 1);
        assertNull(br.getQuery().getAllele());
        assertNull(br.getResponse());
    }

    @Test
    @Override
    public void testAlleleConversion(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"1", "1002922", "DEL", null};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(getNonMachingFields(br.getQuery(), query).size() == 1);
        assertEquals(query[2].substring(0, 1), br.getQuery().getAllele());

        // unsupported
        assertFalse(br.getResponse());
    }

    @Test
    @Override
    public void testChromConversion(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"chrom14", "106833421", "A", null};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue((getNonMachingFields(br.getQuery(), query).size() == 1) && (getNonMachingFields(br.getQuery(), query).contains(0)));
        assertNotNull(br.getResponse());
    }

    @Test
    @Override
    public void testInvalidChrom(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"30", "41087870", "A", null};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(getNonMachingFields(br.getQuery(), query).size() == 1);
        assertNull(br.getQuery().getChromosome());
        assertNull(br.getResponse());
    }

    @Test
    @Override
    public void testChromX(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"X", "41087870", "C", null};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(queriesMatch(br.getQuery(), query));
        assertNotNull(br.getResponse());
    }

    @Test
    @Override
    public void testChromMT(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"MT", "2", "A", null};
        BeaconResponseTo br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(beaconsMatch(br.getBeacon(), BEACON));
        assertTrue(queriesMatch(br.getQuery(), query));
        assertNotNull(br.getResponse());
    }

    @Override
    public void testDifferentGenome(URL url) throws JAXBException, MalformedURLException {
        // intentionally empty
    }
}
