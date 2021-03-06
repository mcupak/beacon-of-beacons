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
package com.dnastack.bob.rest.resource;

import com.dnastack.bob.rest.api.ReferenceResource;
import com.dnastack.bob.rest.util.JaxbList;
import com.dnastack.bob.rest.util.MediaTypeResolver;
import com.dnastack.bob.service.dto.ReferenceDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Reference rest resource.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Path("/references")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@RequestScoped
@Named
@Api(value = "References")
public class ReferenceResourceImpl implements ReferenceResource {

    @Context
    private HttpHeaders headers;

    @GET
    @ApiOperation(value = "List all references", notes = "Lists all the supported references in their canonical form (hg-based notation).", response = ReferenceDto.class, responseContainer = "List")
    @Override
    public Response showAll() {
        return (MediaType.APPLICATION_XML.equals(MediaTypeResolver.getMediaType(headers)))
               ? Response.ok()
                         .entity(new JaxbList<>(ReferenceDto.values()))
                         .build()
               : Response.ok()
                         .entity(Arrays.asList(ReferenceDto.values())
                                       .stream()
                                       .map(c -> c.toString())
                                       .collect(Collectors.toList()))
                         .build();
    }
}
