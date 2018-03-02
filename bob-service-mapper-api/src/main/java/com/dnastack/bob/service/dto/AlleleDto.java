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
package com.dnastack.bob.service.dto;

import javax.xml.bind.annotation.XmlType;

/**
 * Canonical allele representation.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@XmlType(name = "allele")
public enum AlleleDto {

    A("A"), C("C"), G("G"), T("T"), D("D"), I("I");

    private final String allele;

    private AlleleDto(String allele) {
        this.allele = allele;
    }

    public static AlleleDto fromString(String text) {
        if (text != null) {
            for (AlleleDto b : AlleleDto.values()) {
                if (text.equalsIgnoreCase(b.toString())) {
                    return b;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return allele;
    }

}
