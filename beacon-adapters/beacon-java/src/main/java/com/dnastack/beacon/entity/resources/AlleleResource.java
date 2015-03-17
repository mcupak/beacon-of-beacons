package com.dnastack.beacon.entity.resources;

public class AlleleResource {
	
	private String allele;
	private Double frequency;
	
	public AlleleResource() {
		// needed for JAXB
	}
	
	/* 
	 * Required field(s): allele  
	 * frequency is a double between 0 & 1
	*/
	public AlleleResource(String allele, Double frequency) {
		this.allele = allele;
		this.frequency = frequency;
	}

	public String getAllele() {
		return allele;
	}

	public void setAllele(String allele) {
		this.allele = allele;
	}

	public Double getFrequency() {
		return frequency;
	}

	public void setFrequency(Double frequency) {
		this.frequency = frequency;
	}
}
