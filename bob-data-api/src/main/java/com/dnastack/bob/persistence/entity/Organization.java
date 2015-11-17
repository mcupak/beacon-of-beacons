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
package com.dnastack.bob.persistence.entity;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@NamedQueries({
    @NamedQuery(name = "findOrganizationByName", query = "SELECT b FROM Organization b WHERE b.name=:name"),
    @NamedQuery(name = "findOrganizationsByIds", query = "SELECT b FROM Organization b WHERE b.id IN :ids"),
    @NamedQuery(name = "findOrganizationByIdAndVisibility", query = "SELECT o FROM Organization o LEFT OUTER JOIN o.beacons b WHERE o.id=:id AND b.visible=:visible"),
    @NamedQuery(name = "findOrganizationsByIdsAndVisibility", query = "SELECT DISTINCT o FROM Organization o LEFT OUTER JOIN o.beacons b WHERE o.id IN :ids AND b.visible=:visible"),
    @NamedQuery(name = "findOrganizationsByVisibility", query = "SELECT DISTINCT b.organization FROM Beacon b WHERE b.visible=:visible")
})
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
    private String description;
    private String url;
    private String address;
    private String logoUrl;
    @OneToMany(mappedBy = "organization")
    private Set<Beacon> beacons;

}