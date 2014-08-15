/*
 * The MIT License
 *
 * Copyright 2014 Miroslav Cupak (mirocupak@gmail.com).
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
package com.dnastack.beacon.util;

import com.dnastack.beacon.core.Query;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * Utils for query manipulation.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Named
@RequestScoped
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
