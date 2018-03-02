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

import com.dnastack.bob.service.dto.AlleleDto;
import com.dnastack.bob.service.dto.ChromosomeDto;
import com.dnastack.bob.service.dto.ReferenceDto;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.jayway.restassured.RestAssured.given;
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
    public void testAllChromosomes(@ArquillianResource URL url) throws JAXBException, IOException {
        given().baseUri(url.toExternalForm())
               .accept(contentType)
               .when()
               .get((CHROMOSOMES_TEMPLATE))
               .then()
               .statusCode(Status.OK.getStatusCode())
               .contentType(contentType)
               .body("",
                     containsInAnyOrder(Arrays.asList(ChromosomeDto.values())
                                              .parallelStream()
                                              .map(chr -> chr.toString())
                                              .collect(Collectors.toSet())
                                              .toArray()));
    }

    @Test
    public void testAllReferences(@ArquillianResource URL url) throws JAXBException, IOException {
        given().baseUri(url.toExternalForm())
               .accept(contentType)
               .when()
               .get((REFERENCES_TEMPLATE))
               .then()
               .statusCode(Status.OK.getStatusCode())
               .contentType(contentType)
               .body("",
                     containsInAnyOrder(Arrays.asList(ReferenceDto.values())
                                              .parallelStream()
                                              .map(r -> r.toString())
                                              .collect(Collectors.toSet())
                                              .toArray()));
    }

    @Test
    public void testAllAlleles(@ArquillianResource URL url) throws JAXBException, IOException {
        given().baseUri(url.toExternalForm())
               .accept(contentType)
               .when()
               .get((ALLELES_TEMPLATE))
               .then()
               .statusCode(Status.OK.getStatusCode())
               .contentType(contentType)
               .body("",
                     containsInAnyOrder(Arrays.asList(AlleleDto.values())
                                              .parallelStream()
                                              .map(a -> a.toString())
                                              .collect(Collectors.toSet())
                                              .toArray()));
    }

}
