/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dnastack.beacon.service;

import com.dnastack.beacon.core.Beacon;
import com.dnastack.beacon.core.Query;
import com.dnastack.beacon.util.HttpUtils;
import com.dnastack.beacon.util.ParsingUtils;
import com.dnastack.beacon.util.QueryUtils;
import java.net.MalformedURLException;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.http.client.methods.HttpGet;

/**
 * A Genomics Alliance beacon service at UCSC.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Named
@ApplicationScoped
public class UcscBeaconService extends GenomeUnawareBeaconService {

    private static final String BASE_URL = "http://hgwdev-max.cse.ucsc.edu/cgi-bin/beacon/query";
    private static final String PARAM_TEMPLATE = "?track=%s&chrom=%s&pos=%d&allele=%s";
    private static final String CHROM_TEMPLATE = "chr%s";

    @Inject
    private HttpUtils httpUtils;

    @Inject
    private ParsingUtils parsingUtils;

    @Inject
    private QueryUtils queryUtils;

    private String getQueryUrl(String track, String chrom, Long pos, String allele) throws MalformedURLException {
        String params = String.format(PARAM_TEMPLATE, track, chrom, pos, allele);

        return BASE_URL + params;
    }

    @Override
    public String getQueryResponse(Beacon beacon, Query query, String ref) {
        String response = null;

        HttpGet httpGet;
        try {
            httpGet = new HttpGet(getQueryUrl(beacon.getId(), queryUtils.denormalizeChrom(CHROM_TEMPLATE, query.getChromosome()), query.getPosition(), query.getAllele()));
        } catch (MalformedURLException ex) {
            return response;
        }

        return httpUtils.executeRequest(httpGet);
    }

    @Override
    public Boolean parseQueryResponse(String response) {
        return parsingUtils.parseYesNoCaseInsensitive(response);
    }

}
