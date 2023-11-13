
package com.jbrod.joblink_api.app.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jbrod.joblink_api.app.db.users.Employer;
import com.jbrod.joblink_api.app.db.users.EmployerDB;
import com.jbrod.joblink_api.app.db.users.UserDB;
import com.jbrod.joblink_api.app.exceptions.InvalidInformationException;
import com.jbrod.joblink_api.app.exceptions.NotFoundException;
import com.jbrod.joblink_api.app.services.users.EmployerService;
import com.jbrod.joblink_api.app.services.users.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controlador de Empleador -> Servlet.
 * //Un usuario empleador deberia poder completar su informacion y obtenerla.
 * @author Jorge
 */
@WebServlet (name = "EmployerControleer", urlPatterns = {"/v1/employers"})
public class EmployerController extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        
        EmployerDB employerdb = new EmployerDB();
        EmployerService employerService = new EmployerService(employerdb);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        
        String action = request.getParameter("action");
        if(action != null){
            switch (action) {
                case "finish-employer-registration":
                    //1. Obtener el objeto especializado Employer con el que se completara la informacion - debe tener: username, mission, vision, paymentMethod
                    Employer completeRegister = objectMapper.readValue(request.getInputStream(), Employer.class);
                    String username = completeRegister.getUsername();
                    try {

                        //2. Guardarlo en la base de datos
                        completeRegister = employerService.crateEmployer(completeRegister);
                        
                        //3. Actualizar al empleador como "perfil completado" en la base de datos
                        UserDB userDB = new UserDB(); 
                        UserService userService = new UserService(userDB);
                        userService.setUserCompleted(username);
                        
                        //objectMapper.writeValue(response.getWriter(), completeRegister);
                        response.setStatus(HttpServletResponse.SC_CREATED);

                    } catch (InvalidInformationException ex) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        ex.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
        }
        

        //OBTENER EMPLEADOR POR NOMBRE DE USUARIO -------------------------------
        String employerUsername = request.getParameter("get-employer");
        if(employerUsername != null){
            try {
                
                //Obtener el empleador por el username
                Employer responseEmployer = employerService.getEmployerByUsername(employerUsername);
                objectMapper.writeValue(response.getWriter(), responseEmployer);
                response.setStatus(HttpServletResponse.SC_FOUND);
                
            } catch (InvalidInformationException ex) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } catch (NotFoundException ex) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }
    
}
