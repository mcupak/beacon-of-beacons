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
