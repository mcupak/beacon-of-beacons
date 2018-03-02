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
import java.util.HashMap;
import java.util.Map;

/**
 * Representation of a query result provided by a beacon.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@ToString
@EqualsAndHashCode
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("deprecation")
@Entity
public class BeaconResponse implements BasicEntity<Long> {

    public static final int STRING_MAX_LENGTH = 511;
    public static final int STRING_DEFAULT_LENGTH = 255;
    private static final long serialVersionUID = 2318476024983822938L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    private Beacon beacon;
    @ManyToOne
    private Query query;
    private Boolean response = null;
    private Double frequency = null;
    @Column(length = STRING_MAX_LENGTH)
    private String externalUrl = null;
    @ElementCollection(fetch = FetchType.LAZY)
    @Column(length = STRING_MAX_LENGTH)
    private Map<String, String> info = null;

    @PrePersist
    @PreUpdate
    private void truncate() {
        if (getExternalUrl() != null && getExternalUrl().length() > STRING_MAX_LENGTH) {
            setExternalUrl(getExternalUrl() == null ? null : getExternalUrl().substring(0, STRING_MAX_LENGTH - 1));
        }
        if (getInfo() != null) {
            Map<String, String> tmp = new HashMap<>(getInfo());
            for (Map.Entry<String, String> e : tmp.entrySet()) {
                String key = e.getKey();
                if (e.getKey().length() > STRING_DEFAULT_LENGTH) {
                    getInfo().remove(key);
                    key = key.substring(0, STRING_DEFAULT_LENGTH);
                }
                getInfo().put(key,
                              e.getValue().substring(0, Integer.min(STRING_MAX_LENGTH - 1, e.getValue().length())));
            }
        }
    }
}
