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

import lombok.*;
import lombok.experimental.Builder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

/**
 * Organization owning a beacon.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@SuppressWarnings("deprecation")
@ToString(exclude = "beacons")
@EqualsAndHashCode(exclude = {"id", "beacons"})
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedQueries({@NamedQuery(name = "findOrganizationByName", query = "SELECT b FROM Organization b WHERE b.name=:name"), @NamedQuery(name = "findOrganizationsByIds", query = "SELECT b FROM Organization b WHERE b.id IN :ids"), @NamedQuery(name = "findOrganizationByIdAndVisibility", query = "SELECT o FROM Organization o LEFT OUTER JOIN o.beacons b WHERE o.id=:id AND b.visible=:visible"), @NamedQuery(name = "findOrganizationsByIdsAndVisibility", query = "SELECT DISTINCT o FROM Organization o LEFT OUTER JOIN o.beacons b WHERE o.id IN :ids AND b.visible=:visible"), @NamedQuery(name = "findOrganizationsByVisibility", query = "SELECT DISTINCT b.organization FROM Beacon b WHERE b.visible=:visible")})
public class Organization implements BasicEntity<String> {

    private static final long serialVersionUID = -2628422425515576512L;

    @Id
    @NotNull
    @Size(min = 1)
    @Column(nullable = false, unique = true)
    private String id;
    @NotNull
    @Size(min = 1)
    @Column(nullable = false, unique = true)
    private String name;
    @NotNull
    @Past
    @Temporal(TemporalType.DATE)
    private Date createdDate;
    private String description;
    private String url;
    private String address;
    @Lob
    private byte[] logo;
    @OneToMany(mappedBy = "organization")
    private Set<Beacon> beacons;

}
