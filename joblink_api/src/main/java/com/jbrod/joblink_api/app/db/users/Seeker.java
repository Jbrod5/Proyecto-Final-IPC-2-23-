
package com.jbrod.joblink_api.app.db.users;

/**
 * Representa la parte especializada de un solicitante en la base de datos.
 * @author Jorge
 */
public class Seeker {

    private String username;
    private String curriculumPath; 

    public Seeker() {
    
    }
    
    public Seeker(String username, String curriculumPath) {
        this.username = username;
        this.curriculumPath = curriculumPath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCurriculumPath() {
        return curriculumPath;
    }

    public void setCurriculumPath(String curriculumPath) {
        this.curriculumPath = curriculumPath;
    }
    
    
    
    
    
}
