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
package com.dnastack.bob.lrg;

/**
 *
 * @author mfiume
 */
class Mapping {

    private final Coordinates sourceCoordinates;
    private final Coordinates targetCoordinates;

    Mapping(Coordinates sourceCoordinates, Coordinates targetCoordinates) {
        this.sourceCoordinates = sourceCoordinates;
        this.targetCoordinates = targetCoordinates;

        checkNulls(sourceCoordinates);
        checkNulls(targetCoordinates);
        checkSpans(sourceCoordinates, targetCoordinates);
    }

    @Override
    public String toString() {
        return "Mapping{" + "lrgCoords=" + sourceCoordinates + ", otherCoords=" + targetCoordinates + '}';
    }

    public Coordinates mapForward(Coordinates coord) {
        return map(sourceCoordinates, targetCoordinates, coord);
    }

    public Coordinates mapBackward(Coordinates coord) {
        return map(targetCoordinates, sourceCoordinates, coord);
    }

    public Coordinates getSourceCoordinates() {
        return sourceCoordinates;
    }

    public Coordinates getTargetCoordinates() {
        return targetCoordinates;
    }

    private Coordinates map(Coordinates fromCoordinates, Coordinates toCoordinates, Coordinates coord) throws IndexOutOfBoundsException {

        checkInBounds(fromCoordinates, coord);

        String remappedName = toCoordinates.getName();
        String remappedLocus = toCoordinates.getLocus();
        String remappedStrand = toCoordinates.getStrand();

        long startDiff = coord.getStart() - fromCoordinates.getStart();
        long endDiff = coord.getEnd() - fromCoordinates.getStart();

        long remappedStart = toCoordinates.getStart() + startDiff;
        long remappedEnd = toCoordinates.getStart() + endDiff;

        Coordinates remappedCoords = new Coordinates(remappedName, remappedLocus, remappedStart, remappedEnd, remappedStrand);
        checkInBounds(toCoordinates, remappedCoords);

        return remappedCoords;
    }

    private void checkInBounds(Coordinates target, Coordinates query) throws IndexOutOfBoundsException {

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
        if (query.getStrand() == null ? target.getStrand() != null : !query.getStrand().equals(target.getStrand())) {
            throw new IndexOutOfBoundsException("Strand mismatch: " + query.getEnd() + " does not match " + target.getEnd());
        }
    }

    private void checkNulls(Coordinates coords) {
        if (coords.getName() == null) {
            throw new RuntimeException("Name for coordinate is null");
        }
        if (coords.getLocus() == null) {
            throw new RuntimeException("Locus for coordinate is null");
        }
    }

    private void checkSpans(Coordinates sourceCoordinates, Coordinates targetCoordinates) {
        if (sourceCoordinates.getEnd() - sourceCoordinates.getStart() != targetCoordinates.getEnd() - targetCoordinates.getStart()) {
            throw new RuntimeException("Source and target lengths do not match");
        }
    }

}
