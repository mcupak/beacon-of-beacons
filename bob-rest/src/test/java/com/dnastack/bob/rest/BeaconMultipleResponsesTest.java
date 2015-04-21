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

import com.dnastack.bob.rest.util.QueryEntry;
import com.dnastack.bob.service.dto.BeaconResponseTo;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.dnastack.bob.rest.util.BeaconResponseTestUtils.queriesMatch;
import static com.dnastack.bob.rest.util.DataProvider.getBeacons;
import static com.dnastack.bob.rest.util.DataProvider.getQueries;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isIn;

/**
 * Test of responses.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RunWith(Arquillian.class)
@RunAsClient
public class BeaconMultipleResponsesTest extends AbstractResponseTest {

    private static final Logger logger = Logger.getLogger(BeaconMultipleResponsesTest.class.getName());

    @Test
    public void testAllResponses(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        QueryEntry q = new QueryEntry(getQueries().get(0));
        q.setBeacon(null);
        List<BeaconResponseTo> brs = readBeaconResponses(url.toExternalForm() + getUrl(q, true));

        Set<String> ids = new HashSet<>();
        for (BeaconResponseTo br : brs) {
            ids.add(br.getBeacon().getId());
        }
        assertThat(getBeacons(), everyItem(isIn(ids)));

        for (BeaconResponseTo br : brs) {
            assertThat(queriesMatch(br.getQuery(), q), is(true));
        }
    }

    @Test
    public void testResponsesFiltered(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Set<String> beacons = getBeacons();

        for (String b : beacons) {
            QueryEntry query = (QueryEntry) getQueries(b).toArray()[0];

            logger.log(Level.INFO, String.format("Testing query: %s", query));
            Boolean res = readBeaconResponses(url.toExternalForm() + getUrl(query, true)).get(0).getResponse();
            logger.log(Level.INFO, String.format("Beacon: " + query.getBeacon() + " - expected response: %s; actual response: %s", query.getResponse(), res));

            collector.checkThat(query.toString(), res, equalTo(query.getResponse()));
        }
    }

    @Test
    public void testMultipleResponsesFiltered(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        QueryEntry query = new QueryEntry(getQueries().get(0));
        Set<String> beacons = getBeacons();
        String a = query.getBeacon();

        for (String b : beacons) {
            if (!a.equals(b)) {
                query.setBeacon(String.format("[%s,%s]", a, b));
                logger.log(Level.INFO, String.format("Testing query: %s", query));

                List<BeaconResponseTo> brs = readBeaconResponses(url.toExternalForm() + getUrl(query, true));

                Set<String> ids = new HashSet<>();
                for (BeaconResponseTo br : brs) {
                    ids.add(br.getBeacon().getId());
                }
                collector.checkThat(query.toString(), a, isIn(ids));
                collector.checkThat(query.toString(), b, isIn(ids));
                collector.checkThat(query.toString(), ids.size(), equalTo(2));

                for (BeaconResponseTo br : brs) {
                    collector.checkThat(query.toString(), queriesMatch(br.getQuery(), query), is(true));
                }
            }
        }
    }
}
