/* 
 * Copyright 2018 DNAstack.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dnastack.bob.persistence.entity;

import com.dnastack.bob.persistence.enumerated.Reference;
import lombok.*;
import lombok.experimental.Builder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

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
@NamedQueries({@NamedQuery(name = "findBeaconsByAggregation", query = "SELECT b FROM Beacon b WHERE b.aggregator=:aggregator"), @NamedQuery(name = "findBeaconByIdAndVisibility", query = "SELECT b FROM Beacon b WHERE b.id=:id AND b.visible=:visible"), @NamedQuery(name = "findBeaconsByIdsAndVisibility", query = "SELECT b FROM Beacon b WHERE b.id IN :ids AND b.visible=:visible"), @NamedQuery(name = "findBeaconsByIds", query = "SELECT b FROM Beacon b WHERE b.id IN :ids"), @NamedQuery(name = "findBeaconsByVisibility", query = "SELECT b FROM Beacon b WHERE b.visible=:visible")})
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
    @NotNull
    @Past
    @Temporal(TemporalType.DATE)
    private Date createdDate;
    private String url;
    @Size(max = 1500)
    @Column(length = 1500)
    private String description;
    private String api;
    private String homePage;
    private String email;
    private String auth;

    private String responseParser;
    private String externalUrlParser;
    private String metadataParser;
    private String fetcher;
    private String requester;
    private String chromosomeConverter;
    private String positionConverter;
    private String alleleConverter;
    private String referenceConverter;
    private String beaconConverter;
    @NotNull
    @ManyToOne
    private Organization organization;
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
