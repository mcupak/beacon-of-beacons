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

import com.dnastack.bob.persistence.entity.Beacon;
import com.dnastack.bob.service.converter.api.BeaconConverter;

import javax.inject.Named;
import java.io.Serializable;

import static com.dnastack.bob.service.converter.util.ConvertUtils.extractIdWithoutOrganizationFromBeaconId;

/**
 * Beacon converter returning the ID of the beacon without the organization ID prefix.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Named
public class BeaconIdWithoutOrganizationBeaconConverter implements BeaconConverter, Serializable {

    private static final long serialVersionUID = -2087059187273663405L;

    @Override
    public String convert(Beacon input) {
        return (input == null) ? null : extractIdWithoutOrganizationFromBeaconId(input);
    }

}
