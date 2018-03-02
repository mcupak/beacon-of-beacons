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
package com.dnastack.bob.service.parser.impl;

import com.dnastack.bob.persistence.entity.Beacon;
import com.dnastack.bob.service.parser.api.ResponseParser;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.dnastack.bob.service.parser.util.ParseUtils.*;

/**
 * Parses "yes" and "no" strings with ref conversion.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Stateless
@Named
@Dependent
@Local(ResponseParser.class)
public class StringYesNoRefResponseParser implements ResponseParser, Serializable {

    private static final long serialVersionUID = -4790485566013440026L;

    @Asynchronous
    @Override
    public Future<Boolean> parse(Beacon b, Future<String> response) {
        Boolean res = null;
        try {
            String str = response.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
            res = parseStartsWithYesNoCaseInsensitive(str);
            if (res == null) {
                // ref response is treated as false
                Boolean isRef = parseRef(str);
                if (isRef != null && isRef) {
                    res = false;
                }
            }
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            // ignore
        }

        return new AsyncResult<>(res);
    }
}
