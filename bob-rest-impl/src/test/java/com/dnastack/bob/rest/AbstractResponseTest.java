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

import com.dnastack.bob.rest.util.QueryEntry;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.dnastack.bob.rest.util.DataProvider.isQueryForMultipleBeacons;
import static com.dnastack.bob.rest.util.RequestUtils.encode;

/**
 * Test of a beacon service.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public abstract class AbstractResponseTest extends BasicTest {

    public static final String QUERY_BEACON_FILTER_TEMPLATE = "responses?beacon=%s&chrom=%s&pos=%s&allele=%s";
    public static final String QUERY_BEACON_FILTER_WITH_REF_TEMPLATE = "responses?beacon=%s&chrom=%s&pos=%s&allele=%s&ref=%s";
    public static final String QUERY_TEMPLATE = "responses?chrom=%s&pos=%s&allele=%s";
    public static final String QUERY_WITH_REF_TEMPLATE = "responses?chrom=%s&pos=%s&allele=%s&ref=%s";
    public static final String QUERY_BEACON_TEMPLATE = "responses/%s?chrom=%s&pos=%s&allele=%s";
    public static final String QUERY_BEACON_WITH_REF_TEMPLATE = "responses/%s?chrom=%s&pos=%s&allele=%s&ref=%s";

    private static String encodeBeaconIds(String beaconIds) {
        if (beaconIds == null) {
            return null;
        }

        String res;
        if (beaconIds.startsWith("[") && beaconIds.endsWith("]")) {
            String collect = Arrays.asList(beaconIds.substring(1, beaconIds.length() - 1).split(","))
                                   .stream()
                                   .map((String b) -> encode(b))
                                   .collect(Collectors.joining(","));
            res = "[" + collect + "]";
        } else {
            res = encode(beaconIds);
        }

        return res;
    }

    protected static String getUrl(QueryEntry q, boolean filter) {
        q.setBeacon(encodeBeaconIds(q.getBeacon()));

        String res;
        if (q.getBeacon() == null || q.getBeacon().isEmpty()) {
            // query all beacons
            if (q.getReference() == null) {
                res = String.format(QUERY_TEMPLATE, q.getChromosome(), q.getPosition(), q.getAllele());
            } else {
                res = String.format(QUERY_WITH_REF_TEMPLATE,
                                    q.getChromosome(),
                                    q.getPosition(),
                                    q.getAllele(),
                                    q.getReference());
            }
        } else if (isQueryForMultipleBeacons(q) || filter) {
            // filtering
            if (q.getReference() == null) {
                res = String.format(QUERY_BEACON_FILTER_TEMPLATE,
                                    q.getBeacon(),
                                    q.getChromosome(),
                                    q.getPosition(),
                                    q.getAllele());
            } else {
                res = String.format(QUERY_BEACON_FILTER_WITH_REF_TEMPLATE,
                                    q.getBeacon(),
                                    q.getChromosome(),
                                    q.getPosition(),
                                    q.getAllele(),
                                    q.getReference());
            }
        } else {
            if (q.getReference() == null) {
                res = String.format(QUERY_BEACON_TEMPLATE,
                                    q.getBeacon(),
                                    q.getChromosome(),
                                    q.getPosition(),
                                    q.getAllele());
            } else {
                res = String.format(QUERY_BEACON_WITH_REF_TEMPLATE,
                                    q.getBeacon(),
                                    q.getChromosome(),
                                    q.getPosition(),
                                    q.getAllele(),
                                    q.getReference());
            }
        }

        return res;
    }

}
