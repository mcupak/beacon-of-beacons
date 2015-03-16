package com.dnastack.beacon.entity.resources;

/*
 * Dimensions of the data set (required if the beacon reports allele frequencies)
 */
public class DataSizeResource {
   
	private Integer variants;
    private Integer samples;
	
    public DataSizeResource() {
        // needed for JAXB
	}
    
	/*
	 * required field(s): variants
	 */
    public DataSizeResource(Integer variants, Integer samples) {
		this.variants = variants;
		this.samples = samples;
	}
	
	public Integer getVariants() {
		return variants;
	}
	
	public void setVariants(Integer variants) {
		this.variants = variants;
	}
	
	public Integer getSamples() {
		return samples;
	}
	
	public void setSamples(Integer samples) {
		this.samples = samples;
	}

    
    
}
