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

import com.dnastack.beacon.entity.Chromosome;
import com.dnastack.beacon.entity.Query;
import com.dnastack.beacon.entity.Reference;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
 * Utils for query manipulation.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class QueryUtils {

    private static final Map<Reference, String> chromMapping = new HashMap<>();

    static {
        chromMapping.put(Reference.HG38, "GRCh38");
        chromMapping.put(Reference.HG19, "GRCh37");
        chromMapping.put(Reference.HG18, "NCBI36");
        chromMapping.put(Reference.HG17, "NCBI35");
        chromMapping.put(Reference.HG16, "NCBI34");

    }

    /**
     * Generates a canonical chrom ID.
     *
     * @param chrom chromosome
     *
     * @return normalized chromosome value
     */
    public static Chromosome normalizeChromosome(String chrom) {
        // parse chrom value
        if (chrom != null) {
            String orig = chrom.toUpperCase();
            for (Chromosome c : Chromosome.values()) {
                if (orig.endsWith(c.toString())) {
                    return c;
                }
            }
        }

        return null;
    }

    /**
     * Converts 0-based position to 1-based position.
     *
     * @param pos 0-based position
     *
     * @return 1-based position
     */
    public static Long normalizePosition(Long pos) {
        if (pos == null) {
            return null;
        }
        return ++pos;
    }

    /**
     * Generate a canonical allele string.
     *
     * @param allele denormalized allele
     *
     * @return normalized allele
     */
    public static String normalizeAllele(String allele) {
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
     * Generate a canonical genome representation (hg*).
     *
     * @param ref denormalized genome
     *
     * @return normalized genome
     */
    public static Reference normalizeReference(String ref) {
        if (ref == null || ref.isEmpty()) {
            return null;
        }

        for (Reference s : chromMapping.keySet()) {
            if (s.toString().equalsIgnoreCase(ref)) {
                return s;
            }
        }
        for (Entry<Reference, String> e : chromMapping.entrySet()) {
            if (e.getValue().equalsIgnoreCase(ref)) {
                return e.getKey();
            }
        }

        return null;
    }

    /**
     * Obtains a canonical query object.
     *
     * @param chrom  chromosome
     * @param pos    position
     * @param allele allele
     * @param ref    genome
     *
     * @return normalized query
     */
    public static Query getQuery(String chrom, Long pos, String allele, String ref) {
        Chromosome c = normalizeChromosome(chrom);
        Reference r = normalizeReference(ref);

        return new Query(c == null ? null : c, pos, normalizeAllele(allele), r == null ? null : r);
    }

}
