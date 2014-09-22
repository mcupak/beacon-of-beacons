/*
 * The MIT License
 *
 * Copyright 2014 DNAstack.
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
package com.dnastack.bob.rest.util;

import com.dnastack.bob.dto.BeaconTo;
import com.dnastack.bob.dto.QueryTo;
import java.util.HashSet;
import java.util.Set;

/**
 * Utils for beacon response testing.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class BeaconResponseTestUtils {

    private static boolean stringsEqual(String a, String b) {
        boolean onlyOneNull = (a == null && b != null) || (a != null && b == null);
        boolean bothNull = a == null && b == null;

        if (onlyOneNull) {
            return false;
        } else {
            return (bothNull) ? true : a.equals(b);
        }
    }

    /**
     * Checks if the beacon ID matches a given ID.
     *
     * @param b  beacon
     * @param id id
     *
     * @return true if the ids match, false otherwise
     */
    public static boolean beaconsMatch(BeaconTo b, String id) {
        if (id == null || b == null) {
            throw new NullPointerException("Argument cannot be null.");
        }

        return id.equals(b.getId());
    }

    /**
     * Checks if the query attributes match a given parameter list.
     *
     * @param q      query
     * @param params parameter array containing the following elements as strings: chrom, pos, allele, ref
     *               (respectively)
     *
     * @return true if the queries match, false otherwise
     */
    public static boolean queriesMatch(QueryTo q, String[] params) {
        return getNonMachingFields(q, params).isEmpty();
    }

    /**
     * Checks which attributes of queries don't match and returns their indices, i.e.
     * chrom=0, pos=1, allele=2, ref=3.
     *
     * @param q      query
     * @param params parameter array containing the following elements as strings: chrom, pos, allele, ref
     *               (respectively)
     *
     * @return list of indices of nonmatching query attributes
     */
    public static Set<Integer> getNonMachingFields(QueryTo q, String[] params) {
        if (q == null) {
            throw new NullPointerException("Query cannot be null.");
        }

        Set<Integer> fields = new HashSet<>();
        if (!stringsEqual(q.getChromosome() == null ? null : q.getChromosome().toString(), params[0])) {
            fields.add(0);
        }
        if (!stringsEqual(q.getPosition() == null ? null : q.getPosition().toString(), params[1])) {
            fields.add(1);
        }
        if (!stringsEqual(q.getAllele(), params[2])) {
            fields.add(2);
        }
        if (!stringsEqual(q.getReference() == null ? null : q.getReference().toString(), params[3])) {
            fields.add(3);
        }

        return fields;
    }

}
