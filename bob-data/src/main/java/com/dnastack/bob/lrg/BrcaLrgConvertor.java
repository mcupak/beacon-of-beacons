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
package com.dnastack.bob.lrg;

import com.dnastack.bob.entity.Chromosome;
import com.dnastack.bob.entity.Reference;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * BRCA convertor.
 * 
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@ApplicationScoped
@Named
@Brca
public class BrcaLrgConvertor implements LrgConvertor {

    private LrgMapping mapping;

    @PostConstruct
    private void init() {
        mapping = LrgMappingProvider.getMapping(LrgLocus.LRG_292.toString(), "GRCh37.p13");
    }

    @Override
    public Chromosome getChromosome() {
        return Chromosome.CHR17;
    }

    @Override
    public Reference getReference() {
        return Reference.HG19;
    }

    @Override
    public Long getPosition(long pos) {
        LrgCoordinates from = new LrgCoordinates(LrgReference.LRG.toString().toLowerCase(), LrgLocus.LRG_292.toString(), pos, pos, false);
        System.out.println(from);
        System.out.println(mapping);

        return mapping.mapForward(from).getStart();
    }
}
