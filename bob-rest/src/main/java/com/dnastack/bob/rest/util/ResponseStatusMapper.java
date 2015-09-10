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

import com.google.common.collect.ImmutableMap;
import javax.ws.rs.core.Response;

/**
 * Maps DAO exceptions to JAX-RS response codes.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class ResponseStatusMapper {

    private static final ImmutableMap<String, Response.Status> mapping = ImmutableMap.of();

    private ResponseStatusMapper() {
        // prevent instantiation
    }

    /**
     * Retrieves Response.Status from a given exception according to the static mapping. If the mapping does not contain
     * the exception, INTERNAL_SERVER_ERROR is returned.
     *
     * @param ex exception
     *
     * @return response status
     */
    public static Response.Status getStatus(Exception ex) {
        Response.Status s = mapping.get(ex.getClass().getCanonicalName());

        return (s == null) ? Response.Status.INTERNAL_SERVER_ERROR : s;
    }
}
