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
package com.dnastack.bob.rest.util;

import java.util.Objects;

/**
 * Query representation with beacon and expected response.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class QueryEntry {

    private String beacon;
    private String chromosome;
    private Long position;
    private String reference;
    private String allele;
    private Boolean response;
    private String description;

    public QueryEntry() {
    }

    public QueryEntry(QueryEntry q) {
        this.beacon = q.getBeacon();
        this.chromosome = q.getChromosome();
        this.position = q.getPosition();
        this.reference = q.getReference();
        this.allele = q.getAllele();
        this.response = q.getResponse();
        this.description = q.getDescription();
    }

    public QueryEntry(String beacon, String chromosome, Long position, String reference, String allele, Boolean response, String description) {
        this.beacon = beacon;
        this.chromosome = chromosome;
        this.position = position;
        this.reference = reference;
        this.allele = allele;
        this.response = response;
        this.description = description;
    }

    public String getBeacon() {
        return beacon;
    }

    public void setBeacon(String beacon) {
        this.beacon = beacon;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getAllele() {
        return allele;
    }

    public void setAllele(String allele) {
        this.allele = allele;
    }

    public Boolean getResponse() {
        return response;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.beacon);
        hash = 67 * hash + Objects.hashCode(this.chromosome);
        hash = 67 * hash + Objects.hashCode(this.position);
        hash = 67 * hash + Objects.hashCode(this.reference);
        hash = 67 * hash + Objects.hashCode(this.allele);
        hash = 67 * hash + Objects.hashCode(this.response);
        hash = 67 * hash + Objects.hashCode(this.description);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final QueryEntry other = (QueryEntry) obj;
        if (!Objects.equals(this.beacon, other.beacon)) {
            return false;
        }
        if (!Objects.equals(this.chromosome, other.chromosome)) {
            return false;
        }
        if (!Objects.equals(this.position, other.position)) {
            return false;
        }
        if (!Objects.equals(this.reference, other.reference)) {
            return false;
        }
        if (!Objects.equals(this.allele, other.allele)) {
            return false;
        }
        if (!Objects.equals(this.response, other.response)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "QueryEntry{" + "beacon=" + beacon + ", chromosome=" + chromosome + ", position=" + position + ", reference=" + reference + ", allele=" + allele + ", response=" + response + ", description=" + description + '}';
    }

}
