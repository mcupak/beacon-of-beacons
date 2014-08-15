/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dnastack.beacon.service;

import com.dnastack.beacon.core.Beacon;
import com.dnastack.beacon.core.BeaconResponse;
import com.dnastack.beacon.core.Query;
import com.dnastack.beacon.util.HttpUtils;
import com.dnastack.beacon.util.ParsingUtils;
import java.net.MalformedURLException;
import java.util.concurrent.Future;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.http.client.methods.HttpGet;

/**
 * NCBI beacon service.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Stateless
@LocalBean
public class NcbiBeaconService extends GenomeAwareBeaconService {

    private static final String BASE_URL = "http://www.ncbi.nlm.nih.gov/projects/genome/beacon/beacon.cgi";
    private static final String PARAM_TEMPLATE = "?ref=%s&chrom=%s&pos=%d&allele=%s";
    // requeries genome specification in the query
    private static final String[] SUPPORTED_REFS = {"NCBI36", "GRCh37", "GRCh38"};

    @Inject
    private HttpUtils httpUtils;

    @Inject
    private ParsingUtils parsingUtils;

    private String getQueryUrl(String ref, String chrom, Long pos, String allele) throws MalformedURLException {
        String params = String.format(PARAM_TEMPLATE, ref, chrom, pos, allele);

        return BASE_URL + params;
    }

    @Override
    @Asynchronous
    public Future<String> getQueryResponse(Beacon beacon, Query query, String ref) {
        String res = null;

        // should be POST, but the server accepts GET as well
        HttpGet httpGet;
        try {
            httpGet = new HttpGet(getQueryUrl(ref, query.getChromosome(), query.getPosition(), query.getAllele()));
            res = httpUtils.executeRequest(httpGet);
        } catch (MalformedURLException ex) {
            // ignore, already null
        }

        return new AsyncResult<>(res);
    }

    @Override
    @Asynchronous
    public Future<Boolean> parseQueryResponse(String response) {
        Boolean res = parsingUtils.parseBooleanFromJson(response, "exist_gt");

        return new AsyncResult<>(res);
    }

    @Override
    protected String[] getRefs() {
        return SUPPORTED_REFS;
    }

    @Override
    @Asynchronous
    public Future<BeaconResponse> executeQuery(Beacon beacon, Query query) {
        return super.executeQuery(beacon, query);
    }
}
