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
package com.dnastack.beacon.entity;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Data set.
 */
@XmlRootElement(name = "dataset")
public class Dataset {

    private String id;
    private String description;
    private String reference;
    private DataSize size;
    private List<DataUse> data_uses;

    public Dataset() {
        // needed for JAXB
    }

    /*
     * required field(s): id
     */
    public Dataset(String id, String description, String reference, DataSize size, List<DataUse> data_uses) {
        this.id = id;
        this.description = description;
        this.reference = reference;
        this.size = size;
        this.data_uses = data_uses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public DataSize getSize() {
        return size;
    }

    public void setSize(DataSize size) {
        this.size = size;
    }

    public List<DataUse> getData_uses() {
        return data_uses;
    }

    public void setData_uses(List<DataUse> data_uses) {
        this.data_uses = data_uses;
    }
}
