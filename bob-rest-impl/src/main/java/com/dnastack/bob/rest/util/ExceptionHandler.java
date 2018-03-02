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

import com.dnastack.bob.rest.base.Error;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Arrays;

/**
 * Handler of DataAccessException hierarchy.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {

    @Context
    private HttpHeaders headers;

    @Override
    public Response toResponse(Exception exception) {
        Response.Status s = ResponseStatusMapper.getStatus(exception);
        Error error = Error.builder()
                           .status(s.getStatusCode())
                           .reason(s.getReasonPhrase())
                           .message(exception.getMessage())
                           .stackTrace(Arrays.deepToString(exception.getStackTrace()))
                           .build();

        return Response.status(s).entity(error).type(MediaTypeResolver.getMediaType(headers)).build();
    }

}
