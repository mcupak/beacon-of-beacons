/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dnastack.beacon.query;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Representation of a query result provided by a beacon.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@XmlRootElement
public class BeaconResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Beacon beacon = null;
    private Query query = null;
    private Boolean response = null;

    public BeaconResponse() {
    }

    public BeaconResponse(Beacon beacon, Query query, Boolean response) {
        this.beacon = beacon;
        this.query = query;
        this.response = response;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public Boolean isResponse() {
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
        final BeaconResponse other = (BeaconResponse) obj;
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
        return "BeaconResponse{" + "beacon=" + beacon + ", query=" + query + ", response=" + response + '}';
    }

}
