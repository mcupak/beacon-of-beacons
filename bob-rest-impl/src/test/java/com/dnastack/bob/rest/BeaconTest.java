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

import com.dnastack.bob.service.dto.BeaconDto;
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
import java.util.Set;

import static com.dnastack.bob.rest.util.DataProvider.getBeacons;
import static com.dnastack.bob.rest.util.RequestUtils.encode;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Test of beacons info.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RunWith(Arquillian.class)
@RunAsClient
@Log
public class BeaconTest extends BasicTest {

    public static final String BEACONS_TEMPLATE = "beacons";
    public static final String BEACONS_FILTERED_TEMPLATE = "beacons?beacon=%s";
    public static final String BEACON_TEMPLATE = "beacons/%s";

    private static String getUrl() {
        return BEACONS_TEMPLATE;
    }

    private static String getUrl(String beaconId, boolean param) {
        return String.format(param ? BEACONS_FILTERED_TEMPLATE : BEACON_TEMPLATE, beaconId);
    }

    @Test
    public void testAllBeacons(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        given().baseUri(url.toExternalForm())
               .accept(contentType)
               .when()
               .get((getUrl()))
               .then()
               .statusCode(Status.OK.getStatusCode())
               .contentType(contentType)
               .body(ContentType.JSON.equals(contentType) ? "id" : "collection.beacon.id",
                     hasItems(getBeacons().toArray()));
    }

    @Test
    public void testBeaconsFiltered(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        getBeacons().stream().forEach((String id) -> {
            log.info(String.format("Testing beacon: %s", id));
            Response r = given().baseUri(url.toExternalForm()).accept(contentType).when().get(getUrl(encode(id), true));
            collector.checkThat(r.getStatusCode(), equalTo(Status.OK.getStatusCode()));
            collector.checkThat(r.getContentType(), equalTo(contentType.toString()));


            List<String> ids = ContentType.JSON.equals(contentType)
                               ? JsonPath.from(r.asString())
                                         .getList("id", String.class)
                               : XmlPath.from(r.asString()).getList("collection.beacon.id", String.class);
            collector.checkThat(ids, hasSize(1));
            collector.checkThat(ids, hasItem(id));
        });
    }

    @Test
    public void testMultipleBeaconsFiltered(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Set<String> bs = getBeacons();
        String a = (String) bs.toArray()[0];

        bs.stream().filter((String b) -> !a.equals(b)).forEach((String b) -> {
            log.info(String.format("Testing beacons: %s, %s", a, b));
            Response r = given().baseUri(url.toExternalForm())
                                .accept(contentType)
                                .when()
                                .get(getUrl("[" + encode(a) + "," + encode(b) + "]", true));
            collector.checkThat(r.getStatusCode(), equalTo(Status.OK.getStatusCode()));
            collector.checkThat(r.getContentType(), equalTo(contentType.toString()));

            List<String> ids = ContentType.JSON.equals(contentType)
                               ? JsonPath.from(r.asString())
                                         .getList("id", String.class)
                               : XmlPath.from(r.asString()).getList("collection.beacon.id", String.class);
            collector.checkThat(ids, hasSize(2));
            collector.checkThat(ids, containsInAnyOrder(a, b));
        });
    }

    @Test
    public void testBeacon(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        getBeacons().stream().forEach((String id) -> {
            log.info(String.format("Testing beacon: %s", id));
            Response r = given().baseUri(url.toExternalForm())
                                .accept(contentType)
                                .when()
                                .get(getUrl(encode(id), false));
            collector.checkThat(r.getStatusCode(), equalTo(Status.OK.getStatusCode()));
            collector.checkThat(r.getContentType(), equalTo(contentType.toString()));

            BeaconDto b = r.as(BeaconDto.class);
            collector.checkThat(b, notNullValue());
            collector.checkThat(b.getId(), equalTo(id));
        });
    }
}
