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
                getInfo().put(key, e.getValue().substring(0, Integer.min(STRING_MAX_LENGTH - 1, e.getValue().length())));
            }
        }
    }
}
