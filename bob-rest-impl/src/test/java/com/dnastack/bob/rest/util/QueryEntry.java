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
package com.dnastack.bob.rest.util;

import lombok.*;
import lombok.experimental.Builder;

/**
 * Query representation with beacon and expected response.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@SuppressWarnings("deprecation")
@ToString
@EqualsAndHashCode
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryEntry {

    private String beacon;
    private String chromosome;
    private Long position;
    private String reference;
    private String allele;
    private Boolean response;
    private String description;

    public QueryEntry(QueryEntry q) {
        this.beacon = q.getBeacon();
        this.chromosome = q.getChromosome();
        this.position = q.getPosition();
        this.reference = q.getReference();
        this.allele = q.getAllele();
        this.response = q.getResponse();
        this.description = q.getDescription();
    }

}
