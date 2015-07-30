/*
 * The MIT License
 *
 * Copyright 2014 DNAstack.
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
package com.dnastack.bob.rest.comparator;

import com.dnastack.bob.service.dto.BeaconResponseDto;
import java.util.Comparator;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.NonNull;

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
