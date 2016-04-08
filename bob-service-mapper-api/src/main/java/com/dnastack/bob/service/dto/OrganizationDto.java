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

/**
 * Organization DTO.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@XmlRootElement(name = "organization")
@XmlType(name = "organization")
@ToString
@EqualsAndHashCode
@Builder
@Getter
@Setter
@NoArgsConstructor // needed for JAXB
@AllArgsConstructor
@ApiModel(value = "Organization")
@SuppressWarnings("deprecation")
public class OrganizationDto implements Serializable {

    private static final long serialVersionUID = 5579930786521160428L;

    @NotNull
    @ApiModelProperty(value = "ID of the organization.", example = "wtsi")
    private String id;
    @NotNull
    @ApiModelProperty(value = "Name of the organization.", example = "Wellcome Trust Sanger Institute")
    private String name;
    @ApiModelProperty(value = "Description of the organization.", example = "")
    private String description;
    @NotNull
    @ApiModelProperty(value = "Date when the organization was registered in the system (ISO 8601 format).", example = "2014-06-23")
    private Date createdDate;
    @ApiModelProperty(value = "Web of the organization (RFC 1738 format).", example = "https://www.sanger.ac.uk")
    private String url;
    @ApiModelProperty(value = "Address of the organization.", example = "Wellcome Trust Genome Campus, Hinxton, Cambridge. CB10 1SA, United Kingdom")
    private String address;
    @ApiModelProperty(value = "Logo of the organization (Base64 encoding as per RFC 4648).", example = "iVBORw0KGg")
    private byte[] logo;
}
