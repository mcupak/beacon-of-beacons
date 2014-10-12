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
package com.dnastack.bob.entity;

import com.dnastack.bob.processor.BeaconProcessor;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Beacon.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class Beacon implements Serializable {

    private static final long serialVersionUID = 2L;

    @NotNull
    @Size(min = 1)
    private String id;
    @NotNull
    @Size(min = 1)
    private String name;
    private String organization;
    private BeaconProcessor processor;
    private boolean visible;
    @NotNull
    private Set<Beacon> aggregators;

    public Beacon(String id, String name) {
        this.id = id;
        this.name = name;
        this.processor = null;
        this.visible = true;
        this.aggregators = new HashSet<>();
    }

    public Beacon(String id, String name, BeaconProcessor processor) {
        this.id = id;
        this.name = name;
        this.processor = processor;
        this.visible = true;
        this.aggregators = new HashSet<>();
    }

    public Beacon(String id, String name, BeaconProcessor processor, boolean visible) {
        this.id = id;
        this.name = name;
        this.processor = processor;
        this.visible = visible;
        this.aggregators = new HashSet<>();
    }

    public Beacon(String id, String name, BeaconProcessor processor, boolean visible, String organization) {
        this.id = id;
        this.name = name;
        this.processor = processor;
        this.visible = visible;
        this.organization = organization;
        this.aggregators = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BeaconProcessor getProcessor() {
        return processor;
    }

    public boolean isAggregator() {
        return getProcessor() == null;
    }

    public void setProcessor(BeaconProcessor processor) {
        this.processor = processor;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Set<Beacon> getAggregators() {
        return Collections.unmodifiableSet(aggregators);
    }

    public void addAggregator(Beacon beacon) {
        if (beacon == null) {
            throw new NullPointerException("beacon");
        }
        if (aggregators == null) {
            aggregators = new HashSet<>();
        }
        aggregators.add(beacon);
    }

    public void removeAggregator(Beacon beacon) {
        if (beacon == null) {
            throw new NullPointerException("beacon");
        }
        if (aggregators != null) {
            aggregators.remove(beacon);
        }
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.name);
        hash = 53 * hash + Objects.hashCode(this.organization);
        hash = 53 * hash + Objects.hashCode(this.processor);
        hash = 53 * hash + (this.visible ? 1 : 0);
        hash = 53 * hash + Objects.hashCode(this.aggregators);
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
        final Beacon other = (Beacon) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.organization, other.organization)) {
            return false;
        }
        if (!Objects.equals(this.processor, other.processor)) {
            return false;
        }
        if (this.visible != other.visible) {
            return false;
        }
        if (!Objects.equals(this.aggregators, other.aggregators)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Beacon{" + "id=" + id + ", name=" + name + ", organization=" + organization + '}';
    }

}
