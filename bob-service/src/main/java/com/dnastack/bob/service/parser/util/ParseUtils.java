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
package com.dnastack.bob.service.parser.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utils for parsing query responses.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Dependent
@Named
public class ParseUtils {

    /**
     * Checks whether a given response contains the specified string (found/not found), case insensitive.
     *
     * @param response    response
     * @param trueString  string reporting a positive query result
     * @param falseString string reporting a negative query result
     *
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
     * @param response    response
     * @param trueString  string reporting a positive query result
     * @param falseString string reporting a negative query result
     *
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
     * Checks whether a given response is equal to the specified string, case insensitive.
     *
     * @param response    response
     * @param trueString  string reporting a positive query result
     * @param falseString string reporting a negative query result
     *
     * @return true if the response contains trueString, false if the response contains falseString, null otherwise
     */
    public Boolean parseIsStringCaseInsensitive(String response, String trueString, String falseString) {
        if (response == null) {
            return null;
        }

        if (response.equalsIgnoreCase(trueString)) {
            return true;
        }
        if (response.equalsIgnoreCase(falseString)) {
            return false;
        }

        return null;
    }

    /**
     * Checks whether the response starts with yes or no.
     *
     * @param response response
     *
     * @return true if the response is yes, false if the response is no, null otherwise
     */
    public Boolean parseStartsWithYesNoCaseInsensitive(String response) {
        return parseStartsWithStringCaseInsensitive(response, "yes", "no");
    }

    /**
     * Checks whether the response is yes or no.
     *
     * @param response response
     *
     * @return true if the response is yes, false if the response is no, null otherwise
     */
    public Boolean parseIsYesNoCaseInsensitive(String response) {
        return parseIsStringCaseInsensitive(response, "yes", "no");
    }

    /**
     * Checks whether the response is "ref", i.e. this is the reference allele at the given location.
     *
     * @param response response
     *
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
     * @param path     list of JSON keys determining the path to the searched value
     *
     * @return field value if it is true/false, null otherwise
     */
    public Boolean parseBooleanFromJson(String response, String... path) {
        if (response == null) {
            return null;
        }

        try {
            JSONObject jo = new JSONObject(response);
            JSONObject current = null;
            for (String s : path) {
                current = jo.optJSONObject(s);
                if (current == null) {
                    JSONArray a = jo.optJSONArray(s);
                    if (a != null) {
                        current = a.optJSONObject(0);
                        if (current == null) {
                            try {
                                return a.getBoolean(0);
                            } catch (JSONException jex) {
                                return null;
                            }
                        }
                    }
                }
                if (current == null) {
                    try {
                        return jo.getBoolean(s);
                    } catch (JSONException jex) {
                        return null;
                    }
                }
                jo = current;
            }
        } catch (JSONException ex) {
            return null;
        }

        return null;
    }

    /**
     * Parses string value out of the given field in a JSON response.
     *
     * @param response response in JSON format
     * @param path     list of JSON keys determining the path to the searched value
     *
     * @return field value if it is true/false, null otherwise
     */
    public String parseStringFromJson(String response, String... path) {
        if (response == null) {
            return null;
        }

        try {
            JSONObject jo = new JSONObject(response);
            JSONObject current = null;
            for (String s : path) {
                current = jo.optJSONObject(s);
                if (current == null) {
                    JSONArray a = jo.optJSONArray(s);
                    if (a != null) {
                        current = a.optJSONObject(0);
                        if (current == null) {
                            try {
                                return a.getString(0);
                            } catch (JSONException jex) {
                                return null;
                            }
                        }
                    }
                }
                if (current == null) {
                    try {
                        return jo.getString(s);
                    } catch (JSONException jex) {
                        return null;
                    }
                }
                jo = current;
            }
        } catch (JSONException ex) {
            return null;
        }

        return null;
    }

    /**
     * Parses yes/no value out of the given field in a JSON response, case-insensitive.
     *
     * @param response
     * @param path
     *
     * @return true if the response is yes, false otherwise
     */
    public Boolean parseYesNoFromJson(String response, String... path) {
        String res = parseStringFromJson(response, path);

        return (res == null) ? null : parseStartsWithYesNoCaseInsensitive(res);
    }

    /**
     * Checks if there are multiple values given as a parameter.
     *
     * @param param values
     *
     * @return true/false
     */
    public boolean parameterHasMultipleValidValue(String param) {
        if (param == null) {
            throw new NullPointerException("param");
        }

        return param.matches("\\[(((\\w)*-(\\w)*)*(\\w)*,)*((\\w)*-(\\w)*)*(\\w)*\\]");
    }

    /**
     * Checks if there is a single value given for the specified parameter.
     *
     * @param param parameter value
     *
     * @return true/false
     */
    public boolean parameterHasSingleValidValue(String param) {
        if (param == null) {
            throw new NullPointerException("param");
        }

        return param.matches("[-a-zA-Z0-9]*");
    }

    /**
     * Extract multiple values of a single parameter using "," as a deliminer and "[]" as borders.
     *
     * @param param parameter value
     *
     * @return collection of individual values
     */
    public Collection<String> parseMultipleParameterValues(String param) {
        if (param == null) {
            throw new NullPointerException("param");
        }

        List<String> values = new ArrayList<>();
        if (parameterHasSingleValidValue(param)) {
            values.add(param);
        } else if (parameterHasMultipleValidValue(param)) {
            values.addAll(Arrays.asList(param.substring(param.indexOf("[") + 1, param.indexOf("]")).split(",")));
        }

        return values;
    }
}
