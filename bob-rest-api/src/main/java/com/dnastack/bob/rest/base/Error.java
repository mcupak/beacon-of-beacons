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
package com.dnastack.bob.rest.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Builder;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Application error.
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
@ApiModel(value = "Error")
@XmlRootElement(name = "error")
public class Error implements Serializable {

    private static final long serialVersionUID = 7211701892878610009L;

    @NotNull
    @ApiModelProperty(value = "Status code.")
    private Integer status;
    @NotNull
    @ApiModelProperty(value = "Reason phrase.")
    private String reason;
    @NotNull
    @ApiModelProperty(value = "Error message.")
    private String message;
    @ApiModelProperty(value = "Stack trace.")
    private String stackTrace;

}
