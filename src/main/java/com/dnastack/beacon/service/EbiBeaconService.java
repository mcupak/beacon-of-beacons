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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

/**
 * EBI beacon service.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Stateless
@LocalBean
public class EbiBeaconService extends GenomeUnawareBeaconService {

    private static final String BASE_URL = "http://www.ebi.ac.uk/eva/beacon";

    @Inject
    private HttpUtils httpUtils;

    private List<NameValuePair> getQueryData(String chrom, Long pos, String allele) {
        List<NameValuePair> nvs = new ArrayList<>();
        // project=0 implies any project
        nvs.add(new BasicNameValuePair("project", "0"));
        nvs.add(new BasicNameValuePair("chromosome", chrom));
        nvs.add(new BasicNameValuePair("coordinate", pos.toString()));
        nvs.add(new BasicNameValuePair("allele", allele));
        // active=1 implies response in JSON (+ surrounding HTML)
        nvs.add(new BasicNameValuePair("active", "1"));
        nvs.add(new BasicNameValuePair("op", "Search"));
        nvs.add(new BasicNameValuePair("form_id", "eva_beacon_form"));

        return nvs;
    }

    @Override
    @Asynchronous
    public Future<String> getQueryResponse(Beacon beacon, Query query, String ref) {
        String res = null;

        HttpPost httpPost = new HttpPost(BASE_URL);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(getQueryData(query.getChromosome(), query.getPosition(), query.getAllele())));
            res = httpUtils.executeRequest(httpPost);
        } catch (UnsupportedEncodingException ex) {
            // ignore, already null
        }

        return new AsyncResult<>(res);
    }

    @Override
    @Asynchronous
    public Future<Boolean> parseQueryResponse(String response) {
        Boolean res = null;

        if (response != null) {
            int start = response.indexOf("exist");
            if (start >= 0) {
                char c = response.charAt(start + 8);
                if (c == 'T' || c == 't') {
                    res = true;
                } else if (c == 'F' || c == 'f') {
                    res = false;
                }
            }
        }

        return new AsyncResult<>(res);
    }

    @Override
    @Asynchronous
    public Future<BeaconResponse> executeQuery(Beacon beacon, Query query) {
        return super.executeQuery(beacon, query);
    }
}
