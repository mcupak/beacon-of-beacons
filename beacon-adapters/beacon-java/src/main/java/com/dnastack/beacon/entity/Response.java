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
 * Response.
 */
@XmlRootElement(name = "response")
public class Response {

    private Boolean exists;
    private Integer observed;
    private List<Allele> alleles;
    private String info;
    private Error error;

    /*
     * required field(s): exists
     * observed is an Integer with min value 0
     */
    public Response(Boolean exists, Integer observed, List<Allele> alleles, String info, Error error) {
        this.exists = exists;
        this.observed = observed;
        this.alleles = alleles;
        this.info = info;
        this.error = error;
    }

    public Response() {
        // needed for JAXB
    }

    public Boolean getExists() {
        return exists;
    }

    public void setExists(Boolean exists) {
        this.exists = exists;
    }

    public Integer getObserved() {
        return observed;
    }

    public void setObserved(Integer observed) {
        this.observed = observed;
    }

    public List<Allele> getAlleles() {
        return alleles;
    }

    public void setAlleles(List<Allele> alleles) {
        this.alleles = alleles;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
