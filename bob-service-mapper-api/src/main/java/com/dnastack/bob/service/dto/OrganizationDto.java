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
    @ApiModelProperty(value = "ID of the organization.")
    private String id;
    @NotNull
    @ApiModelProperty(value = "Name of the organization.")
    private String name;
    @ApiModelProperty(value = "Description of the organization.")
    private String description;
    @NotNull
    @ApiModelProperty(value = "Date when the organization was registered in the system (ISO 8601 format).")
    private Date createdDate;
    @ApiModelProperty(value = "Web of the organization (RFC 1738 format).")
    private String url;
    @ApiModelProperty(value = "Address of the organization.")
    private String address;
    @ApiModelProperty(value = "Logo of the organization (Base64 encoding as per RFC 4648).")
    private byte[] logo;
}
