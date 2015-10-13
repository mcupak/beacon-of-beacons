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

import com.dnastack.bob.persistence.enumerated.Reference;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Builder;

/**
 * Beacon.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@SuppressWarnings("deprecation")
@ToString(exclude = {"parents", "children", "datasets"})
@EqualsAndHashCode(exclude = {"parents", "children", "datasets"})
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedQueries({
    @NamedQuery(name = "findBeaconsByAggregation", query = "SELECT b FROM Beacon b WHERE b.aggregator=:aggregator"),
    @NamedQuery(name = "findBeaconByIdAndVisibility", query = "SELECT b FROM Beacon b WHERE b.id=:id AND b.visible=:visible"),
    @NamedQuery(name = "findBeaconsByIdsAndVisibility", query = "SELECT b FROM Beacon b WHERE b.id IN :ids AND b.visible=:visible"),
    @NamedQuery(name = "findBeaconsByIds", query = "SELECT b FROM Beacon b WHERE b.id IN :ids"),
    @NamedQuery(name = "findBeaconsByVisibility", query = "SELECT b FROM Beacon b WHERE b.visible=:visible")
})
public class Beacon implements BasicEntity<String> {

    private static final long serialVersionUID = -1451238811291388547L;

    @Id
    @NotNull
    @Column(nullable = false, unique = true)
    @Size(min = 1)
    private String id;
    @NotNull
    @Size(min = 1)
    @Column(nullable = false)
    private String name;
    private String url;
    @ManyToOne
    @NotNull
    private Organization organization;
    @Size(max = 1000)
    @Column(length = 1000)
    private String description;
    private String api;
    private String homePage;
    private String email;
    private String auth;

    private String responseParser;
    private String externalUrlParser;
    private String fetcher;
    private String requester;
    private String chromosomeConverter;
    private String positionConverter;
    private String alleleConverter;
    private String referenceConverter;
    private String beaconConverter;
    // TODO: query from datasets or cache properly
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Reference> supportedReferences;

    @NotNull
    @Column(nullable = false)
    private Boolean visible = true;
    @NotNull
    @Column(nullable = false)
    private Boolean enabled = true;
    @NotNull
    @Column(nullable = false)
    private Boolean aggregator = false;
    @ManyToMany
    private Set<Beacon> parents;
    @ManyToMany(mappedBy = "parents")
    private Set<Beacon> children;
    @OneToMany(mappedBy = "beacon")
    private Set<Dataset> datasets;

}
