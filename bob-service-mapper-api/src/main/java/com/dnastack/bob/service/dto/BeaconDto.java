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
import java.util.Date;
import java.util.Set;

/**
 * Beacon DTO.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@XmlRootElement(name = "beacon")
@XmlType(name = "beacon")
@ToString
@EqualsAndHashCode
@Builder
@Getter
@Setter
@NoArgsConstructor // needed for JAXB
@AllArgsConstructor
@ApiModel(value = "Beacon")
@SuppressWarnings("deprecation")
public class BeaconDto implements Serializable {

    private static final long serialVersionUID = -3853326944626100071L;

    @NotNull
    @ApiModelProperty(value = "ID of the beacon.")
    private String id;
    @NotNull
    @ApiModelProperty(value = "Name of the beacon.")
    private String name;
    @ApiModelProperty(hidden = true)
    private String url;
    @ApiModelProperty(value = "Name of the organization.")
    private String organization;
    @ApiModelProperty(value = "Description of the beacon.")
    private String description;
    @ApiModelProperty(hidden = true)
    private String homePage;
    @ApiModelProperty(hidden = true)
    private String email;
    @ApiModelProperty(value = "true if the beacon is a virtual aggregator of beacons, false otherwise.")
    private boolean aggregator;
    @ApiModelProperty(hidden = true)
    private boolean enabled;
    @ApiModelProperty(hidden = true)
    private boolean visible;
    @ApiModelProperty(value = "Date when the beacon was registered (ISO 8601 format).")
    private Date createdDate;
    @ApiModelProperty(value = "List of references supported by the beacon (HG-based notation).")
    private Set<ReferenceDto> supportedReferences;
    @ApiModelProperty(value = "List of exposed non-aggregating beacons aggregated by this aggreagetor, or null if the beacon is not an aggreagator.")
    private Set<BeaconDto> aggregatedBeacons;

}
