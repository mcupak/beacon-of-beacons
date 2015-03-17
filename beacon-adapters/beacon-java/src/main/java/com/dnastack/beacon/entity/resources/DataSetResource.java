package com.dnastack.beacon.entity.resources;

import java.util.List;

public class DataSetResource {

    private String id;
    private String description;
	private String reference;
    private DataSizeResource size;
    private List<DataUseResource> data_uses;
    
    public DataSetResource() {
        // needed for JAXB
    }
    
    /*
     * required field(s): id 
     */
    public DataSetResource(String id, String description, String reference, DataSizeResource size, List<DataUseResource> data_uses) {
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
	
	public DataSizeResource getSize() {
		return size;
	}
	
	public void setSize(DataSizeResource size) {
		this.size = size;
	}
	
	public List<DataUseResource> getData_uses() {
		return data_uses;
	}
	
	public void setData_uses(List<DataUseResource> data_uses) {
		this.data_uses = data_uses;
	}
}
