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
package com.dnastack.bob.service.converter.util;

import com.dnastack.bob.persistence.enumerated.Chromosome;
import com.dnastack.bob.persistence.enumerated.Reference;
import java.util.Map.Entry;

import static com.dnastack.bob.service.util.Constants.REFERENCE_MAPPING;

/**
 * Utils for query manipulation.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class ConvertUtils {

    /**
     * Generate a domain-specific chromosome identifier.
     *
     * @param template template used for String.format()
     * @param chrom    canonical chromosome
     *
     * @return denormalized chromosome value
     */
    public static String denormalizeChromosome(String template, Chromosome chrom) {
        return String.format(template, chrom);
    }

    /**
     * Converts X/Y chromosomes to lowercase.
     *
     * @param c chromosome
     *
     * @return denormalized chromosome
     */
    public static String denormalizeChromXYToLowercase(Chromosome c) {
        if ("X".equals(c.toString()) || "Y".equals(c.toString())) {
            return c.toString().toLowerCase();
        }
        return c.toString();
    }

    /**
     * Converts X/Y chromosomes to number (23/24, respectively).
     *
     * @param c chromosome
     *
     * @return integral chromosome
     */
    public static Integer denormalizeChromosomeToNumber(Chromosome c) {
        if (null != c.toString()) {
            switch (c.toString()) {
                case "X":
                    return 23;
                case "Y":
                    return 24;
                case "MT":
                    return 25;
                default:
                    return Integer.parseInt(c.toString());
            }
        }

        return null;
    }

    /**
     * Converts 0-based position to 1-based position.
     *
     * @param pos position
     *
     * @return position
     */
    public static Long denormalizePosition(Long pos) {
        if (pos == null) {
            return null;
        }
        return ++pos;
    }

    /**
     * Generate a domain-specific allele string with long allele names like DEL and INS.
     *
     * @param allele normalized allele
     *
     * @return denormalized allele
     */
    public static String denormalizeAllele(String allele) {
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
     * Generate a domain-specific allele string with indels wrapped in <> (url encoded).
     *
     * @param allele allele
     *
     * @return denormalized allele
     */
    public static String denormalizeAlleleToBrackets(String allele) {
        if (allele == null || allele.isEmpty()) {
            return null;
        }

        String res = allele.toUpperCase();
        if (res.equals("D") || res.equals("DEL") || res.equals("I") || res.equals("INS")) {
            return "%3C" + allele + "%3E";
        }

        return res;
    }

    /**
     * Generate a domain-specific genome representation (GRCh*, NCBI*).
     *
     * @param ref normalized genome
     *
     * @return denormalized genome
     */
    public static String denormalizeReference(Reference ref) {
        if (ref == null) {
            return null;
        }

        for (String s : REFERENCE_MAPPING.values()) {
            if (s.equalsIgnoreCase(ref.toString())) {
                return s;
            }
        }
        for (Entry<Reference, String> e : REFERENCE_MAPPING.entrySet()) {
            if (e.getKey().equals(ref)) {
                return e.getValue();
            }
        }

        return ref.toString();
    }

}
