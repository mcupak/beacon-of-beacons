package com.dnastack.beacon.entity.resources;

public class ErrorResource {
	
	private String name; 
	private String description;
	
	public ErrorResource() {
        // needed for JAXB
	}

	/*
	 * required field(s): name
	 */
	public ErrorResource(String name, String description) {
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
