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
import java.util.List;
import java.util.Set;

/**
 * Data use.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@SuppressWarnings("deprecation")
@ToString(exclude = {"datasets", "requirements"})
@EqualsAndHashCode(exclude = {"id", "datasets", "requirements"})
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DataUse implements BasicEntity<Long> {

    private static final long serialVersionUID = -1851160255605858159L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotNull
    @Column(nullable = false, unique = true)
    private String category;
    private String description;
    @ManyToMany
    private Set<DataUseRequirement> requirements;
    @ManyToMany(mappedBy = "dataUses")
    private List<Dataset> datasets;

}
