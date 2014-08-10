/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dnastack.beacon.core;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@XmlRootElement
public class Query implements Serializable {

    private static final long serialVersionUID = 3L;

    String genome = null;
    String chromosome = null;
    Long position = 0L;
    String allele = null;

    public Query() {
    }

    public Query(String genome, String chromosome, long position, String allele) {
        this.genome = genome;
        this.chromosome = chromosome;
        this.position = position;
        this.allele = allele;
    }

    public String getGenome() {
        return genome;
    }

    public void setGenome(String genome) {
        this.genome = genome;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getAllele() {
        return allele;
    }

    public void setAllele(String allele) {
        this.allele = allele;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (this.genome != null ? this.genome.hashCode() : 0);
        hash = 13 * hash + (this.chromosome != null ? this.chromosome.hashCode() : 0);
        hash = 13 * hash + (int) (this.position ^ (this.position >>> 32));
        hash = 13 * hash + (this.allele != null ? this.allele.hashCode() : 0);
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
        final Query other = (Query) obj;
        if ((this.genome == null) ? (other.genome != null) : !this.genome.equals(other.genome)) {
            return false;
        }
        if ((this.chromosome == null) ? (other.chromosome != null) : !this.chromosome.equals(other.chromosome)) {
            return false;
        }
        if (this.position != other.position) {
            return false;
        }
        if ((this.allele == null) ? (other.allele != null) : !this.allele.equals(other.allele)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Query{" + "genome=" + genome + ", chromosome=" + chromosome + ", position=" + position + ", allele=" + allele + '}';
    }

}
