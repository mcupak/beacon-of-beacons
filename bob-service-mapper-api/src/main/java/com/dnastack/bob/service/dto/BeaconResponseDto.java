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

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Map;

/**
 * BeaconResponse DTO.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@XmlRootElement(name = "beacon-response")
@XmlType(name = "beacon-response")
@ToString
@EqualsAndHashCode
@Builder
@Getter
@Setter
@NoArgsConstructor // needed for JAXB
@AllArgsConstructor
@ApiModel(value = "BeaconResponse")
@SuppressWarnings("deprecation")
public class BeaconResponseDto implements Serializable {

    private static final long serialVersionUID = 7784232950079896515L;

    @NotNull
    @ApiModelProperty(value = "Beacon providing the response.")
    private BeaconDto beacon;
    @NotNull
    @ApiModelProperty(value = "Query as understood by the beacon.")
    private QueryDto query;
    @ApiModelProperty(value = "Response to the query, null indicates an error.")
    private Boolean response = null;
    @ApiModelProperty(value = "Allele frequency.")
    private Double frequency = null;
    @ApiModelProperty(value = "URL to an external system providing more information about the given allele (RFC 1738 format).")
    private String externalUrl = null;
    @ApiModelProperty(value = "Custom information provided by the beacon in key-value string pairs.")
    private Map<String, String> info = null;

}
