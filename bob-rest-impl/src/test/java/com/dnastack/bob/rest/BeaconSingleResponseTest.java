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

import com.dnastack.bob.rest.util.Parameter;
import com.dnastack.bob.rest.util.ParameterRule;
import com.dnastack.bob.rest.util.QueryEntry;
import com.jayway.restassured.http.ContentType;
import lombok.extern.java.Log;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.dnastack.bob.rest.util.DataProvider.getQueries;
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.path.json.JsonPath.from;
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
    public final ParameterRule<QueryEntry> rule = new ParameterRule<>(QUERIES);

    @Parameter
    private QueryEntry query;

    @Test
    public void testResponse(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        log.info(String.format("Testing query: %s", query));

        com.jayway.restassured.response.Response r = get((url.toExternalForm() + getUrl(query, false)));
        collector.checkThat(r.getStatusCode(), equalTo(Response.Status.OK.getStatusCode()));
        collector.checkThat(r.getContentType(), equalTo(ContentType.JSON.toString()));

        String beaconId = from(r.asString()).getString("beacon.id");
        collector.checkThat(beaconId, notNullValue());
        collector.checkThat(beaconId, equalTo(query.getBeacon()));

        Boolean response = from(r.asString()).getObject("response", Boolean.class);
        collector.checkThat(response, equalTo(query.getResponse()));

        log.info(String.format("Beacon: " + query.getBeacon() + " - expected response: %s; actual response: %s", query.getResponse(), response));
    }
}
