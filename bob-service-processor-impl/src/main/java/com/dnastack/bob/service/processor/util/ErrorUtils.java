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
package com.dnastack.bob.service.processor.util;

import java.util.Arrays;

/**
 * Utilities for generating errors.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class ErrorUtils {

    private ErrorUtils() {
        // prevent instantiation
    }

    /**
     * Constructs a message to log from a throwable.
     *
     * @param t throwable
     * @return message
     */
    public static String getErrorMessage(Throwable t) {
        return String.format("%s: %s", t.getMessage(), Arrays.toString(t.getStackTrace()));
    }
}
