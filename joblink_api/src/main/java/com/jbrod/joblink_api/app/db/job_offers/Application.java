
package com.jbrod.joblink_api.app.db.job_offers;

/**
 * Objeto que representa una aplicacion a un trabajo en la base de datos.
 * @author Jorge
 */
public class Application {
    private int offer; 
    private String seeker; //username
    private String reason; //razon de la aplicacion
    private int status; //1 = seleccion, 2 = entrevistable, 3 = rechazado, 4 = aceptado
    private String report; 

    public Application() {
    }

    public Application(int offer, String seeker, String reason, int status, String report) {
        this.offer = offer;
        this.seeker = seeker;
        this.reason = reason;
        this.status = status;
        this.report = report;
    }
    
    
    
    
    
    

    public int getOffer() {
        return offer;
    }

    public void setOffer(int offer) {
        this.offer = offer;
    }

    public String getSeeker() {
        return seeker;
    }

    public void setSeeker(String seeker) {
        this.seeker = seeker;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
    
    
    
    
}
