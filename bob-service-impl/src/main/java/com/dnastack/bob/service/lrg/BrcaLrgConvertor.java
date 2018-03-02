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
package com.dnastack.bob.service.lrg;

import com.dnastack.bob.persistence.enumerated.Chromosome;
import com.dnastack.bob.persistence.enumerated.Reference;

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
        LrgCoordinates from = new LrgCoordinates(LrgReference.LRG.toString().toLowerCase(),
                                                 LrgLocus.LRG_292.toString(),
                                                 pos,
                                                 pos,
                                                 false);

        return mapping.mapForward(from).getStart();
    }
}
