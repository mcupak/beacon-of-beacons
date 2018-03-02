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
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

/**
 * Beacon's data set.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@SuppressWarnings("deprecation")
@ToString(exclude = {"dataUses", "queries"})
@EqualsAndHashCode(exclude = {"dataUses", "queries"})
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Dataset implements BasicEntity<String> {

    private static final long serialVersionUID = 469349231640573964L;

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;
    @NotNull
    @Size(min = 1)
    @Column(name = "name", nullable = false)
    private String name;
    private String description;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Reference reference;
    @Embedded
    private DataSize size;
    @ManyToOne
    private Beacon beacon;
    @ManyToMany
    private Set<DataUse> dataUses;
    @OneToMany(mappedBy = "dataSet")
    private List<Query> queries;

}
