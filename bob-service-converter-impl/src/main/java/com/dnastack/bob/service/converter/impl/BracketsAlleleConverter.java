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
package com.dnastack.bob.service.converter.impl;

import com.dnastack.bob.service.converter.api.AlleleConverter;
import java.io.Serializable;
import javax.inject.Named;

/**
 * Allele converter with indels converted to long strings and wrapped in <> (url encoded).
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Named
public class BracketsAlleleConverter implements AlleleConverter, Serializable {

    private static final long serialVersionUID = -2526002182337944545L;

    @Override
    public String convert(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        String res = input.toUpperCase();
        if (res.equals("D")) {
            res = "DEL";
        }
        if (res.equals("I")) {
            res = "INS";
        }
        if (res.equals("DEL") || res.equals("INS")) {
            res = "%3C" + res + "%3E";
        }

        return res;
    }

}
