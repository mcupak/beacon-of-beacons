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
package com.dnastack.bob.service.converter.impl;

import com.dnastack.bob.persistence.enumerated.Chromosome;
import com.dnastack.bob.service.converter.api.ChromosomeConverter;

import javax.inject.Named;
import java.io.Serializable;

/**
 * Converter of chromosomes to their string representations, X/Y chromosomes to number (23/24, respectively).
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Named
public class NumberChromosomeConverter implements ChromosomeConverter, Serializable {

    private static final long serialVersionUID = 6434813857469055140L;

    @Override
    public String convert(Chromosome input) {
        String res = (input == null) ? null : input.toString();
        if (null != res) {
            switch (res) {
                case "X":
                    return "23";
                case "Y":
                    return "24";
                case "MT":
                    return "25";
                default:
                    return res;
            }
        }

        return null;
    }

}
