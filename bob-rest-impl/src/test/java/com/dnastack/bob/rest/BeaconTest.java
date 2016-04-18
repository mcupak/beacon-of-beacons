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

import com.dnastack.bob.service.dto.BeaconDto;
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
import java.util.Set;

import static com.dnastack.bob.rest.util.DataProvider.getBeacons;
import static com.dnastack.bob.rest.util.RequestUtils.encode;
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.path.json.JsonPath.from;
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
        when().get((url.toExternalForm() + getUrl())).then().statusCode(Response.Status.OK.getStatusCode()).contentType(ContentType.JSON).body("id", hasItems(getBeacons().toArray()));
    }

    @Test
    public void testBeaconsFiltered(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        getBeacons().stream().forEach((String id) -> {
            log.info(String.format("Testing beacon: %s", id));
            com.jayway.restassured.response.Response r = get((url.toExternalForm() + getUrl(encode(id), true)));
            collector.checkThat(r.getStatusCode(), equalTo(Response.Status.OK.getStatusCode()));
            collector.checkThat(r.getContentType(), equalTo(ContentType.JSON.toString()));

            List<String> ids = from(r.asString()).getList("id", String.class);
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
            com.jayway.restassured.response.Response r = get((url.toExternalForm() + getUrl("[" + encode(a) + "," + encode(b) + "]", true)));
            collector.checkThat(r.getStatusCode(), equalTo(Response.Status.OK.getStatusCode()));
            collector.checkThat(r.getContentType(), equalTo(ContentType.JSON.toString()));

            List<String> ids = from(r.asString()).getList("id", String.class);
            collector.checkThat(ids, hasSize(2));
            collector.checkThat(ids, containsInAnyOrder(a, b));
        });
    }

    @Test
    public void testBeacon(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        getBeacons().stream().forEach((String id) -> {
            log.info(String.format("Testing beacon: %s", id));
            com.jayway.restassured.response.Response r = get((url.toExternalForm() + getUrl(encode(id), false)));
            collector.checkThat(r.getStatusCode(), equalTo(Response.Status.OK.getStatusCode()));
            collector.checkThat(r.getContentType(), equalTo(ContentType.JSON.toString()));

            BeaconDto b = r.as(BeaconDto.class);
            collector.checkThat(b, notNullValue());
            collector.checkThat(b.getId(), equalTo(id));
        });
    }
}
