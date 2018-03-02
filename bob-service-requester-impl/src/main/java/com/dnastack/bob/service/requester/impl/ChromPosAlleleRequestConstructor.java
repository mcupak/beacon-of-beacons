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
package com.dnastack.bob.service.requester.impl;

import com.dnastack.bob.service.requester.api.RequestConstructor;

import javax.inject.Named;
import java.io.Serializable;
import java.util.Map;

/**
 * Request constructor using URL with chrom, pos, allele params.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Named
public class ChromPosAlleleRequestConstructor implements RequestConstructor, Serializable {

    private static final long serialVersionUID = -4140519271564294181L;

    @Override
    public String getUrl(String template, String beacon, String ref, String chrom, Long pos, String referenceAllele, String allele, String dataset) {
        return String.format(template, chrom, pos, allele);
    }

    @Override
    public Map<String, String> getPayload(String template, String beacon, String ref, String chrom, Long pos, String referenceAllele, String allele, String dataset) {
        return null;
    }

}
