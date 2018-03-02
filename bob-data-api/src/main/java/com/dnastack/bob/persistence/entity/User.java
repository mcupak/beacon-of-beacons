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

/**
 * User.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@SuppressWarnings("deprecation")
@ToString
@EqualsAndHashCode(exclude = {"id"})
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedQueries({@NamedQuery(name = "findUserByUserName", query = "SELECT b FROM User b WHERE b.userName=:userName"), @NamedQuery(name = "findUserByUserNameNull", query = "SELECT b FROM User b WHERE b.userName IS NULL")})
public class User implements BasicEntity<Long> {

    private static final long serialVersionUID = 7621625748088389070L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotNull
    @Column(unique = true, nullable = false)
    private String userName;

}
