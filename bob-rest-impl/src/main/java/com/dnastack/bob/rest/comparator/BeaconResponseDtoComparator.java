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
package com.dnastack.bob.rest.comparator;

import com.dnastack.bob.service.dto.BeaconResponseDto;
import lombok.NonNull;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Comparator;

/**
 * Comparator of BeaconResponseDto objects. Performs comparison of BeaconDto objects in the responses.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RequestScoped
@Named
public class BeaconResponseDtoComparator implements Comparator<BeaconResponseDto> {

    @Inject
    @NameComparator
    private BeaconDtoComparator beaconComparator;

    @Override
    public int compare(@NonNull BeaconResponseDto o1, @NonNull BeaconResponseDto o2) {
        if (o1.getBeacon() == null || o2.getBeacon() == null) {
            throw new NullPointerException("Beacon is null.");
        }
        return beaconComparator.compare(o1.getBeacon(), o2.getBeacon());
    }

}
