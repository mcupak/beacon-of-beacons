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

import com.dnastack.bob.persistence.enumerated.Chromosome;
import com.dnastack.bob.persistence.enumerated.Reference;
import lombok.*;
import lombok.experimental.Builder;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * Generalized beacon query representation.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@SuppressWarnings("deprecation")
@ToString
@EqualsAndHashCode(exclude = "id")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Query implements BasicEntity<Long> {

    private static final long serialVersionUID = -4843153796455403263L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Chromosome chromosome;
    @NotNull
    @Min(0L)
    @Max(300000000L)
    private Long position;
    @Pattern(regexp = "([A,C,T,G]+)")
    private String referenceAllele;
    @NotNull
    @Pattern(regexp = "([D,I])|([A,C,T,G]+)")
    private String allele;
    @Enumerated(EnumType.STRING)
    private Reference reference;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date submitted;
    private String ip;
    @ManyToOne
    private Dataset dataSet;
    @ManyToOne
    private User user;

}
