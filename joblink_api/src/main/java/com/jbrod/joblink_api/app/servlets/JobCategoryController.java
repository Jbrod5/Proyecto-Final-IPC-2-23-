
package com.jbrod.joblink_api.app.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jbrod.joblink_api.app.db.job_offers.JobCategory;
import com.jbrod.joblink_api.app.db.job_offers.JobCategoryDB;
import com.jbrod.joblink_api.app.exceptions.InvalidInformationException;
import com.jbrod.joblink_api.app.exceptions.NotFoundException;
import com.jbrod.joblink_api.app.services.job_offers.JobCategoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.apache.http.entity.ContentType;

/**
 * Controlador de JobCategory.
 * @author Jorge
 */
@WebServlet(name = "JobCategoryController", urlPatterns = {"/v1/job-categories"})
public class JobCategoryController extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String action = request.getParameter("action");
        JobCategoryDB jcdb = new JobCategoryDB();
        JobCategoryService jcs = new JobCategoryService(jcdb);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        
        switch (action) {
            case "get-all":
                List<JobCategory> listJobCategories= jcs.getAllCategories();
                objectMapper.writeValue(response.getWriter(), listJobCategories);
                break;
                
            case "get-enabled":
                List<JobCategory> listCategories= jcs.getAllEnabledCategories();
                objectMapper.writeValue(response.getWriter(), listCategories);
                break; 
                
            case "get-by-id":
                
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    JobCategory cat = jcs.getById(id);
                    objectMapper.writeValue(response.getWriter(), cat);
                    
                } catch (NotFoundException ex) {
                    System.out.println("No se encontro el elemento solicitado dentro de categorias ofertas trabajo.");
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    ex.printStackTrace();
                } catch (NumberFormatException ex){
                    System.out.println("El contenido del parametro id dentro del request no es un entero.");
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    ex.printStackTrace();
                }
            
                break;

            default:
                throw new AssertionError();
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    
        try {
            //Acciones: agregar una categoria, habilitar-desabilitar categorias, obtener una categoria, obtener todas las categorias (habilitadas - deshabilitadas)
            JobCategoryDB jobCategoryDB = new JobCategoryDB();
            JobCategoryService categoryService = new JobCategoryService(jobCategoryDB);
            
            ObjectMapper objectMapper = new  ObjectMapper().registerModule(new JavaTimeModule());
            String action = request.getParameter("action");
            
            switch(action){
                case "add-category":
                    //1. Obtener la categoria a agregar desde el request
                    JobCategory newCategory = objectMapper.readValue(request.getInputStream(), JobCategory.class);
                    
                    //2. Agregar la categoria nueva
                    JobCategory newCategoryDB =  categoryService.createJobCategory(newCategory);
                    
                    //3. Responder al con la nueva categoria
                    response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
                    objectMapper.writeValue(response.getWriter(), newCategoryDB);
                    
                    break;
                case "change-status":
                    //1. Obtener el json de la categoria
                    JobCategory change_status = objectMapper.readValue(request.getInputStream(), JobCategory.class);
                    categoryService.changeStatusEnabled(change_status.getId());
                    response.setStatus(HttpServletResponse.SC_ACCEPTED);
                    
                    break;
                default:
                    break;
                    
            }
        } catch (InvalidInformationException ex) {
            System.out.println("Hubo un error con la informacion contenida dentro de un JobCategory: InvalidInformationException");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ex.printStackTrace();
        }
        
    }
}
