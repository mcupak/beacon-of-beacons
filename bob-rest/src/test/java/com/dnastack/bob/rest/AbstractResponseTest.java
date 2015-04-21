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
import com.dnastack.bob.service.dto.BeaconResponseTo;
import com.dnastack.bob.service.dto.ChromosomeTo;
import com.dnastack.bob.service.dto.QueryTo;
import com.dnastack.bob.service.dto.ReferenceTo;
import com.google.common.collect.ImmutableList;
import java.net.MalformedURLException;
import java.util.List;
import javax.xml.bind.JAXBException;

import static com.dnastack.bob.rest.util.DataProvider.isQueryForMultipleBeacons;

/**
 * Test of a beacon service.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public abstract class AbstractResponseTest extends BasicTest {

    public static final String QUERY_BEACON_FILTER_TEMPLATE = "rest/responses?beacon=%s&chrom=%s&pos=%s&allele=%s";
    public static final String QUERY_BEACON_FILTER_WITH_REF_TEMPLATE = "rest/responses?beacon=%s&chrom=%s&pos=%s&allele=%s&ref=%s";
    public static final String QUERY_TEMPLATE = "rest/responses?chrom=%s&pos=%s&allele=%s";
    public static final String QUERY_WITH_REF_TEMPLATE = "rest/responses?chrom=%s&pos=%s&allele=%s&ref=%s";
    public static final String QUERY_BEACON_TEMPLATE = "rest/responses/%s?chrom=%s&pos=%s&allele=%s";
    public static final String QUERY_BEACON_WITH_REF_TEMPLATE = "rest/responses/%s?chrom=%s&pos=%s&allele=%s&ref=%s";
    // paths for jettisson (not jackson)
    public static final String BEACON_RESPONSE = "beaconResponse";
    public static final List<String> BEACON_PATH = ImmutableList.of(BEACON_RESPONSE, "beacon", "id");
    public static final List<String> RESPONSE_PATH = ImmutableList.of(BEACON_RESPONSE, "response");
    public static final String QUERY = "query";
    public static final List<String> POS_PATH = ImmutableList.of(BEACON_RESPONSE, QUERY, "position");
    public static final List<String> CHROM_PATH = ImmutableList.of(BEACON_RESPONSE, QUERY, "chromosome");
    public static final List<String> ALLELE_PATH = ImmutableList.of(BEACON_RESPONSE, QUERY, "allele");
    public static final List<String> REF_PATH = ImmutableList.of(BEACON_RESPONSE, QUERY, "reference");

    protected static String getUrl(QueryEntry q, boolean filter) {
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

    @SuppressWarnings("unchecked")
    public static List<BeaconResponseTo> readBeaconResponses(String url) throws JAXBException, MalformedURLException {
        return (List<BeaconResponseTo>) readObject(BeaconResponseTo.class, url);
    }

    protected static BeaconResponseTo readBeaconResponse(String url) throws JAXBException, MalformedURLException {
        return (BeaconResponseTo) readObject(BeaconResponseTo.class, url);
    }

    protected static String readBeaconId(String response) {
        return readField(response, BEACON_PATH);
    }

    protected static Long readPos(String response) {
        return Long.valueOf(readField(response, POS_PATH));
    }

    protected static ChromosomeTo readChrom(String response) {
        return ChromosomeTo.fromString(readField(response, CHROM_PATH).substring(3));
    }

    protected static String readAllele(String response) {
        return readField(response, ALLELE_PATH);
    }

    protected static ReferenceTo readRef(String response) {
        return ReferenceTo.fromString(readField(response, REF_PATH));
    }

    protected static QueryTo readQuery(String response) {
        return new QueryTo(readChrom(response), readPos(response), readAllele(response), readRef(response));
    }

    protected static Boolean readResponsePredicate(String response) {
        return Boolean.valueOf(readField(response, RESPONSE_PATH));
    }

}
