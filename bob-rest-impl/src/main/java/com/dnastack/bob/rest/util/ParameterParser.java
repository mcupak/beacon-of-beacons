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

import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Parsing utils for request parameters.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class ParameterParser {

    private ParameterParser() {
        // prevent instantiation
    }

    /**
     * Checks if there are multiple values given as a parameter.
     *
     * @param param values
     * @return true/false
     */
    public static boolean parameterHasMultipleValidValue(@NonNull String param) {
        return param.matches(
                "\\[((([a-zA-Z_0-9\\.\\+])*-([a-zA-Z_0-9\\.\\+])*)*([a-zA-Z_0-9\\.\\+])*,)*(([a-zA-Z_0-9\\.\\+])*-([a-zA-Z_0-9\\.\\+])*)*([a-zA-Z_0-9\\.\\+])*\\]");
    }

    /**
     * Checks if there is a single value given for the specified parameter.
     *
     * @param param parameter value
     * @return true/false
     */
    public static boolean parameterHasSingleValidValue(@NonNull String param) {
        return param.matches("([-a-zA-Z_0-9\\.\\+])*");
    }

    /**
     * Extract multiple values of a single parameter using "," as a delimiter and "[]" as borders.
     *
     * @param param parameter value
     * @return collection of individual values
     */
    public static Collection<String> parseMultipleParameterValues(@NonNull String param) {
        List<String> values = new ArrayList<>();
        if (parameterHasSingleValidValue(param)) {
            values.add(param);
        } else if (parameterHasMultipleValidValue(param)) {
            values.addAll(Arrays.asList(param.substring(param.indexOf("[") + 1, param.indexOf("]")).split(",")));
        }

        return values;
    }
}
