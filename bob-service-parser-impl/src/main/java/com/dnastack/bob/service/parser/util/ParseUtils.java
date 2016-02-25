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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utilities for parsing query responses.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class ParseUtils {

    public static final int REQUEST_TIMEOUT = 15;

    private ParseUtils() {
        // prevent instantiation
    }

    /**
     * Checks whether a given response contains the specified string (found/not found), case insensitive.
     *
     * @param response    response
     * @param trueString  string reporting a positive query result
     * @param falseString string reporting a negative query result
     * @return true if the response contains trueString, false if the response contains falseString, null otherwise
     */
    public static Boolean parseContainsStringCaseInsensitive(String response, String trueString, String falseString) {
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
     * @return true if the response contains trueString, false if the response contains falseString, null otherwise
     */
    public static Boolean parseStartsWithStringCaseInsensitive(String response, String trueString, String falseString) {
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
     * @return true if the response contains trueString, false if the response contains falseString, null otherwise
     */
    public static Boolean parseIsStringCaseInsensitive(String response, String trueString, String falseString) {
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
     * @return true if the response is yes, false if the response is no, null otherwise
     */
    public static Boolean parseStartsWithYesNoCaseInsensitive(String response) {
        return parseStartsWithStringCaseInsensitive(response, "yes", "no");
    }

    /**
     * Checks whether the response is yes or no.
     *
     * @param response response
     * @return true if the response is yes, false if the response is no, null otherwise
     */
    public static Boolean parseIsYesNoCaseInsensitive(String response) {
        return parseIsStringCaseInsensitive(response, "yes", "no");
    }

    /**
     * Checks whether the response is "ref", i.e. this is the reference allele at the given location.
     *
     * @param response response
     * @return true if the response is ref, false otherwise, null if there are problems
     */
    public static Boolean parseRef(String response) {
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
     * @return field value if it is true/false, null otherwise
     */
    public static Boolean parseBooleanFromJson(String response, String... path) {
        if (response == null) {
            return null;
        }

        try {
            JSONObject jo = new JSONObject(response);
            JSONObject current;
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
     * @return field value if it is true/false, null otherwise
     */
    public static String parseStringFromJson(String response, String... path) {
        if (response == null) {
            return null;
        }

        try {
            JSONObject jo = new JSONObject(response);
            JSONObject current;
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
     * Parses string map out of the given field in a JSON response.
     *
     * @param response response in JSON format
     * @param path     list of JSON keys determining the path to the searched value
     * @return string key-value map
     */
    public static Map<String, String> parseStringMapFromJson(String response, String... path) {
        if (response == null) {
            return null;
        }

        Map<String, String> res = new HashMap<>();
        try {
            JSONObject jo = new JSONObject(response);
            JSONObject current;
            for (String s : path) {
                current = jo.optJSONObject(s);
                if (current == null) {
                    JSONArray a = jo.optJSONArray(s);
                    if (a != null) {
                        current = a.optJSONObject(0);
                    }
                }
                if (current == null) {
                    try {
                    } catch (JSONException jex) {
                        return null;
                    }
                }
                jo = current;
            }
            if (jo != null) {
                Iterator keys = jo.keys();
                while (keys.hasNext()) {
                    String key = keys.next().toString();
                    res.put(key, jo.getString(key));
                }
            }
        } catch (JSONException ex) {
            return null;
        }

        return res;
    }

    /**
     * Parses yes/no value out of the given field in a JSON response, case-insensitive.
     *
     * @param response
     * @param path
     * @return true if the response is yes, false otherwise
     */
    public static Boolean parseYesNoFromJson(String response, String... path) {
        String res = parseStringFromJson(response, path);

        return (res == null) ? null : parseStartsWithYesNoCaseInsensitive(res);
    }

}
