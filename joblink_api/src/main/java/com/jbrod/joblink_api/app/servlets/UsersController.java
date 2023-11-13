
package com.jbrod.joblink_api.app.servlets;

/**
 * Controlador de usuarios -> Servlet.
 * @author Jorge
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jbrod.joblink_api.app.db.users.User;
import com.jbrod.joblink_api.app.db.users.UserDB;
import com.jbrod.joblink_api.app.exceptions.InvalidInformationException;
import com.jbrod.joblink_api.app.exceptions.NotFoundException;
import com.jbrod.joblink_api.app.services.users.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.http.entity.ContentType;


@WebServlet(name = "UsersController", urlPatterns = {"/v1/users"} )
public class UsersController extends HttpServlet{

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //if(request.getParameter(""))
    }
    
    @Override 
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        // -- para pruebas -- //
        System.out.println(request);
        System.out.println(request.getContentType());

        UserDB userdb = new UserDB(); 
        UserService userService = new UserService(userdb);
       
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        
        
        String action = request.getParameter("action");
        switch (action) {
            case "login":
                //1. obtener el objeto User que intenta iniciar sesion -> Este debe tener username y password
                User loginAttemp = objectMapper.readValue(request.getInputStream(),User.class);
                
                //2. obtener el objeto usuario a traves de las credenciales del objeto en paso 1
                User user;
                try {
                    
                    user = userService.getUserByCredentials(loginAttemp);
                    //3. Agregar el usuario al response con object mapper
                    response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
                    objectMapper.writeValue(response.getWriter(), user);
                    
                } catch (InvalidInformationException ex) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    ex.printStackTrace();
                } catch (NotFoundException ex) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    ex.printStackTrace();
                }
                break;

            case "register":
                response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
                User registerAttemp = objectMapper.readValue(request.getInputStream(),User.class);
            
                try {
                    
                    registerAttemp = userService.createUser(registerAttemp);
                    //objectMapper.writeValue(response.getWriter(), registerAttemp);
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
    
}
