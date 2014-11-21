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
package com.dnastack.bob.dto;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * BeaconResponse DTO.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@XmlRootElement(name = "beacon-response")
public class BeaconResponseTo implements Serializable {

    private static final long serialVersionUID = 54L;

    private BeaconTo beacon;
    private QueryTo query;
    private Boolean response = null;

    public BeaconResponseTo() {
        // needed for JAXB
    }

    public BeaconResponseTo(BeaconTo beacon, QueryTo query, Boolean response) {
        this.beacon = beacon;
        this.query = query;
        this.response = response;
    }

    public BeaconTo getBeacon() {
        return beacon;
    }

    public void setBeacon(BeaconTo beacon) {
        this.beacon = beacon;
    }

    public QueryTo getQuery() {
        return query;
    }

    public void setQuery(QueryTo query) {
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
        final BeaconResponseTo other = (BeaconResponseTo) obj;
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
