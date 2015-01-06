/*
 * The MIT License
 *
 * Copyright 2014 DNAstack.
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
package com.dnastack.bob.log;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * Logging interceptor.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Logged
@Interceptor
public class Logger implements Serializable {

    private static final String APP_NAME = "BEACON-AGGREGATOR";
    private static final long serialVersionUID = 20L;
    private static final DateFormat DATE_FORMAT = SimpleDateFormat
            .getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

    @AroundInvoke
    public Object log(InvocationContext context) throws Exception {
        System.out.println("LOGGER: " + DATE_FORMAT.format(new Date()) + " : " + APP_NAME + ": "
                + context.getMethod().getName() + "(" + Arrays.toString(context.getParameters()) + ")" + context.getContextData().toString());

        return context.proceed();
    }

}
