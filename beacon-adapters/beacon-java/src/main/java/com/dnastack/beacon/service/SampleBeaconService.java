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
package com.dnastack.beacon.service;

import com.dnastack.beacon.entity.Beacon;
import com.dnastack.beacon.entity.BeaconResponse;
import com.dnastack.beacon.entity.Chromosome;
import com.dnastack.beacon.entity.Dataset;
import com.dnastack.beacon.entity.Error;
import com.dnastack.beacon.entity.Query;
import com.dnastack.beacon.entity.Reference;
import com.dnastack.beacon.entity.Response;
import com.dnastack.beacon.util.QueryUtils;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

/**
 * Sample implementation of a beacon service.
 *
 * TODO: Replace this class with your actual beacon.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RequestScoped
public class SampleBeaconService implements BeaconService {

    private Dataset dataset;
    private List<Dataset> datasets;

    private Query query;
    private List<Query> queries;

    private Beacon beacon;

    @PostConstruct
    public void init() {
        this.dataset = new Dataset("id", "description", "reference", null, null);
        this.query = new Query("allele string", Chromosome.CHR1, 1L, Reference.HG19, "dataset Id");

        this.queries = new ArrayList<>();
        this.queries.add(query);
        this.datasets = new ArrayList<>();
        this.datasets.add(dataset);

        this.beacon = new Beacon("foo", "bar", "org", "sample beacon", "0.1/0.2", "http://dnastack.com/ga4gh/bob/", "beacon@dnastack.com", "oauth2", datasets, queries);
    }

    @Override
    public BeaconResponse query(String chrom, Long pos, String allele, String ref, String dataset) {

        // required parameters are missing
        if (chrom == null || pos == null || allele == null || ref == null) {
            Error errorResource = new Error("Incomplete Query", "Required parameters are missing");
            Response responseResource = new Response(null, null, null, null, errorResource);
            BeaconResponse response = new BeaconResponse(beacon.getId(), QueryUtils.getQuery(chrom, pos, allele, ref, dataset), responseResource);
            return response;
        }

        BeaconResponse response = new BeaconResponse(beacon.getId(), QueryUtils.getQuery(chrom, pos, allele, ref, dataset), null);
        Response responseResource = new Response(true, 0, null, "bla", null);

        // generate a sample response
        response.setResponse(responseResource);

        return response;
    }

    @Override
    public Beacon info() {
        return beacon;
    }

}
