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
package com.dnastack.bob.rest.util;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Utility determining the media type to use in a response.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class MediaTypeResolver {

    private MediaTypeResolver() {
        // prevent instantiation
    }

    /**
     * Determines whether to use JSON (default) or XML.
     *
     * @param headers headers
     * @return media type
     */
    public static String getMediaType(HttpHeaders headers) {
        List<MediaType> mediaTypes = headers.getAcceptableMediaTypes();
        // default to JSON
        String chosenType = MediaType.APPLICATION_JSON;
        if (mediaTypes != null) {
            for (MediaType m : mediaTypes) {
                if (m.isCompatible(MediaType.APPLICATION_JSON_TYPE)) {
                    chosenType = MediaType.APPLICATION_JSON;
                    break;
                }
                if (m.isCompatible(MediaType.APPLICATION_XML_TYPE)) {
                    chosenType = MediaType.APPLICATION_XML;
                    break;
                }
            }
        }

        return chosenType;
    }
}
