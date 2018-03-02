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
