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
