package com.dnastack.beacon.entity.resources;

public class QueryResource {
	
   	    private String allele;
   	    private String chromosome;
   	    private Integer position;
   	    private String reference;
        private String dataset_id;
        
        /*
         * required field(s): allele, chromosome, position, reference
         */
		public QueryResource(String allele, String chromosome, Integer position, String reference, String dataset_id) {
			this.allele = allele;
			this.chromosome = chromosome;
			this.position = position;
			this.reference = reference;
			this.dataset_id = dataset_id;
		}
		
		public QueryResource() {
	        // needed for JAXB
		}
		
		public String getAllele() {
			return allele;
		}
		
		public void setAllele(String allele) {
			this.allele = allele;
		}
		
		public String getChromosome() {
			return chromosome;
		}
		
		public void setChromosome(String chromosome) {
			this.chromosome = chromosome;
		}
		
		public Integer getPosition() {
			return position;
		}
		
		public void setPosition(Integer position) {
			this.position = position;
		}
		
		public String getReference() {
			return reference;
		}
		
		public void setReference(String reference) {
			this.reference = reference;
		}
		
		public String getDataset_id() {
			return dataset_id;
		}
		
		public void setDataset_id(String dataset_id) {
			this.dataset_id = dataset_id;
		}
}
