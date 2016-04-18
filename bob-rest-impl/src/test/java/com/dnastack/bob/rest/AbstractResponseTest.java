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
            String collect = Arrays.asList(beaconIds.substring(1, beaconIds.length() - 1).split(",")).stream().map((String b) -> encode(b)).collect(Collectors.joining(","));
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
                res = String.format(QUERY_WITH_REF_TEMPLATE, q.getChromosome(), q.getPosition(), q.getAllele(), q.getReference());
            }
        } else if (isQueryForMultipleBeacons(q) || filter) {
            // filtering
            if (q.getReference() == null) {
                res = String.format(QUERY_BEACON_FILTER_TEMPLATE, q.getBeacon(), q.getChromosome(), q.getPosition(), q.getAllele());
            } else {
                res = String.format(QUERY_BEACON_FILTER_WITH_REF_TEMPLATE, q.getBeacon(), q.getChromosome(), q.getPosition(), q.getAllele(), q.getReference());
            }
        } else {
            if (q.getReference() == null) {
                res = String.format(QUERY_BEACON_TEMPLATE, q.getBeacon(), q.getChromosome(), q.getPosition(), q.getAllele());
            } else {
                res = String.format(QUERY_BEACON_WITH_REF_TEMPLATE, q.getBeacon(), q.getChromosome(), q.getPosition(), q.getAllele(), q.getReference());
            }
        }

        return res;
    }

}
