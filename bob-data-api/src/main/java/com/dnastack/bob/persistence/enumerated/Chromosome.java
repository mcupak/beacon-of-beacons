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
package com.dnastack.bob.persistence.enumerated;

/**
 * Canonical chromosome representation.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public enum Chromosome {

    // order is important!
    CHR22("22"), CHR21("21"), CHR20("20"), CHR19("19"), CHR18("18"), CHR17("17"), CHR16("16"), CHR15("15"), CHR14("14"), CHR13(
            "13"), CHR12("12"), CHR11("11"), CHR10("10"), CHR9("9"), CHR8("8"), CHR7("7"), CHR6("6"), CHR5("5"), CHR4(
            "4"), CHR3("3"), CHR2("2"), CHR1("1"), CHRX("X"), CHRY("Y"), CHRMT("MT");

    private final String chrom;

    private Chromosome(String chrom) {
        this.chrom = chrom;
    }

    public static Chromosome fromString(String text) {
        if (text != null) {
            for (Chromosome b : Chromosome.values()) {
                if (text.equalsIgnoreCase(b.toString())) {
                    return b;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return chrom;
    }

}
