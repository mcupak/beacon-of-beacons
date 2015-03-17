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

import java.util.ArrayList;
import java.util.List;

import com.dnastack.beacon.entity.Beacon;
import com.dnastack.beacon.entity.BeaconResponse;
import com.dnastack.beacon.entity.resources.DataSetResource;
import com.dnastack.beacon.entity.resources.ErrorResource;
import com.dnastack.beacon.entity.resources.QueryResource;
import com.dnastack.beacon.entity.resources.ResponseResource;
import com.dnastack.beacon.util.QueryUtils;

/**
 * Sample implementation of a beacon service.
 * 
 * TODO: Replace this class with your actual beacon.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class SampleBeaconService implements BeaconService {

	DataSetResource dataSetResource = new DataSetResource("id", "description", "reference", null, null);
	List<DataSetResource> datasets = new ArrayList<DataSetResource>();

	QueryResource queryResource = new QueryResource("allele string", "chromosome id", 1, "genome Id", "dataset Id");
	List<QueryResource> queries = new ArrayList<QueryResource>();

	private final Beacon beacon = new Beacon("foo", "bar", "org", "sample beacon", "0.1/0.2", "http://dnastack.com/ga4gh/bob/", "beacon@dnastack.com", "oauth2", datasets, queries);
    
    @Override
    public BeaconResponse query(String chrom, Long pos, String allele, String ref, String dataset) {

    	// required parameters are missing
    	if (chrom == null || pos == null || allele == null || ref == null) {
    		ErrorResource errorResource = new ErrorResource("Incomplete Query", "Required parameters are missing");
    		ResponseResource responseResource = new ResponseResource(null, null, null, null, errorResource);
    		BeaconResponse response = new BeaconResponse(beacon.getId(), QueryUtils.getQuery(chrom, pos, allele, ref, dataset), responseResource);
    		return response;
    	}

    	BeaconResponse response = new BeaconResponse(beacon.getId(), QueryUtils.getQuery(chrom, pos, allele, ref, dataset), null);
        ResponseResource responseResource = new ResponseResource(true, 0, null, "bla", null);

        // generate a sample response
        if (pos % 2 == 0) {
            response.setResponse(responseResource);
        } else {
            response.setResponse(responseResource);
        }

        return response;
    }

    @Override
    public Beacon info() {
    	queries.add(queryResource);
    	datasets.add(dataSetResource);
    	return beacon;
    }

}
