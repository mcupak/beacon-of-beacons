package com.dnastack.beacon.entity.resources;

import java.util.List;


/*
 * Data use limitations
 */
public class DataUseResource {

    private String category;
    private String description;
    private List<DataUseRequirementResource> requirements;
	
    public DataUseResource() {
        // needed for JAXB
    }
    
    /*
     * required field(s): category
     */
    public DataUseResource(String category, String description, List<DataUseRequirementResource> requirements) {
    	this.category = category;
		this.description = description;
		this.requirements = requirements;
	} 
   
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<DataUseRequirementResource> getRequirements() {
		return requirements;
	}
	
	public void setRequirements(List<DataUseRequirementResource> requirements) {
		this.requirements = requirements;
	}
    
    
    
}
