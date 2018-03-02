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

import com.dnastack.bob.rest.util.Parameter;
import com.dnastack.bob.rest.util.ParameterRule;
import com.dnastack.bob.rest.util.QueryEntry;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.xml.XmlPath;
import com.jayway.restassured.response.Response;
import lombok.extern.java.Log;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.dnastack.bob.rest.util.DataProvider.getQueries;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Test of responses.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RunWith(Arquillian.class)
@RunAsClient
@Log
public class BeaconSingleResponseTest extends AbstractResponseTest {

    public static final List<QueryEntry> QUERIES = getQueries();

    @Rule
    public final ParameterRule<QueryEntry> queryRule = new ParameterRule<>(QUERIES);

    @Parameter
    private QueryEntry query;

    @Test
    public void testResponse(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        log.info(String.format("Testing query: %s", query));

        Response r = given().baseUri(url.toExternalForm()).accept(contentType).when().get(getUrl(query, false));
        collector.checkThat(r.getStatusCode(), equalTo(Status.OK.getStatusCode()));
        collector.checkThat(r.getContentType(), equalTo(contentType.toString()));

        String beaconId;
        Boolean response;
        if (ContentType.JSON.equals(contentType)) {
            beaconId = JsonPath.from(r.asString()).getString("beacon.id");
            response = JsonPath.from(r.asString()).getObject("response", Boolean.class);
        } else {
            beaconId = XmlPath.from(r.asString()).getString("beacon-response.beacon.id");
            response = XmlPath.from(r.asString()).getObject("beacon-response.response", Boolean.class);
        }

        collector.checkThat(beaconId, notNullValue());
        collector.checkThat(beaconId, equalTo(query.getBeacon()));

        collector.checkThat(response, equalTo(query.getResponse()));

        log.info(String.format("Beacon: " + query.getBeacon() + " - expected response: %s; actual response: %s",
                               query.getResponse(),
                               response));
    }
}
