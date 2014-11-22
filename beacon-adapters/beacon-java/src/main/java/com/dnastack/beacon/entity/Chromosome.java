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
package com.dnastack.beacon.entity;

/**
 * Chromosome.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public enum Chromosome {

    // order is important!
    CHR22("22"), CHR21("21"), CHR20("20"), CHR19("19"), CHR18("18"), CHR17("17"), CHR16("16"), CHR15("15"), CHR14("14"), CHR13("13"), CHR12("12"), CHR11("11"), CHR10("10"), CHR9("9"), CHR8("8"), CHR7("7"), CHR6("6"), CHR5("5"), CHR4("4"), CHR3("3"), CHR2("2"), CHR1("1"), CHRX("X"), CHRY("Y"), CHRMT("MT");

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
