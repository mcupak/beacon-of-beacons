/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dnastack.beacon.service;

import com.dnastack.beacon.core.Beacon;
import com.dnastack.beacon.core.BeaconResponse;
import com.dnastack.beacon.core.BeaconService;
import com.dnastack.beacon.core.Query;
import com.dnastack.beacon.util.HttpUtils;
import java.net.MalformedURLException;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.http.client.methods.HttpGet;

/**
 * WTSI beacon service.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Named
@ApplicationScoped
public class WtsiBeaconService implements BeaconService {

    private static final String BASE_URL = "http://www.sanger.ac.uk/sanger/GA4GH_Beacon";
    private static final String PARAM_TEMPLATE = "?src=all&ass=%s&chr=%s&pos=%d&all=%s";

    @Inject
    private HttpUtils httpUtils;

    private String getQueryUrl(String ref, String chrom, Long pos, String allele) throws MalformedURLException {
        String params = String.format(PARAM_TEMPLATE, ref, chrom, pos, allele);

        return BASE_URL + params;
    }

    private String getQueryResponse(Query query) {
        String response = null;

        HttpGet httpGet;
        try {
            httpGet = new HttpGet(getQueryUrl(query.getReference(), query.getChromosome(), query.getPosition(), query.getAllele()));
        } catch (MalformedURLException ex) {
            return response;
        }

        return httpUtils.executeRequest(httpGet);
    }

    private Boolean parseQueryResponse(String response) {
        if (response == null) {
            return null;
        }

        String s = response.toLowerCase();

        if (s.contains("yes")) {
            return true;
        }
        if (s.contains("no")) {
            return false;
        }

        return null;
    }

    @Override
    public BeaconResponse executeQuery(Beacon beacon, Query query) {
        BeaconResponse res = new BeaconResponse(beacon, query, null);

        res.setResponse(parseQueryResponse(getQueryResponse(query)));

        return res;
    }
}
