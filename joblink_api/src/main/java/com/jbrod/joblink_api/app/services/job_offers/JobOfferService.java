
package com.jbrod.joblink_api.app.services.job_offers;

import com.jbrod.joblink_api.app.db.job_offers.JobOffer;
import com.jbrod.joblink_api.app.db.job_offers.JobOfferDB;
import com.jbrod.joblink_api.app.exceptions.InvalidInformationException;
import com.jbrod.joblink_api.app.exceptions.NotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de manejo de Ofertas de empleo usando JobOfferDB.
 * @author Jorge
 */
public class JobOfferService {

    private JobOfferDB jobOfferDB;
    
    public JobOfferService(JobOfferDB jobOfferDB) {
        this.jobOfferDB = jobOfferDB;
    }
    
    /* Acciones que puede realizar: 
       createOffer, selectOfferById, getAllOffers, getAllByCategory, 
       getAllOffersByEmployer, 
       updateStatus -- 1 Seleccion, 2 Activa, 3 Finalizada, 4 - Entrevista */

    
    public JobOffer createOffer(JobOffer offer) throws InvalidInformationException{
        offer.setPublicationDate(LocalDate.now());
        int statusEnabled = 1;
        offer.setStatus(statusEnabled);
        if(validateOffer(offer)){ 
            return jobOfferDB.createOffer(offer);
        }
        throw new InvalidInformationException("El objeto JobOffer que se intenta ingresar no cuenta con los elementos necesarios para ser almancenado.");
    }
    
    private boolean validateOffer(JobOffer offer){
        return (   offer.getName() != null
                && offer.getDescription() != null
                && offer.getCategory() != 0
                && offer.getApplicationDeadline() != null
                && offer.getApproximateSalary() != 0
                && offer.getModality() != 0
                && offer.getLocation() != null
                && offer.getDetails() != null
                && offer.getEmployer() != null
        );
    }
    
    
    
    
    public JobOffer getById(int id) throws NotFoundException{
        Optional<JobOffer> jobOffer = jobOfferDB.selectJobOfferById(id);
        return jobOffer.orElseThrow( () -> new NotFoundException("No se encontro una oferta con el id indicado.") );
    }
    
    public List<JobOffer> getAllOffers(){
        return jobOfferDB.getAllOffers();
    }
    
    public List<JobOffer> getAllOffersByCategory(int category){
        return jobOfferDB.getAllOffersByCategory(category);
    }
    
    public List<JobOffer> getAllOffersByEmployer(String employer){
        return jobOfferDB.getAllOffersByEmployer(employer);
    }
    
    public boolean updateStatusToFinished(int id){
        //Finished = 3
        int finished = 3;
        jobOfferDB.updateStatus(id, finished);
        return true; 
    }
    
    public boolean updateStatusToInterview(int id){
        int interview = 4;
        jobOfferDB.updateStatus(id, interview);
        return true; 
    }
}
