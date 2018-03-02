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
package com.dnastack.bob.service.processor.api;

import com.dnastack.bob.persistence.entity.Beacon;
import com.dnastack.bob.persistence.entity.BeaconResponse;
import com.dnastack.bob.persistence.entity.Query;

import java.util.concurrent.Future;

/**
 * Beacon query service.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public interface BeaconProcessor {

    /**
     * Asynchronously executes a query against a beacon.
     *
     * @param beacon beacon
     * @param query  query
     * @return response
     */
    Future<BeaconResponse> executeQuery(Beacon beacon, Query query);

}
