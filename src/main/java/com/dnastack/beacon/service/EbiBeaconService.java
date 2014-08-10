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
 * EBI beacon service.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Named
@ApplicationScoped
public class EbiBeaconService implements BeaconService {
    
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
    
    private String getQueryResponse(Query query) {
        HttpPost httpPost = new HttpPost(BASE_URL);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(getQueryData(query.getChromosome(), query.getPosition(), query.getAllele())));
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
        
        return httpUtils.executeRequest(httpPost);
    }
    
    private Boolean parseQueryResponse(String response) {
        if (response == null) {
            return null;
        }
        
        int start = response.indexOf("exist");
        if (start < 0) {
            return null;
        }
        
        char c = response.charAt(start + 8);
        
        if (c == 'T' || c == 't') {
            return true;
        }
        if (c == 'F' || c == 'f') {
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
