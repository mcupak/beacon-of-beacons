/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dnastack.beacon.util;

import javax.enterprise.context.ApplicationScoped;
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
@ApplicationScoped
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
     * @param response reponse
     * @return true if the response is yes, false if the response is no, null otherwise
     */
    public Boolean parseYesNoCaseInsensitive(String response) {
        return parseStartsWithStringCaseInsensitive(response, "yes", "no");
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
