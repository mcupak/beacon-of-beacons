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
package com.dnastack.bob.service.parser.api;

import com.dnastack.bob.persistence.entity.Beacon;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * Beacon metadata parser.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public interface MetadataParser extends BeaconParser<Map<String, String>> {

    /**
     * Asynchronously extracts metadata from the given raw query response.
     *
     * @param beacon   beacon
     * @param response response
     * @return string key-value pairs
     */
    @Override
    Future<Map<String, String>> parse(Beacon beacon, Future<String> response);

}
