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

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utils for parsing query responses.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Named
@RequestScoped
public class ParsingUtils {

    /**
     * Checks whether a given response contains the specified string (found/not found), case insensitive.
     *
     * @param response response
     * @param trueString string reporting a positive query result
     * @param falseString string reporting a negative query result
     * @return true if the response contains trueString, false if the response contains falseString, null otherwise
     */
    public Boolean parseContainsStringCaseInsensitive(String response, String trueString, String falseString) {
        if (response == null) {
            return null;
        }

        String s = response.toLowerCase();
        if (s.contains(trueString)) {
            return true;
        }
        if (s.contains(falseString)) {
            return false;
        }

        return null;
    }

    /**
     * Checks whether a given response starts with the specified string, case insensitive.
     *
     * @param response response
     * @param trueString string reporting a positive query result
     * @param falseString string reporting a negative query result
     * @return true if the response contains trueString, false if the response contains falseString, null otherwise
     */
    public Boolean parseStartsWithStringCaseInsensitive(String response, String trueString, String falseString) {
        if (response == null) {
            return null;
        }

        String s = response.toLowerCase();
        if (s.startsWith(trueString)) {
            return true;
        }
        if (s.startsWith(falseString)) {
            return false;
        }

        return null;
    }

    /**
     * Checks whether the response is yes or no.
     *
     * @param response response
     * @return true if the response is yes, false if the response is no, null otherwise
     */
    public Boolean parseYesNoCaseInsensitive(String response) {
        return parseStartsWithStringCaseInsensitive(response, "yes", "no");
    }

    /**
     * Checks whether the response is "ref", i.e. this is the reference allele at the given location.
     *
     * @param response response
     * @return true if the response is ref, false otherwise, null if there are problems
     */
    public Boolean parseRef(String response) {
        if (response == null) {
            return null;
        }

        return response.toLowerCase().startsWith("ref");
    }

    /**
     * Parses boolean value out of the given field in a JSON response.
     *
     * @param response response in JSON format
     * @param field field name
     * @return field value if it is true/false, null otherwise
     */
    public Boolean parseBooleanFromJson(String response, String field) {
        if (response == null) {
            return null;
        }

        JSONObject jo = new JSONObject(response);
        try {
            boolean b = jo.getBoolean(field);
            return b;
        } catch (JSONException jex) {
            // cannot parse the response or no record message
            return null;
        }
    }
}
