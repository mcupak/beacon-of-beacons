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

import com.dnastack.bob.dto.BeaconResponseTo;
import com.dnastack.bob.dto.QueryTo;
import com.dnastack.bob.dto.ChromosomeTo;
import com.dnastack.bob.dto.ReferenceTo;
import com.google.common.collect.ImmutableList;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.xml.bind.JAXBException;

/**
 * Test of a beacon service.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public abstract class AbstractResponseTest extends BasicTest {

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

    /**
     * Construct URL for the given BeaconResponseTo.
     *
     * @param beacon beacon id
     * @param params parameter array containing the following elements as strings: chrom, pos, allele, ref
     *               (respectively)
     *
     * @return url
     */
    protected static String getUrl(String beacon, String[] params) {
        String res = null;
        if (params.length == 4) {
            if (params[3] == null) {
                res = String.format(QUERY_BEACON_TEMPLATE, beacon, params[0], params[1], params[2]);
            } else {
                res = String.format(QUERY_BEACON_WITH_REF_TEMPLATE, beacon, params[0], params[1], params[2], params[3]);
            }
        }

        return res;
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

    public abstract void testAllRefsFound(URL url) throws JAXBException, MalformedURLException;

    public abstract void testAllRefsNotFound(URL url) throws JAXBException, MalformedURLException;

    public abstract void testSpecificRefFound(URL url) throws JAXBException, MalformedURLException;

    public abstract void testSpecificRefNotFound(URL url) throws JAXBException, MalformedURLException;

    public abstract void testInvalidRef(URL url) throws JAXBException, MalformedURLException;

    public abstract void testRefConversion(URL url) throws JAXBException, MalformedURLException;

    public abstract void testDifferentGenome(URL url) throws JAXBException, MalformedURLException;

    public abstract void testStringAllele(URL url) throws JAXBException, MalformedURLException;

    public abstract void testDel(URL url) throws JAXBException, MalformedURLException;

    public abstract void testIns(URL url) throws JAXBException, MalformedURLException;

    public abstract void testInvalidAllele(URL url) throws JAXBException, MalformedURLException;

    public abstract void testAlleleConversion(URL url) throws JAXBException, MalformedURLException;

    public abstract void testChromConversion(URL url) throws JAXBException, MalformedURLException;

    public abstract void testInvalidChrom(URL url) throws JAXBException, MalformedURLException;

    public abstract void testChromX(URL url) throws JAXBException, MalformedURLException;

    public abstract void testChromMT(URL url) throws JAXBException, MalformedURLException;

}
