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
package com.dnastack.bob.service.requester.api;

import java.util.Map;

/**
 * Query URL generator.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public interface RequestConstructor {

    /**
     * Generates query URL for a given beacon.
     *
     * @param template        beacon url template
     * @param beacon          beacon
     * @param ref             reference genome
     * @param chrom           chromosome
     * @param pos             position
     * @param referenceAllele reference allele
     * @param allele          allele
     * @param dataset         dataset
     * @return URL
     */
    String getUrl(String template, String beacon, String ref, String chrom, Long pos, String referenceAllele, String allele, String dataset);

    /**
     * Generates query request payload.
     *
     * @param template        beacon url template
     * @param beacon          beacon
     * @param ref             reference genome
     * @param chrom           chromosome
     * @param pos             position
     * @param referenceAllele reference allele
     * @param allele          allele
     * @param dataset         dataset
     * @return payload key-value pairs
     */
    Map<String, String> getPayload(String template, String beacon, String ref, String chrom, Long pos, String referenceAllele, String allele, String dataset);

}
