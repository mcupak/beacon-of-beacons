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

import com.dnastack.bob.rest.util.ArquillianUtils;
import com.dnastack.bob.rest.util.BeaconResponseTestUtils;
import com.dnastack.bob.rest.util.DataProvider;
import com.dnastack.bob.rest.util.Parameter;
import com.dnastack.bob.rest.util.ParameterRule;
import com.dnastack.bob.rest.util.QueryEntry;
import com.dnastack.bob.service.fetcher.util.HttpUtils;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import lombok.extern.log4j.Log4j;
import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.archive.importer.MavenImporter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * Base test class to be extended by other tests.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Log4j
public abstract class BasicTest {

    private static final HttpUtils httpUtils = new HttpUtils();

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            log.info("Starting test: " + description.getClassName() + " - " + description.getMethodName() + "()");
        }
    };

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(MavenImporter.class)
            .loadPomFromFile("pom.xml")
            .importBuildOutput()
            .as(WebArchive.class)
            .addClasses(BasicTest.class, AbstractResponseTest.class, ParameterRule.class, ArquillianUtils.class, Parameter.class, BeaconResponseTestUtils.class, DataProvider.class, QueryEntry.class)
            .addAsResource("queries.json");
        log.info(String.format("WAR name: %s", war.getName()));

        return war;
    }

    public static Object readObject(Class<?> c, String url) throws JAXBException, MalformedURLException {
        JAXBContext jc = JAXBContext.newInstance(c);

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        unmarshaller.setProperty(JAXBContextProperties.MEDIA_TYPE, "application/json");
        unmarshaller.setProperty(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
        StreamSource source = new StreamSource(url);
        JAXBElement jaxbElement = unmarshaller.unmarshal(source, c);

        return jaxbElement.getValue();
    }

    public static String readResponse(String url) {
        try {
            return httpUtils.executeRequest(httpUtils.createRequest(url, false, null, null));
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }

    private static String readField(JSONObject field, List<String> path) {
        for (int i = 1; i < path.size(); i++) {
            field = field.getJSONObject(path.get(i - 1));
        }

        String loc = path.get(path.size() - 1);
        String res;
        try {
            res = field.getString(loc);
        } catch (JSONException je) {
            try {
                res = String.valueOf(field.getLong(loc));
            } catch (JSONException je2) {
                try {
                    res = String.valueOf(field.getInt(loc));
                } catch (JSONException je3) {
                    try {
                        res = String.valueOf(field.getBoolean(loc));
                    } catch (JSONException je4) {
                        res = null;
                    }
                }
            }
        }

        return res;
    }

    public static String readField(String response, List<String> path) {
        JSONObject field = new JSONObject(response);

        return readField(field, path);
    }

    public static List<String> readFieldArray(String response, List<String> path) {
        List<String> fields = new ArrayList<>();

        JSONArray elems = new JSONArray(response);
        for (int i = 0; i < elems.length(); i++) {
            fields.add(readField(elems.getJSONObject(i), path));
        }

        return fields;
    }

}
