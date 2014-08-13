/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dnastack.beacon.util;

import com.dnastack.beacon.core.Query;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * Utils for query manipulation.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Named
@ApplicationScoped
public class QueryUtils {

    // order is important!
    private static final String[] CHROM_VALS = {"22", "21", "20", "19", "18", "17", "16", "15", "14", "13", "12", "11", "10", "9", "8", "7", "6", "5", "4", "3", "2", "1", "X", "Y", "MT"};

    /**
     * Validates a given query.
     *
     * @param q query
     * @return true if the query is valid (none of the fields is null), false otherwise
     */
    public boolean isQueryValid(Query q) {
        return q != null && q.getChromosome() != null && q.getPosition() != null && q.getAllele() != null;
    }

    /**
     * Generates a canonical chrom ID.
     *
     * @param chrom chromosome
     * @return normalized chromosome value
     */
    public String normalizeChrom(String chrom) {
        // parse chrom value
        String res = null;
        if (chrom != null) {
            String orig = chrom.toUpperCase();
            for (String s : CHROM_VALS) {
                if (orig.endsWith(s)) {
                    return s;
                }
            }
        }

        return res;
    }

    /**
     * Generate a domain-specific chromosome identifier.
     *
     * @param template template used for String.format()
     * @param chrom canonical chromosome
     * @return denormalized chromosome value
     */
    public String denormalizeChrom(String template, String chrom) {
        return String.format(template, chrom);
    }

    /**
     * Generates a canonical version of a query (field values normalized).
     *
     * @param q query
     * @return normalized query
     */
    public Query normalizeQuery(Query q) {
        return new Query(normalizeChrom(q.getChromosome()), q.getPosition(), q.getAllele());
    }
}
