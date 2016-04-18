package com.dnastack.bob.rest.util;

import lombok.extern.java.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Request utilities.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Log
public class RequestUtils {

    /**
     * URL-encodes a given string.
     *
     * @param s decoded input
     * @return encoded output
     */
    public static String encode(String s) {
        String res = null;
        try {
            res = URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.severe(e.getMessage());
        }

        return res;
    }

    /**
     * URL-decodes a given string.
     *
     * @param s encoded input
     * @return decoded output
     */
    public static String decode(String s) {
        String res = null;
        try {
            res = URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.severe(e.getMessage());
        }

        return res;
    }
}
