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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

/**
 * AMPLab beacon service.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Named
@ApplicationScoped
public class AmpLabBeaconService implements BeaconService {

    private static final String BASE_URL = "http://beacon.eecs.berkeley.edu/beacon.php";

    @Inject
    private HttpUtils httpUtils;

    private List<NameValuePair> getQueryData(String ref, String chrom, Long pos, String allele) {
        List<NameValuePair> nvs = new ArrayList<>();
        // so far there is only 1 population
        nvs.add(new BasicNameValuePair("population", "1000genomes"));
        nvs.add(new BasicNameValuePair("genome", ref));
        nvs.add(new BasicNameValuePair("chr", chrom));
        nvs.add(new BasicNameValuePair("coord", pos.toString()));
        nvs.add(new BasicNameValuePair("allele", allele));

        return nvs;
    }

    private String getQueryResponse(Query query) {
        HttpPost httpPost = new HttpPost(BASE_URL);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(getQueryData(query.getReference(), query.getChromosome(), query.getPosition(), query.getAllele())));
        } catch (UnsupportedEncodingException ex) {
            return null;
        }

        return httpUtils.executeRequest(httpPost);
    }

    private Boolean parseQueryResponse(String response) {
        if (response == null) {
            return null;
        }

        String s = response.toLowerCase();
        if (s.contains("beacon found")) {
            return true;
        }
        if (s.contains("beacon cannot find")) {
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
