package com.dnastack.beacon.entity.resources;

public class DataUseRequirementResource {
	
	private String name;
	private String description;
	
	public DataUseRequirementResource() {
        // needed for JAXB
	}
	
	
	/*
	 * required field(s): name
	 */
	public DataUseRequirementResource(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
}
