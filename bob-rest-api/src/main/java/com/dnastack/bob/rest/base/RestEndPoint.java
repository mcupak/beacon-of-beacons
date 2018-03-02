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

import lombok.*;
import lombok.experimental.Builder;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Rest endpoint representation.
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
@XmlRootElement(name = "rest-end-point")
public class RestEndPoint implements Serializable {

    private static final long serialVersionUID = 35L;
    private String id;
    private String baseUrl;
    private String example;

}
