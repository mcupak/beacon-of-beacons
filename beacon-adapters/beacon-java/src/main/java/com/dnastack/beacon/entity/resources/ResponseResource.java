package com.dnastack.beacon.entity.resources;

import java.util.List;

public class ResponseResource {
	
	private Boolean exists;
	private Integer observed;
	private List<AlleleResource> alleles;
	private String info;
	private ErrorResource error;
	
	/*
	 * required field(s): exists
	 * observed is an Integer with min value 0
	 */
	public ResponseResource(Boolean exists, Integer observed, List<AlleleResource> alleles, String info, ErrorResource error) {
		this.exists = exists;
		this.observed = observed;
		this.alleles = alleles;
		this.info = info;
		this.error = error;
	}

	public ResponseResource() {
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

	public List<AlleleResource> getAlleles() {
		return alleles;
	}

	public void setAlleles(List<AlleleResource> alleles) {
		this.alleles = alleles;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public ErrorResource getError() {
		return error;
	}

	public void setError(ErrorResource error) {
		this.error = error;
	}
}
