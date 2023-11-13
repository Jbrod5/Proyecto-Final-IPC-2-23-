
package com.jbrod.joblink_api.app.db.job_offers;

/**
 * Objeto que representa una categoria de oferta de empleo en la base de datos.
 * @author Jorge
 */
public class JobCategory {

    private int id; 
    private String category;
    private boolean enabled; 

    
    
    public JobCategory() {
    }
    
    public JobCategory(int id, String category, boolean enabled) {
        this.id = id;
        this.category = category;
        this.enabled = enabled;
    }

    public JobCategory(String category) {
        this.category = category;
    }
    
    
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
