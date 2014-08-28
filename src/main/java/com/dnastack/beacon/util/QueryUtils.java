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
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
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
    private static final Map<String, String> chromMapping = ImmutableMap.of("hg38", "GRCh38", "hg19", "GRCh37", "hg18", "NCBI36", "hg17", "NCBI35", "hg16", "NCBI34");

    /**
     * Generates a canonical chrom ID.
     *
     * @param chrom chromosome
     * @return normalized chromosome value
     */
    public String normalizeChromosome(String chrom) {
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
    public String denormalizeChromosome(String template, String chrom) {
        return String.format(template, chrom);
    }

    /**
     * Converts X/Y chromosomes to lowercase.
     *
     * @param c chromosome
     * @return denormalized chromosome
     */
    public String makeChromXYLowercase(String c) {
        if ("X".equals(c) || "Y".equals(c)) {
            return c.toLowerCase();
        }
        return c;
    }

    /**
     * Converts 1-based position to 0-based position.
     *
     * @param pos 1-based position
     * @return 0-based position
     */
    public Long normalizePosition(Long pos) {
        if (pos == null) {
            return null;
        }
        return --pos;
    }

    /**
     * Converts 0-based position to 1-based position.
     *
     * @param pos 0-based position
     * @return 1-based position
     */
    public Long denormalizePosition(Long pos) {
        if (pos == null) {
            return null;
        }
        return ++pos;
    }

    /**
     * Generate a canonical allele string.
     *
     * @param allele denormalized allele
     * @return normalized allele
     */
    public String normalizeAllele(String allele) {
        if (allele == null || allele.isEmpty()) {
            return null;
        }

        String res = allele.toUpperCase();
        if (res.equals("DEL") || res.equals("INS")) {
            return res.substring(0, 1);
        }
        if (Pattern.matches("([D,I])|([A,C,T,G]+)", res)) {
            return res;
        }

        return null;
    }

    /**
     * Generate a domain-specific allele string with long allele names like DEL and INS.
     *
     * @param allele normalized allele
     * @return denormalized allele
     */
    public String denormalizeAllele(String allele) {
        if (allele == null || allele.isEmpty()) {
            return null;
        }

        String res = allele.toUpperCase();
        if (res.equals("D")) {
            return "DEL";
        }
        if (res.equals("I")) {
            return "INS";
        }

        return res;
    }

    /**
     * Generate a canonical genome representation (hg*).
     *
     * @param ref denormalized genome
     * @return normalized genome
     */
    public String normalizeReference(String ref) {
        if (ref == null || ref.isEmpty()) {
            return null;
        }

        String res = ref.toLowerCase();
        for (String s : chromMapping.keySet()) {
            if (s.toLowerCase().equals(ref)) {
                return s;
            }
        }
        for (Entry<String, String> e : chromMapping.entrySet()) {
            if (e.getValue().toLowerCase().equals(ref)) {
                return e.getKey();
            }
        }

        return null;
    }

    /**
     * Generate a domain-specific genome representation (GRCh*, NCBI*).
     *
     * @param ref normalized genome
     * @return denormalized genome
     */
    public String denormalizeReference(String ref) {
        if (ref == null || ref.isEmpty()) {
            return null;
        }

        String res = ref.toLowerCase();
        for (String s : chromMapping.values()) {
            if (s.toLowerCase().equals(ref)) {
                return s;
            }
        }
        for (Entry<String, String> e : chromMapping.entrySet()) {
            if (e.getKey().toLowerCase().equals(ref)) {
                return e.getValue();
            }
        }

        return res;
    }

    /**
     * Generates a canonical version of a query (field values normalized).
     *
     * @param q query
     * @return normalized query
     */
    public Query normalizeQuery(Query q) {
        return new Query(normalizeChromosome(q.getChromosome()), q.getPosition(), normalizeAllele(q.getAllele()), normalizeReference(q.getReference()));
    }
}
