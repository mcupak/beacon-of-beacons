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
public class AmpLabBeaconService extends GenomeAwareBeaconService {

    private static final String BASE_URL = "http://beacon.eecs.berkeley.edu/beacon.php";
    // requeries genome specification in the query
    private static final String[] SUPPORTED_REFS = {"hg18", "hg19", "hg38"};
    private static final String CHROM_TEMPLATE = "chr%s";

    @Inject
    private HttpUtils httpUtils;

    @Inject
    private ParsingUtils parsingUtils;

    @Inject
    private QueryUtils queryUtils;

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

    @Override
    public String getQueryResponse(Beacon beacon, Query query, String ref) {
        HttpPost httpPost = new HttpPost(BASE_URL);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(getQueryData(ref, queryUtils.denormalizeChrom(CHROM_TEMPLATE, query.getChromosome()), query.getPosition(), query.getAllele())));
        } catch (UnsupportedEncodingException ex) {
            return null;
        }

        return httpUtils.executeRequest(httpPost);
    }

    @Override
    public Boolean parseQueryResponse(String response) {
        return parsingUtils.parseContainsStringCaseInsensitive(response, "beacon found", "beacon cannot find");
    }

    @Override
    protected String[] getRefs() {
        return SUPPORTED_REFS;
    }

}
