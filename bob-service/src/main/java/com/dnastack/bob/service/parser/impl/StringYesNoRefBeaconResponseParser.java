/*
 * The MIT License
 *
 * Copyright 2014 Miroslav Cupak (mirocupak@gmail.com).
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
package com.dnastack.bob.service.parser.impl;

import com.dnastack.bob.persistence.entity.Beacon;
import com.dnastack.bob.service.parser.api.BeaconResponseParser;
import java.io.Serializable;
import java.util.concurrent.Future;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

import static com.dnastack.bob.service.parser.util.ParseUtils.parseRef;
import static com.dnastack.bob.service.parser.util.ParseUtils.parseYesNoCaseInsensitive;

/**
 * Parses "yes" and "no" strings with ref conversion.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Stateless
@Named
@LocalBean
public class StringYesNoRefBeaconResponseParser implements BeaconResponseParser, Serializable {

    private static final long serialVersionUID = -4790485566013440026L;

    @Asynchronous
    @Override
    public Future<Boolean> parseQueryResponse(Beacon b, String response) {
        Boolean res = parseYesNoCaseInsensitive(response);
        if (res == null) {
            // ref response is treated as false
            Boolean isRef = parseRef(response);
            if (isRef != null && isRef) {
                res = false;
            }
        }

        return new AsyncResult<>(res);
    }
}
