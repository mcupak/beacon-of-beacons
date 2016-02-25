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
package com.dnastack.bob.service.lrg;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Objects;

/**
 * LRG mapping.
 *
 * @author mfiume
 */
@ToString
@EqualsAndHashCode
public class LrgMapping {

    @Getter
    private final LrgCoordinates sourceCoordinates;
    @Getter
    private final LrgCoordinates targetCoordinates;

    public LrgMapping(@NonNull LrgCoordinates sourceCoordinates, @NonNull LrgCoordinates targetCoordinates) {
        this.sourceCoordinates = sourceCoordinates;
        this.targetCoordinates = targetCoordinates;

        checkSpans(sourceCoordinates, targetCoordinates);
    }

    public LrgCoordinates mapForward(LrgCoordinates coord) {
        return map(sourceCoordinates, targetCoordinates, coord);
    }

    public LrgCoordinates mapBackward(LrgCoordinates coord) {
        return map(targetCoordinates, sourceCoordinates, coord);
    }

    private LrgCoordinates map(LrgCoordinates fromCoordinates, LrgCoordinates toCoordinates, LrgCoordinates coord) throws IndexOutOfBoundsException {
        checkInBounds(fromCoordinates, coord);

        String remappedName = toCoordinates.getName();
        String remappedLocus = toCoordinates.getLocus();
        Boolean remappedStrand = toCoordinates.getPositiveStrand();

        long startDiff = coord.getStart() - fromCoordinates.getStart();
        long endDiff = coord.getEnd() - fromCoordinates.getStart();

        long remappedStart = toCoordinates.getStart() + startDiff;
        long remappedEnd = toCoordinates.getStart() + endDiff;

        // TODO:double check
        if (!Objects.equals(fromCoordinates.getPositiveStrand(), toCoordinates.getPositiveStrand())) {
            remappedStart = toCoordinates.getEnd() - startDiff;
            remappedEnd = toCoordinates.getEnd() - endDiff;
        }

        LrgCoordinates remappedCoords = new LrgCoordinates(remappedName, remappedLocus, remappedStart, remappedEnd, remappedStrand);
        checkInBounds(toCoordinates, remappedCoords);

        return remappedCoords;
    }

    private void checkInBounds(LrgCoordinates target, LrgCoordinates query) throws IndexOutOfBoundsException {
        // check name
        if (!query.getName().equals(target.getName())) {
            throw new IndexOutOfBoundsException("Name mismatch: " + query.getName() + " does not match target name " + target.getName());
        }

        // check locus
        if (!query.getLocus().equals(target.getLocus())) {
            throw new IndexOutOfBoundsException("Locus mismatch: " + query.getLocus() + " does not match target locus " + target.getLocus());
        }

        // check start
        if (query.getStart() < target.getStart()) {
            throw new IndexOutOfBoundsException("Position out of range: " + query.getStart() + " is before target start " + target.getStart());
        }

        // check end
        if (query.getEnd() > target.getEnd()) {
            throw new IndexOutOfBoundsException("Position out of range: " + query.getEnd() + " is after target end " + target.getEnd());
        }

        // check strand
        if (query.getPositiveStrand() == null ? target.getPositiveStrand() != null : !query.getPositiveStrand().equals(target.getPositiveStrand())) {
            throw new IndexOutOfBoundsException("Strand mismatch: " + query.getPositiveStrand() + " does not match " + target.getPositiveStrand());
        }
    }

    private void checkSpans(LrgCoordinates sourceCoordinates, LrgCoordinates targetCoordinates) {
        if (sourceCoordinates.getEnd() - sourceCoordinates.getStart() != targetCoordinates.getEnd() - targetCoordinates.getStart()) {
            throw new IllegalArgumentException("Source and target lengths do not match");
        }
    }

}
