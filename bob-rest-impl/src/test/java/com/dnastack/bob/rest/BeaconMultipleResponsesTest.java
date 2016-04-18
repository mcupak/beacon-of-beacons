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
import com.jayway.restassured.http.ContentType;
import lombok.extern.java.Log;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.dnastack.bob.rest.util.DataProvider.getBeacons;
import static com.dnastack.bob.rest.util.DataProvider.getQueries;
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.path.json.JsonPath.from;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.Matchers.*;

/**
 * Test of responses.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RunWith(Arquillian.class)
@RunAsClient
@Log
public class BeaconMultipleResponsesTest extends AbstractResponseTest {

    @Test
    public void testAllResponses(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        QueryEntry q = new QueryEntry(getQueries().get(0));
        q.setBeacon(null);

        when().get((url.toExternalForm() + getUrl(q, true))).then().statusCode(Response.Status.OK.getStatusCode()).contentType(ContentType.JSON).body("beacon.id", hasItems(getBeacons().toArray())).body("query.chromosome", everyItem(containsString(q.getChromosome()))).body("query.position", everyItem(equalTo(q.getPosition().intValue()))).body("query.allele", everyItem(equalTo(q.getAllele()))).body("query.reference", everyItem(equalTo(q.getReference())));
    }

    @Test
    public void testResponsesFiltered(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        getBeacons().stream().forEach((String b) -> {
            QueryEntry query = (QueryEntry) getQueries(b).toArray()[0];
            log.info(String.format("Testing query: %s", query));

            com.jayway.restassured.response.Response r = get(url.toExternalForm() + getUrl(query, true));
            collector.checkThat(r.getStatusCode(), equalTo(Response.Status.OK.getStatusCode()));
            collector.checkThat(r.getContentType(), equalTo(ContentType.JSON.toString()));

            List<String> beaconIds = from(r.asString()).getList("beacon.id", String.class);
            collector.checkThat(beaconIds, hasSize(1));
            collector.checkThat(beaconIds, hasItem(query.getBeacon()));

            List<Boolean> responses = from(r.asString()).getList("response", Boolean.class);
            collector.checkThat(responses, hasSize(1));
            collector.checkThat(responses, hasItem(query.getResponse()));

            log.info(String.format("Beacon: " + query.getBeacon() + " - expected response: %s; actual response: %s", query.getResponse(), responses.get(0)));
        });
    }

    @Test
    public void testMultipleResponsesFiltered(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        QueryEntry q = new QueryEntry(getQueries().get(0));
        String a = q.getBeacon();

        getBeacons().stream().filter((String b) -> !a.equals(b)).forEach((String b) -> {
            q.setBeacon(String.format("[%s,%s]", a, b));
            log.info(String.format("Testing query: %s", q));
            System.out.println(url.toExternalForm() + getUrl(q, true));

            com.jayway.restassured.response.Response r = get(url.toExternalForm() + getUrl(q, true));
            collector.checkThat(r.getStatusCode(), equalTo(Response.Status.OK.getStatusCode()));
            collector.checkThat(r.getContentType(), equalTo(ContentType.JSON.toString()));

            List<String> beaconIds = from(r.asString()).getList("beacon.id", String.class);
            collector.checkThat(beaconIds, hasSize(2));
            collector.checkThat(beaconIds, containsInAnyOrder(a, b));

            List<String> chromosomes = from(r.asString()).getList("query.chromosome", String.class);
            collector.checkThat(chromosomes, everyItem(containsString(q.getChromosome())));
            List<Long> positions = from(r.asString()).getList("query.position", Long.class);
            collector.checkThat(positions, everyItem(equalTo(q.getPosition())));
            List<String> alleles = from(r.asString()).getList("query.allele", String.class);
            collector.checkThat(alleles, everyItem(equalTo(q.getAllele())));
            List<String> references = from(r.asString()).getList("query.reference", String.class);
            collector.checkThat(references, everyItem(equalTo(q.getReference())));
        });
    }
}
