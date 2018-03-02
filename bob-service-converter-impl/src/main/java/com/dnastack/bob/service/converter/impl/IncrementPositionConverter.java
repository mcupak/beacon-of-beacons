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
package com.dnastack.bob.service.converter.impl;

import com.dnastack.bob.service.converter.api.PositionConverter;

import javax.inject.Named;
import java.io.Serializable;

/**
 * Position converter incrementing the original position by 1.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Named
public class IncrementPositionConverter implements PositionConverter, Serializable {

    private static final long serialVersionUID = 8908286953351752283L;

    @Override
    public Long convert(Long input) {
        return (input == null) ? null : input + 1;
    }

}
