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
package com.dnastack.bob.persistence.entity;

import com.dnastack.bob.processor.BeaconProcessor;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Beacon.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Entity
public class Beacon implements Serializable {

    private static final long serialVersionUID = -1451238811291388547L;

    @Id
    @NotNull
    @Column(nullable = false, unique = true)
    @Size(min = 1)
    private String id;
    @NotNull
    @Column(nullable = false)
    @Size(min = 1)
    private String name;
    @ManyToOne
    @NotNull
    private Organization organization;
    private String description;
    private String api;
    private String homePage;
    private String email;
    private String auth;
    private String processor;
    @NotNull
    @Column(nullable = false)
    private Boolean visible;
    @NotNull
    @OneToMany
    private Set<Beacon> aggregators;

    public Beacon() {
    }

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
        this.processor = processor.getClass().getCanonicalName();
        this.visible = true;
        this.aggregators = new HashSet<>();
    }

    public Beacon(String id, String name, BeaconProcessor processor, boolean visible) {
        this.id = id;
        this.name = name;
        this.processor = processor.getClass().getCanonicalName();
        this.visible = visible;
        this.aggregators = new HashSet<>();
    }

    public Beacon(String id, String name, BeaconProcessor processor, boolean visible, Organization organization) {
        this.id = id;
        this.name = name;
        this.processor = processor.getClass().getCanonicalName();
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

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public boolean isAggregator() {
        return getProcessor() == null;
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

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        return true;
    }

    @Override
    public String toString() {
        return "Beacon{" + "id=" + id + ", name=" + name + ", organization=" + organization + ", description=" + description + ", api=" + api + ", homePage=" + homePage + ", email=" + email + ", auth=" + auth + ", processor=" + processor + ", visible=" + visible + ", aggregators=" + aggregators + '}';
    }

}
