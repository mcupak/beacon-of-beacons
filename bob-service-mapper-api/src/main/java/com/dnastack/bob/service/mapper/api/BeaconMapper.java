/*
 * The MIT License
 *
 * Copyright 2015 DNAstack.
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
package com.dnastack.bob.service.mapper.api;

import com.dnastack.bob.persistence.entity.Beacon;
import com.dnastack.bob.service.dto.BeaconDto;

import java.util.Map;
import java.util.Set;

/**
 * Mapper of beacons to their DTOs.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public interface BeaconMapper extends Mapper<Beacon, BeaconDto> {

    /**
     * Maps a beacon and its descendants to its DTO.
     *
     * @param b            beacon
     * @param descendants  descendants
     * @param showInternal true if internal fields should be included in the DTO, false otherwise
     * @return DTO
     */
    BeaconDto mapEntityToDto(Beacon b, Set<Beacon> descendants, boolean showInternal);

    /**
     * Maps beacons and their descendants to their DTOs.
     *
     * @param beaconsWithDescendants map of beacons to their descendants
     * @param showInternal           true if internal fields should be included in the DTO, false otherwise
     * @return DTOs
     */
    Set<BeaconDto> mapEntitiesToDtos(Map<Beacon, Set<Beacon>> beaconsWithDescendants, boolean showInternal);

}
