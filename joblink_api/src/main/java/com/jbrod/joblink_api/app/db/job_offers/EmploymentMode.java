
package com.jbrod.joblink_api.app.db.job_offers;

/**
 * Este objeto representa una modality de empleo en la base de datos.
 * @author Jorge
 */
public class EmploymentMode {
    private int id; 
    private String modality; 

    public EmploymentMode() {
    }

    public EmploymentMode(int id, String modality) {
        this.id = id;
        this.modality = modality;
    }

    public EmploymentMode(String modality) {
        this.modality = modality;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }
    
    
    
    
    
}
