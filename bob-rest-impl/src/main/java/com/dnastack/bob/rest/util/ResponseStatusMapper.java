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

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Maps DAO exceptions to JAX-RS response codes.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class ResponseStatusMapper {

    private static final Map<String, Response.Status> mapping;

    static {
        mapping = new HashMap<>();
        mapping.put(NotFoundException.class.getCanonicalName(), Response.Status.NOT_FOUND);
        mapping.put(SecurityException.class.getCanonicalName(), Response.Status.UNAUTHORIZED);
        mapping.put(IllegalArgumentException.class.getCanonicalName(), Response.Status.BAD_REQUEST);
        mapping.put(UnsupportedOperationException.class.getCanonicalName(), Response.Status.METHOD_NOT_ALLOWED);
        mapping.put(SecurityException.class.getCanonicalName(), Response.Status.FORBIDDEN);
    }

    private ResponseStatusMapper() {
        // prevent instantiation
    }

    /**
     * Retrieves Response.Status from a given exception according to the static mapping. If the mapping does not contain
     * the exception, INTERNAL_SERVER_ERROR is returned.
     *
     * @param ex exception
     * @return response status
     */
    public static Response.Status getStatus(Exception ex) {
        Response.Status s = mapping.get(ex.getClass().getCanonicalName());

        return (s == null) ? Response.Status.INTERNAL_SERVER_ERROR : s;
    }
}
