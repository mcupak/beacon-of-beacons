/*
 * The MIT License
 *
 * Copyright 2015 DNAstack.
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

import com.dnastack.bob.persistence.entity.Beacon;

/**
 * Utility methods for requestors.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class ConvertUtils {

    /**
     * Returns a substring of an ID of the given beacon.
     *
     * @param b    beacon
     * @param from start index
     * @param to   end index
     *
     * @return
     */
    public static String extractSubstringFromBeaconId(Beacon b, Integer from, Integer to) {
        String res;
        int f = (from == null) ? 0 : from;
        if (to == null) {
            res = b.getId().substring(f);
        } else {
            res = b.getId().substring(f, to);
        }
        return res;
    }

    /**
     * Returns a prefix of an ID of the given beacon.
     *
     * @param b  beacon
     * @param to end index
     *
     * @return
     */
    public static String extractPrefixFromBeaconId(Beacon b, Integer to) {
        return extractSubstringFromBeaconId(b, null, to);
    }

    /**
     * Returns a suffix of an ID of the given beacon.
     *
     * @param b    beacon
     * @param from start index
     *
     * @return
     */
    public static String extractSuffixFromBeaconId(Beacon b, Integer from) {
        return extractSubstringFromBeaconId(b, from, null);
    }

    /**
     * Returns what's left of an ID of the given beacon after removing the prefix with an organization ID and a
     * separator.
     *
     * @param b beacon
     *
     * @return
     */
    public static String extractIdWithoutOrganizationFromBeaconId(Beacon b) {
        String res;
        int from;
        if (b == null) {
            res = null;
        } else {
            if (b.getOrganization() == null || b.getOrganization().getId() == null) {
                from = 0;
            } else {
                from = b.getOrganization().getId().length() + 1;
            }
            res = extractSuffixFromBeaconId(b, from);
        }

        return res;
    }
}
