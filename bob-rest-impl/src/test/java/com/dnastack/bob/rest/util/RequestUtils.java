/* 
 * Copyright 2018 DNAstack.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
