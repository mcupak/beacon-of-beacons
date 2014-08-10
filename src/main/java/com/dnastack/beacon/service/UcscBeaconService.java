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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * A Genomics Alliance beacon service at UCSC..
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Named
@ApplicationScoped
public class UcscBeaconService implements BeaconService {

    private static final String BASE_URL = "http://hgwdev-max.cse.ucsc.edu/cgi-bin/beacon/query";
    private static final String PARAM_TEMPLATE = "?track=%s&chrom=%s&pos=%d&allele=%s";

    private URL getQueryUrl(String track, String chrom, long pos, String allele) throws MalformedURLException {
        String params = String.format(PARAM_TEMPLATE, track, chrom, pos, allele);
        URL url = new URL(BASE_URL + params);

        return url;
    }

    @Override
    public BeaconResponse executeQuery(Beacon beacon, Query query) {
        BeaconResponse res = new BeaconResponse(beacon, query, null);

        URL queryUrl = null;
        try {
            queryUrl = getQueryUrl(beacon.getId(), query.getChromosome(), query.getPosition(), query.getAllele());
        } catch (MalformedURLException ex) {
            return res;
        }

        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(queryUrl.openStream()));
            String output = in.readLine();

            if ("yes".equals(output.toLowerCase())) {
                res.setResponse(true);
            } else if ("no".equals(output.toLowerCase())) {
                res.setResponse(false);
            }
        } catch (IOException ex) {
            return res;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    // ignore
                }
            }
        }

        return res;
    }

}
