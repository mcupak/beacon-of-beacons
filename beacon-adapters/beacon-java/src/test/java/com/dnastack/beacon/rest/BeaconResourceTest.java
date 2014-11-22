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
package com.dnastack.beacon.rest;

import com.dnastack.beacon.entity.BeaconResponse;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.archive.importer.MavenImporter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Beacon test.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 */
@RunWith(Arquillian.class)
@RunAsClient
public class BeaconResourceTest {

    private static final String BEACON = "foo";
    public static final String QUERY_BEACON_TEMPLATE = "rest/query?chrom=%s&pos=%s&allele=%s&ref=%s";

    protected static String getUrl(String beacon, String[] params) {
        String res = null;
        if (params.length == 4) {
            res = String.format(QUERY_BEACON_TEMPLATE, params[0], params[1], params[2], params[3]);
        }

        return res;
    }

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            System.out.println("Starting test: " + description.getClassName() + " - " + description.getMethodName() + "()");
        }
    };

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(MavenImporter.class)
                .loadPomFromFile("pom.xml").importBuildOutput().as(WebArchive.class);
        System.out.println("WAR name: " + war.getName());

        return war;
    }

    public static Object readObject(Class c, String url) throws JAXBException, MalformedURLException {
        JAXBContext jc = JAXBContext.newInstance(c);

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        unmarshaller.setProperty(JAXBContextProperties.MEDIA_TYPE, "application/json");
        unmarshaller.setProperty(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
        StreamSource source = new StreamSource(url);
        JAXBElement jaxbElement = unmarshaller.unmarshal(source, c);

        return jaxbElement.getValue();
    }

    protected static BeaconResponse readBeaconResponse(String url) throws JAXBException, MalformedURLException {
        return (BeaconResponse) readObject(BeaconResponse.class, url);
    }

    @Test
    public void testTrueQuery(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"1", "808922", "A", null};
        BeaconResponse br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertTrue(br.getResponse());
    }

    @Test
    public void testFalseQuery(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        String[] query = {"1", "808921", "A", null};
        BeaconResponse br = readBeaconResponse(url.toExternalForm() + getUrl(BEACON, query));

        assertNotNull(br);
        assertFalse(br.getResponse());
    }

}
