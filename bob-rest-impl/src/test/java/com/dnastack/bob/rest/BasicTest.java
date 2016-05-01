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

import com.dnastack.bob.rest.util.*;
import com.google.common.collect.ImmutableList;
import com.jayway.restassured.http.ContentType;
import lombok.extern.java.Log;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.archive.importer.MavenImporter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.File;

/**
 * Base test class to be extended by other tests.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Log
public abstract class BasicTest {

    @Rule
    public final TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            log.info("Starting test: " + description.getClassName() + " - " + description.getMethodName() + "()");
        }
    };

    @Rule
    public final ErrorCollector collector = new ErrorCollector();

    @Rule
    public final ParameterRule<ContentType> contentTypeParameterRule = new ParameterRule<>(ImmutableList.of(ContentType.JSON,
                                                                                                            ContentType.XML));

    @Parameter
    protected ContentType contentType;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        File[] libs = Maven.resolver()
                           .loadPomFromFile("pom.xml")
                           .resolve("com.jayway.restassured:rest-assured", "junit:junit", "com.google.code.gson:gson")
                           .withTransitivity()
                           .asFile();
        WebArchive war = ShrinkWrap.create(MavenImporter.class)
                                   .loadPomFromFile("pom.xml")
                                   .importBuildOutput()
                                   .as(WebArchive.class)
                                   .addClasses(BasicTest.class,
                                               AbstractResponseTest.class,
                                               ParameterRule.class,
                                               ArquillianUtils.class,
                                               Parameter.class,
                                               DataProvider.class,
                                               QueryEntry.class)
                                   .addAsLibraries(libs)
                                   .addAsResource("queries.json", "queries-quick.json");
        log.info(String.format("WAR name: %s", war.getName()));

        return war;
    }

    @Before
    public void logContentType() {
        log.info("Content type: " + contentType);
    }

}
