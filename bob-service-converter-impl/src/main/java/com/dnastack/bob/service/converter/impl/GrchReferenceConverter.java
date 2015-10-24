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

import com.dnastack.bob.persistence.enumerated.Reference;
import com.dnastack.bob.service.converter.api.ReferenceConverter;
import java.io.Serializable;
import java.util.Map;
import javax.inject.Named;

import static com.dnastack.bob.service.converter.util.ConvertUtils.REFERENCE_MAPPING;

/**
 * Converter of references to their non-HG string representations ((GRCh*, NCBI*).
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Named
public class GrchReferenceConverter implements ReferenceConverter, Serializable {

    private static final long serialVersionUID = -2361871357937831818L;

    @Override
    public String convert(Reference input) {
        if (input == null) {
            return null;
        }

        for (String s : REFERENCE_MAPPING.values()) {
            if (s.equalsIgnoreCase(input.toString())) {
                return s;
            }
        }
        for (Map.Entry<Reference, String> e : REFERENCE_MAPPING.entrySet()) {
            if (e.getKey().equals(input)) {
                return e.getValue();
            }
        }

        return input.toString();
    }

}
