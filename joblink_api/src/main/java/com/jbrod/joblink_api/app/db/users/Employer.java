
package com.jbrod.joblink_api.app.db.users;

/**
 * Representa la parte especializada de un empleador en la base de datos.
 * @author Jorge
 */
public class Employer {

    
    private String     username; 
    private String      mission; 
    private String       vision; 
    private int   paymentMethod; 

    public Employer() {
    
    }

    public Employer(String username, String mission, String vision, int paymentMethod) {
        this.username = username;
        this.mission = mission;
        this.vision = vision;
        this.paymentMethod = paymentMethod;
    }

    
    
    
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getVision() {
        return vision;
    }

    public void setVision(String vision) {
        this.vision = vision;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    
    
    
    
    
    
}
