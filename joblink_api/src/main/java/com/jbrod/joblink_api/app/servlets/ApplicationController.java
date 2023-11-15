
package com.jbrod.joblink_api.app.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jbrod.joblink_api.app.db.job_offers.Application;
import com.jbrod.joblink_api.app.db.job_offers.ApplicationDB;
import com.jbrod.joblink_api.app.exceptions.InvalidInformationException;
import com.jbrod.joblink_api.app.services.job_offers.ApplicationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controlador de Apllication - aplicacion a ofertas de empleo.
 * @author Jorge
 */
@WebServlet(name = "ApplicationController", urlPatterns = {"/v1/application"})
public class ApplicationController extends HttpServlet{

    //Consultar (todas, todas por oferta, una por oferta-solicitante)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        ApplicationDB applicationDB = new ApplicationDB();
        ApplicationService applicationService = new ApplicationService(applicationDB);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        
        List<Application> applications;
        
        switch(action){
            case "get-all": 
                applications = applicationService.getAllApplications();
                objectMapper.writeValue(response.getWriter(), applications);
                break; 
                
            case "get-by-offer":
                int offer = Integer.parseInt(request.getParameter("offer"));
                applications = applicationService.getAllApplicationsByOffer(offer);
                objectMapper.writeValue(response.getWriter(), applications);
                break;
            case "get-specific":
                int specificOffer = Integer.parseInt(request.getParameter("offer"));
                String seeker = request.getParameter("seeker");
                applications = applicationService.getSpecificApplication(specificOffer, seeker);
                objectMapper.writeValue(response.getWriter(), applications);
                break;
        }
    }
    
    //Aplicar (crear)
    //Actualizar estado
    //Agregar reporte

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        ApplicationDB applicationDB = new ApplicationDB();
        ApplicationService applicationService = new ApplicationService(applicationDB);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        
        int specificOffer;
        String seeker; 
        
        switch(action){
            case "create":
                try {
                    //1. Obtener la oferta desde el request.
                    Application application = objectMapper.readValue(request.getInputStream(), Application.class);
                    //2. Agregar la oferta
                    applicationService.createApplication(application);    
                } catch (InvalidInformationException ex) {
                    System.out.println("Hubo un error con la informacion contenida dentro de la aplicacion a crear: InvalidInformationException");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    ex.printStackTrace();
                }
                break;
            case "interview":
                specificOffer = Integer.parseInt(request.getParameter("offer"));
                seeker = request.getParameter("seeker");
                applicationService.updateStatusToInterview(specificOffer, seeker);
                break; 
            case "acepted":
                specificOffer = Integer.parseInt(request.getParameter("offer"));
                seeker = request.getParameter("seeker");
                applicationService.updateStatusToAcepted(specificOffer, seeker);
                break; 
            case "rejected":
                specificOffer = Integer.parseInt(request.getParameter("offer"));
                seeker = request.getParameter("seeker");
                applicationService.updateStatusToRejected(specificOffer, seeker);
                break; 
            case "add-report":
                specificOffer = Integer.parseInt(request.getParameter("offer"));
                seeker = request.getParameter("seeker");
                String report = request.getParameter("report");
                applicationService.addReport(specificOffer, seeker, report);
                break; 
        }
    }
    
}
