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
package com.dnastack.bob.persistence;

import lombok.extern.java.Log;
import org.assertj.core.api.Condition;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Log
public abstract class BasicDaoTest {

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            log.info("Starting test: " + description.getClassName() + " - " + description.getMethodName() + "()");
        }
    };

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
                                   .addPackages(true,
                                                "com.dnastack.bob.persistence",
                                                "org.assertj.core",
                                                "com.google.common")
                                   .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                                   .addAsWebInfResource("jbossas-ds.xml")
                                   .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        return war;
    }

    public Condition getNullCondition() {
        return new Condition("null") {

            @Override
            public boolean matches(Object value) {
                return value == null;
            }
        };
    }

}
