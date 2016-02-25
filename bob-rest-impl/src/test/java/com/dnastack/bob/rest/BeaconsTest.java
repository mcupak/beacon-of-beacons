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
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.xml.bind.JAXBException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dnastack.bob.rest.util.DataProvider.getBeacons;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Test of beacons info.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RunWith(Arquillian.class)
@RunAsClient
public class BeaconsTest extends BasicTest {

    public static final String BEACONS_TEMPLATE = "beacons";
    public static final String BEACONS_FILTERED_TEMPLATE = "beacons?beacon=%s";
    public static final String BEACON_TEMPLATE = "beacons/%s";

    public static String getUrl() {
        return BEACONS_TEMPLATE;
    }

    public static String getUrl(String beaconId, boolean param) {
        return String.format(param ? BEACONS_FILTERED_TEMPLATE : BEACON_TEMPLATE, beaconId);
    }

    @SuppressWarnings("unchecked")
    public static List<BeaconDto> readBeacons(String url) throws JAXBException, MalformedURLException {
        return (List<BeaconDto>) readObject(BeaconDto.class, url);
    }

    public static BeaconDto readBeacon(String url) throws JAXBException, MalformedURLException {
        return (BeaconDto) readObject(BeaconDto.class, url);
    }

    @Test
    public void testAllBeacons(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        List<BeaconDto> bs = readBeacons(url.toExternalForm() + getUrl());
        Set<String> ids = bs.stream().map((BeaconDto b) -> b.getId()).collect(Collectors.toSet());

        assertThat(getBeacons(), everyItem(isIn(ids)));
    }

    @Test
    public void testBeaconsFiltered(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        for (String id : getBeacons()) {
            List<BeaconDto> beacons = readBeacons(url.toExternalForm() + getUrl(id, true));

            collector.checkThat(beacons, notNullValue());
            collector.checkThat(beacons.get(0).getId(), equalTo(id));
        }
    }

    @Test
    public void testMultipleBeaconsFiltered(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        Set<String> bs = getBeacons();
        String a = (String) bs.toArray()[0];

        for (String b : bs) {
            if (!a.equals(b)) {
                List<BeaconDto> beacons = readBeacons(url.toExternalForm() + getUrl("[" + a + "," + b + "]", true));

                collector.checkThat(beacons, notNullValue());
                collector.checkThat(beacons.size(), equalTo(2));
                collector.checkThat((beacons.get(0).getId().equals(a) || beacons.get(0).getId().equals(b)) && (beacons.get(1).getId().equals(a) || beacons.get(1).getId().equals(b)), equalTo(true));
            }
        }

    }

    @Test
    public void testBeacon(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        for (String id : getBeacons()) {
            BeaconDto b = readBeacon(url.toExternalForm() + getUrl(id, false));

            collector.checkThat(b, notNullValue());
            collector.checkThat(b.getId(), equalTo(id));
        }
    }
}
