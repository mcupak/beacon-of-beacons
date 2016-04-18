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

import com.dnastack.bob.service.dto.AlleleDto;
import com.dnastack.bob.service.dto.ChromosomeDto;
import com.dnastack.bob.service.dto.ReferenceDto;
import com.jayway.restassured.http.ContentType;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsInAnyOrder;

/**
 * Test of chromosome, reference and allele info.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RunWith(Arquillian.class)
@RunAsClient
public class ItemTest extends BasicTest {

    public static final String CHROMOSOMES_TEMPLATE = "chromosomes";
    public static final String REFERENCES_TEMPLATE = "references";
    public static final String ALLELES_TEMPLATE = "alleles";

    @Test
    public void testAllChromosomes(@ArquillianResource URL url) throws JAXBException, MalformedURLException, IOException {
        when().get((url.toExternalForm() + CHROMOSOMES_TEMPLATE)).then().statusCode(Response.Status.OK.getStatusCode()).contentType(ContentType.JSON).body("", containsInAnyOrder(Arrays.asList(ChromosomeDto.values()).parallelStream().map(chr -> chr.toString()).collect(Collectors.toSet()).toArray()));
    }

    @Test
    public void testAllReferences(@ArquillianResource URL url) throws JAXBException, MalformedURLException, IOException {
        when().get((url.toExternalForm() + REFERENCES_TEMPLATE)).then().statusCode(Response.Status.OK.getStatusCode()).contentType(ContentType.JSON).body("", containsInAnyOrder(Arrays.asList(ReferenceDto.values()).parallelStream().map(r -> r.toString()).collect(Collectors.toSet()).toArray()));
    }

    @Test
    public void testAllAlleles(@ArquillianResource URL url) throws JAXBException, MalformedURLException, IOException {
        when().get((url.toExternalForm() + ALLELES_TEMPLATE)).then().statusCode(Response.Status.OK.getStatusCode()).contentType(ContentType.JSON).body("", containsInAnyOrder(Arrays.asList(AlleleDto.values()).parallelStream().map(a -> a.toString()).collect(Collectors.toSet()).toArray()));
    }

}
