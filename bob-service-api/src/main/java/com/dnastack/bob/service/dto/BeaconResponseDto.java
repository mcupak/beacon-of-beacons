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
package com.dnastack.bob.service.dto;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * BeaconResponse DTO.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@XmlRootElement(name = "beacon-response")
public class BeaconResponseDto implements Serializable {

    private static final long serialVersionUID = 54L;

    private BeaconDto beacon;
    private QueryDto query;
    private Boolean response = null;

    public BeaconResponseDto() {
        // needed for JAXB
    }

    public BeaconResponseDto(BeaconDto beacon, QueryDto query, Boolean response) {
        this.beacon = beacon;
        this.query = query;
        this.response = response;
    }

    public BeaconDto getBeacon() {
        return beacon;
    }

    public void setBeacon(BeaconDto beacon) {
        this.beacon = beacon;
    }

    public QueryDto getQuery() {
        return query;
    }

    public void setQuery(QueryDto query) {
        this.query = query;
    }

    public Boolean getResponse() {
        return response;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.beacon != null ? this.beacon.hashCode() : 0);
        hash = 41 * hash + (this.query != null ? this.query.hashCode() : 0);
        hash = 41 * hash + (this.response != null ? this.response.hashCode() : 0);
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
        final BeaconResponseDto other = (BeaconResponseDto) obj;
        if (this.beacon != other.beacon && (this.beacon == null || !this.beacon.equals(other.beacon))) {
            return false;
        }
        if (this.query != other.query && (this.query == null || !this.query.equals(other.query))) {
            return false;
        }
        if (this.response != other.response && (this.response == null || !this.response.equals(other.response))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Response of '" + beacon + "' to '" + query + "': '" + response + "'";
    }

}
