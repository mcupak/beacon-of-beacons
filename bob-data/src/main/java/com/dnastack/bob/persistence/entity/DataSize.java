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

import java.util.Objects;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * Data size.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Embeddable
public class DataSize implements BasicEntity {

    private static final long serialVersionUID = -3358978751981821234L;

    @NotNull
    private Long variants;
    @NotNull
    private Long samples;

    public DataSize() {
    }

    public DataSize(Long variants, Long samples) {
        this.variants = variants;
        this.samples = samples;
    }

    public Long getVariants() {
        return variants;
    }

    public void setVariants(Long variants) {
        this.variants = variants;
    }

    public Long getSamples() {
        return samples;
    }

    public void setSamples(Long samples) {
        this.samples = samples;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.variants);
        hash = 89 * hash + Objects.hashCode(this.samples);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DataSize other = (DataSize) obj;
        if (!Objects.equals(this.variants, other.variants)) {
            return false;
        }
        if (!Objects.equals(this.samples, other.samples)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DataSize{" + "variants=" + variants + ", samples=" + samples + '}';
    }

}
