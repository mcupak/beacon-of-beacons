/*
 * The MIT License
 *
 * Copyright 2014 Miroslav Cupak (mirocupak@gmail.com).
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
    @ApiModelProperty(value = "ID of the beacon.", example = "cosmic")
    private String id;
    @NotNull
    @ApiModelProperty(value = "Name of the beacon.", example = "COSMIC")
    private String name;
    @ApiModelProperty(hidden = true)
    private String url;
    @ApiModelProperty(value = "Name of the organization.", example = "Wellcome Trust Sanger Institute")
    private String organization;
    @ApiModelProperty(value = "Description of the beacon.", example = "")
    private String description;
    @ApiModelProperty(hidden = true)
    private String homePage;
    @ApiModelProperty(hidden = true)
    private String email;
    @ApiModelProperty(value = "true if the beacon is a virtual aggregator of beacons, false otherwise.", example = "false")
    private boolean aggregator;
    @ApiModelProperty(hidden = true)
    private boolean enabled;
    @NotNull
    @ApiModelProperty(value = "Date when the beacon was registered (ISO 8601 format).-06-23")
    private Date createdDate;
    @ApiModelProperty(value = "List of references supported by the beacon (HG-based notation).", example = "[\"HG19\",\"HG38\"]")
    private Set<ReferenceDto> supportedReferences;
    @ApiModelProperty(value = "List of exposed non-aggregating beacons aggregated by this aggreagetor, or null if the beacon is not an aggreagator.", example = "[]")
    private Set<BeaconDto> aggregatedBeacons;

}
