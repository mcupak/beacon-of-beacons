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
import com.dnastack.bob.service.dto.BeaconResponseDto;
import lombok.extern.log4j.Log4j;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.xml.bind.JAXBException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dnastack.bob.rest.util.BeaconResponseTestUtils.queriesMatch;
import static com.dnastack.bob.rest.util.DataProvider.getBeacons;
import static com.dnastack.bob.rest.util.DataProvider.getQueries;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.isIn;

/**
 * Test of responses.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RunWith(Arquillian.class)
@RunAsClient
@Log4j
public class BeaconMultipleResponsesTest extends AbstractResponseTest {

    @Test
    public void testAllResponses(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        QueryEntry q = new QueryEntry(getQueries().get(0));
        q.setBeacon(null);
        List<BeaconResponseDto> brs = readBeaconResponses(url.toExternalForm() + getUrl(q, true));

        Set<String> ids = brs.stream().map((BeaconResponseDto br) -> br.getBeacon().getId()).collect(Collectors.toSet());
        assertThat(getBeacons(), everyItem(isIn(ids)));

        brs.stream().forEach((BeaconResponseDto br) -> {
            assertThat(queriesMatch(br.getQuery(), q), is(true));
        });
    }

    @Test
    public void testResponsesFiltered(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Set<String> beacons = getBeacons();

        for (String b : beacons) {
            QueryEntry query = (QueryEntry) getQueries(b).toArray()[0];

            log.info(String.format("Testing query: %s", query));
            List<BeaconResponseDto> brds = readBeaconResponses(url.toExternalForm() + getUrl(query, true));
            collector.checkThat(brds, not(emptyCollectionOf(BeaconResponseDto.class)));
            if (!brds.isEmpty()) {
                Boolean res = brds.get(0).getResponse();
                log.info(String.format("Beacon: " + query.getBeacon() + " - expected response: %s; actual response: %s", query.getResponse(), res));
                collector.checkThat(query.toString(), res, equalTo(query.getResponse()));
            }
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
                log.info(String.format("Testing query: %s", query));

                List<BeaconResponseDto> brs = readBeaconResponses(url.toExternalForm() + getUrl(query, true));

                Set<String> ids = brs.stream().map((BeaconResponseDto br) -> br.getBeacon().getId()).collect(Collectors.toSet());
                collector.checkThat(query.toString(), a, isIn(ids));
                collector.checkThat(query.toString(), b, isIn(ids));
                collector.checkThat(query.toString(), ids.size(), equalTo(2));

                brs.stream().forEach((BeaconResponseDto br) -> {
                    collector.checkThat(query.toString(), queriesMatch(br.getQuery(), query), is(true));
                });
            }
        }
    }
}
