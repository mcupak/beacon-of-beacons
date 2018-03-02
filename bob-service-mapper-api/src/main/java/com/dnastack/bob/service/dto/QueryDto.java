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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Builder;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Query DTO.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@XmlRootElement(name = "query")
@XmlType(name = "query")
@ToString
@EqualsAndHashCode
@Builder
@Getter
@Setter
@NoArgsConstructor // needed for JAXB
@AllArgsConstructor
@ApiModel(value = "Query")
@SuppressWarnings("deprecation")
public class QueryDto implements Serializable {

    private static final long serialVersionUID = 4711685616673439706L;

    @ApiModelProperty(value = "Chromosome ID (1-22, X, Y, MT).")
    private ChromosomeDto chromosome;
    @ApiModelProperty(value = "Coordinate within a chromosome (0-based).")
    private Long position;
    @ApiModelProperty(value = "String of nucleotides A,C,T,G.")
    private String referenceAllele;
    @ApiModelProperty(value = "String of nucleotides A,C,T,G or D, I for deletion and insertion, respectively.")
    private String allele;
    @ApiModelProperty(value = "Genome/assembly ID (HG-based notation).")
    private ReferenceDto reference;

}
