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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.NonNull;

/**
 * Parsing utils for request parameters.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class ParameterParser {

    /**
     * Checks if there are multiple values given as a parameter.
     *
     * @param param values
     *
     * @return true/false
     */
    public static boolean parameterHasMultipleValidValue(@NonNull String param) {
        return param.matches("\\[(((\\w)*-(\\w)*)*(\\w)*,)*((\\w)*-(\\w)*)*(\\w)*\\]");
    }

    /**
     * Checks if there is a single value given for the specified parameter.
     *
     * @param param parameter value
     *
     * @return true/false
     */
    public static boolean parameterHasSingleValidValue(@NonNull String param) {
        return param.matches("[-a-zA-Z0-9]*");
    }

    /**
     * Extract multiple values of a single parameter using "," as a delimiter and "[]" as borders.
     *
     * @param param parameter value
     *
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
