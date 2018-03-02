/* 
 * Copyright 2018 DNAstack.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dnastack.bob.rest;

import com.dnastack.bob.rest.util.QueryEntry;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.xml.XmlPath;
import com.jayway.restassured.response.Response;
import lombok.extern.java.Log;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.dnastack.bob.rest.util.DataProvider.getBeacons;
import static com.dnastack.bob.rest.util.DataProvider.getQueries;
import static com.jayway.restassured.RestAssured.given;
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

        given().baseUri(url.toExternalForm())
               .accept(contentType)
               .when()
               .get(getUrl(q, true))
               .then()
               .statusCode(Status.OK.getStatusCode())
               .contentType(contentType)
               .body(ContentType.JSON.equals(contentType) ? "beacon.id" : "collection.beacon-response.beacon.id",
                     hasItems(getBeacons().toArray()))
               .body(ContentType.JSON.equals(contentType)
                     ? "query.chromosome"
                     : "collection.beacon-response.query.chromosome", everyItem(containsString(q.getChromosome())))
               .body(ContentType.JSON.equals(contentType)
                     ? "query.position"
                     : "collection.beacon-response.query.position",
                     everyItem(equalTo(ContentType.JSON.equals(contentType)
                                       ? q.getPosition().intValue()
                                       : q.getPosition().toString())))
               .body(ContentType.JSON.equals(contentType) ? "query.allele" : "collection.beacon-response.query.allele",
                     everyItem(equalTo(q.getAllele())))
               .body(ContentType.JSON.equals(contentType)
                     ? "query.reference"
                     : "collection.beacon-response.query.reference", everyItem(equalTo(q.getReference())));
    }

    @Test
    public void testResponsesFiltered(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        getBeacons().stream().forEach((String b) -> {
            QueryEntry query = (QueryEntry) getQueries(b).toArray()[0];
            log.info(String.format("Testing query: %s", query));

            Response r = given().baseUri(url.toExternalForm()).accept(contentType).when().get(getUrl(query, true));
            collector.checkThat(r.getStatusCode(), equalTo(Status.OK.getStatusCode()));
            collector.checkThat(r.getContentType(), equalTo(contentType.toString()));

            List<String> beaconIds;
            List<Boolean> responses;
            if (ContentType.JSON.equals(contentType)) {
                beaconIds = JsonPath.from(r.asString()).getList("beacon.id", String.class);
                responses = JsonPath.from(r.asString()).getList("response", Boolean.class);
            } else {
                beaconIds = XmlPath.from(r.asString()).getList("collection.beacon-response.beacon.id", String.class);
                responses = XmlPath.from(r.asString()).getList("collection.beacon-response.response", Boolean.class);
            }

            collector.checkThat(beaconIds, hasSize(1));
            collector.checkThat(beaconIds, hasItem(query.getBeacon()));

            collector.checkThat(responses, hasSize(1));
            collector.checkThat(responses, hasItem(query.getResponse()));

            log.info(String.format("Beacon: " + query.getBeacon() + " - expected response: %s; actual response: %s",
                                   query.getResponse(),
                                   responses.get(0)));
        });
    }

    @Test
    public void testMultipleResponsesFiltered(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        QueryEntry q = new QueryEntry(getQueries().get(0));
        String a = q.getBeacon();

        getBeacons().stream().filter((String b) -> !a.equals(b)).forEach((String b) -> {
            q.setBeacon(String.format("[%s,%s]", a, b));
            log.info(String.format("Testing query: %s", q));

            Response r = given().baseUri(url.toExternalForm()).accept(contentType).when().get(getUrl(q, true));
            collector.checkThat(r.getStatusCode(), equalTo(Status.OK.getStatusCode()));
            collector.checkThat(r.getContentType(), equalTo(contentType.toString()));

            List<String> beaconIds;
            List<String> chromosomes;
            List<Long> positions;
            List<String> alleles;
            List<String> references;
            if (ContentType.JSON.equals(contentType)) {
                beaconIds = JsonPath.from(r.asString()).getList("beacon.id", String.class);
                chromosomes = JsonPath.from(r.asString()).getList("query.chromosome", String.class);
                positions = JsonPath.from(r.asString()).getList("query.position", Long.class);
                alleles = JsonPath.from(r.asString()).getList("query.allele", String.class);
                references = JsonPath.from(r.asString()).getList("query.reference", String.class);
            } else {
                beaconIds = XmlPath.from(r.asString()).getList("collection.beacon-response.beacon.id", String.class);
                chromosomes = XmlPath.from(r.asString())
                                     .getList("collection.beacon-response.query.chromosome", String.class);
                positions = XmlPath.from(r.asString()).getList("collection.beacon-response.query.position", Long.class);
                alleles = XmlPath.from(r.asString()).getList("collection.beacon-response.query.allele", String.class);
                references = XmlPath.from(r.asString())
                                    .getList("collection.beacon-response.query.reference", String.class);
            }

            collector.checkThat(beaconIds, hasSize(2));
            collector.checkThat(beaconIds, containsInAnyOrder(a, b));

            collector.checkThat(chromosomes, everyItem(containsString(q.getChromosome())));
            collector.checkThat(positions, everyItem(equalTo(q.getPosition())));
            collector.checkThat(alleles, everyItem(equalTo(q.getAllele())));
            collector.checkThat(references, everyItem(equalTo(q.getReference())));
        });
    }
}
