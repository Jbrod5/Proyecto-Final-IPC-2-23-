
package com.jbrod.joblink_api.app.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jbrod.joblink_api.app.db.job_offers.EmploymentMode;
import com.jbrod.joblink_api.app.db.job_offers.EmploymentModeDB;
import com.jbrod.joblink_api.app.exceptions.NotFoundException;
import com.jbrod.joblink_api.app.services.job_offers.EmploymentModeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.entity.ContentType;

/**
 * Controlador de EmploymentMode.
 * @author Jorge
 */
@WebServlet(name = "EmploymentModeController", urlPatterns = {"/v1/job-modalities"})
public class EmploymentModeController extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String action = request.getParameter("action");
        //Acciones post: obtener por id y string, obtener todas la modalidades   
            EmploymentModeDB employmentModeDB = new EmploymentModeDB();
            EmploymentModeService employmentModeService = new EmploymentModeService(employmentModeDB);
            
            ObjectMapper objectMapper = new  ObjectMapper().registerModule(new JavaTimeModule());
            
            switch("action"){
                case "get-all": 
                    List<EmploymentMode> listEmploymentModes = employmentModeDB.getAllModalities();
                    objectMapper.writeValue(response.getWriter(), listEmploymentModes);
                    break; 
                case "get-by-id":            
                    try {
                        int id = Integer.parseInt(request.getParameter("id"));
                        EmploymentMode mode = employmentModeService.getById(id);
                        objectMapper.writeValue(response.getWriter(), mode);
                    } catch (NotFoundException ex) {
                        System.out.println("No se encontro el elemento solicitado dentro de las modalidades de empleo.");
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        ex.printStackTrace();
                    }
                    break; 
                case "get-by-string":            
                    try {
                        String cad = request.getParameter("string");
                        EmploymentMode mode = employmentModeService.getByModalityString(cad);
                        objectMapper.writeValue(response.getWriter(), mode);
                    } catch (NotFoundException ex) {
                        System.out.println("No se encontro el elemento solicitado dentro de las modalidades de empleo.");
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        ex.printStackTrace();
                    }
                    break;
            }
    }
    
    
    @Override 
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try {
            //Acciones post: crear una modalidad 
            EmploymentModeDB employmentModeDB = new EmploymentModeDB();
            EmploymentModeService employmentModeService = new EmploymentModeService(employmentModeDB);
            
            ObjectMapper objectMapper = new  ObjectMapper().registerModule(new JavaTimeModule());
            String action = request.getParameter("action");
            
            switch (action) {
                case "create-modality":
                    //1. Obtener la modalidad a agregar desde el request
                    EmploymentMode mode = objectMapper.readValue(request.getInputStream(), EmploymentMode.class);
                    //2. Agregar la modalidad nueva
                    EmploymentMode newMode =  employmentModeDB.createCategory(mode);
                    //3. Responder con la nueva modalidad
                    response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
                    objectMapper.writeValue(response.getWriter(), newMode);
                    break;
                default:
                    break; 
            }
            
        } catch (IOException e) {
        }
    }
    
}
