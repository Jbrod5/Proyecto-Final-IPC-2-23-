
package com.jbrod.joblink_api.app.services.job_offers;

import com.jbrod.joblink_api.app.db.job_offers.Application;
import com.jbrod.joblink_api.app.db.job_offers.ApplicationDB;
import com.jbrod.joblink_api.app.exceptions.InvalidInformationException;
import java.util.List;

/**
 * Servicio de manejo de Aplicacions de trabajo usando ApplicationDB.
 * @author Jorge
 */
public class ApplicationService {

    private ApplicationDB applicationDB;

    public ApplicationService(ApplicationDB applicationDB) {
        this.applicationDB = applicationDB;
    }

    //Aplicar (crear)
    //Consultar (todas, todas por oferta, una por oferta-solicitante)
    //Actualizar estado
    //Agregar reporte
    
    public Application createApplication (Application app) throws InvalidInformationException{
        app.setStatus(1);
        if(validateApplication(app)){
            return applicationDB.createApplication(app);
        }
        throw new InvalidInformationException("El objeto Application que se intenta ingresar no cuenta con los elementos necesarios para ser almancenado.");
    }

    private boolean validateApplication(Application app){
        return (
                   app.getOffer() != 0
                && app.getReason() != null
                && app.getSeeker() != null
        );
    }
    
    
    
    public List<Application> getAllApplications(){
        return applicationDB.getAllApplications();
    }
    
    public List<Application> getAllApplicationsByOffer(int offer){
        return applicationDB.getAllApplicationsByOffer(offer);
    }
    
    public List<Application> getSpecificApplication(int offer, String seeker){
        return applicationDB.getSpecificApplication(offer, seeker);
    }

    
    
    
    //1 = seleccion, 2 = entrevistable, 3 = rechazado, 4 = aceptado
    public boolean updateStatusToInterview(int offer, String seeker){
        int interviewStatus = 2;
        applicationDB.updateStatus(offer, seeker, interviewStatus);
        return true; 
    }
    
    public boolean updateStatusToRejected(int offer, String seeker){
        int rejectedStatus = 3;
        applicationDB.updateStatus(offer, seeker, rejectedStatus);
        return true; 
    }
    
    public boolean updateStatusToAcepted(int offer, String seeker){
        int aceptedStatus = 4;
        applicationDB.updateStatus(offer, seeker, aceptedStatus);
        return true; 
    }
    
    public boolean addReport(int offer, String seeker, String report){
        applicationDB.updateReport(offer, seeker, report);
        return true; 
    }

}
