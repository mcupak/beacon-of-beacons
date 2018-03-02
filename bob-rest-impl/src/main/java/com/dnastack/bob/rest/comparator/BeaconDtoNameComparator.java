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

import com.dnastack.bob.service.dto.BeaconDto;
import lombok.NonNull;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Comparator of BeaconDto objects. Performs case-insensitive comparison of names of BeaconDto objects.
 * In case the names are equal, IDs are compared to distinguish similar beacons.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RequestScoped
@Named
@NameComparator
public class BeaconDtoNameComparator implements BeaconDtoComparator {

    @Inject
    @IdComparator
    private BeaconDtoComparator idComparator;

    @Override
    public int compare(@NonNull BeaconDto o1, @NonNull BeaconDto o2) {
        if (o1.getName() == null || o2.getName() == null) {
            throw new NullPointerException("Beacon ID is null.");
        }

        int i = o1.getName().compareToIgnoreCase(o2.getName());
        if (i == 0) {
            i = idComparator.compare(o1, o2);
        }

        return i;
    }

}
