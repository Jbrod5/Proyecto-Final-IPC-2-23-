
package com.jbrod.joblink_api.app.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jbrod.joblink_api.app.db.job_offers.JobOffer;
import com.jbrod.joblink_api.app.db.job_offers.JobOfferDB;
import com.jbrod.joblink_api.app.exceptions.InvalidInformationException;
import com.jbrod.joblink_api.app.exceptions.NotFoundException;
import com.jbrod.joblink_api.app.services.job_offers.JobOfferService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Controlador de JobOffer - ofertas de empleo.
 * @author Jorge
 */
@WebServlet(name = "JobOfferController", urlPatterns = {"/v1/job-offers"})
public class JobOfferController extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        JobOfferDB jobOfferDB = new JobOfferDB();
        JobOfferService jobOfferService = new JobOfferService(jobOfferDB);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        
        List<JobOffer> offers;
        
        switch(action){
            case "get-all":
                offers = jobOfferService.getAllOffers();
                objectMapper.writeValue(response.getWriter(), offers);
                break; 
                
            case "get-by-id":
                
                try {
                    int idOffer = Integer.parseInt(request.getParameter("id"));
                    JobOffer offer = jobOfferService.getById(idOffer);
                    objectMapper.writeValue(response.getWriter(), offer);
                } catch (NotFoundException ex) {
                    ex.printStackTrace();
                    System.out.println("No se encontro el elemento solicitado dentro de las ofertas de empleo.");
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);   
                }
                break;
                
            case "get-by-category":
                int categoryOffer = Integer.parseInt(request.getParameter("category"));
                offers = jobOfferService.getAllOffersByCategory(categoryOffer);
                objectMapper.writeValue(response.getWriter(), offers);
                break;
                
            case "get-by-employer":
                String employer =request.getParameter("employer");
                offers = jobOfferService.getAllOffersByEmployer(employer);
                objectMapper.writeValue(response.getWriter(), offers);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String action = request.getParameter("action");
        JobOfferDB jobOfferDB = new JobOfferDB();
        JobOfferService jobOfferService = new JobOfferService(jobOfferDB);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        
        switch (action) {
            case "create":                
            
                try {
                    //1. Obtener la oferta desde el request.
                    JobOffer offer = objectMapper.readValue(request.getInputStream(), JobOffer.class);
                    //2. Agregar la oferta
                    jobOfferService.createOffer(offer);    
                } catch (InvalidInformationException ex) {
                    System.out.println("Hubo un error con la informacion contenida dentro de la oferta a crear: InvalidInformationException");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    ex.printStackTrace();
                }
                break;
                
            case "set-finished":
                int idJobOffer = Integer.parseInt(request.getParameter("id"));
                jobOfferService.updateStatusToFinished(idJobOffer);
                break;
            case "setInterview":
                int idOffer = Integer.parseInt(request.getParameter("id"));
                jobOfferService.updateStatusToFinished(idOffer);
                break;
        }
 
    }
    

}
