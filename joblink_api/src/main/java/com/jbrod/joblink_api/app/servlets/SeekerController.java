
package com.jbrod.joblink_api.app.servlets;

import com.jbrod.joblink_api.app.db.users.Seeker;
import com.jbrod.joblink_api.app.db.users.SeekerDB;
import com.jbrod.joblink_api.app.db.users.UserDB;
import com.jbrod.joblink_api.app.exceptions.InvalidInformationException;
import com.jbrod.joblink_api.app.exceptions.NotFoundException;
import com.jbrod.joblink_api.app.services.users.SeekerService;
import com.jbrod.joblink_api.app.services.users.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;

/**
 * Controlador de Solicitante -> Servlet.
 * Un usuario solicitante deberia poder completar su informacion y obtenerla
 * @author Jorge
 */
@WebServlet (name = "SeekerController" , urlPatterns = {"/v1/seekers"})
@MultipartConfig(location = "C:/Users/Jorge/AppData/Local/Temp")
public class SeekerController extends HttpServlet{

    private static final String PDF_PATH = "C:/Users/Jorge/OneDrive/Documents"; 
    private static final String PART_NAME = "datafile";
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws  ServletException, IOException{
        
        SeekerDB seekerDB = new SeekerDB();
        SeekerService seekerService = new  SeekerService(seekerDB);
        
        try {
            
            String action = request.getParameter("action");
            
            switch (action){
                case "save-curriculum":
                    String username = request.getParameter("user");
                    Part filePart = request.getPart(PART_NAME);
                    String fileName = filePart.getContentType();
                    InputStream fileStream = filePart.getInputStream();
                    
                    String finalPath = PDF_PATH + "/" + username + "."+ FilenameUtils.getExtension(filePart.getSubmittedFileName());
                    filePart.write(finalPath);
                    
                    //Actualizar el perfil a completado
                    UserDB userDB = new UserDB();
                    UserService userService = new UserService(userDB);
                    userService.setUserCompleted(username);
                    
                    //Actualizar la especializacion Seeker
                    Seeker seeker = new Seeker(username, finalPath);
                    System.out.println(username + " , " + finalPath);
                    seekerService.createSeeker(seeker);
                    
                    
                    
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    break;
                    
                case "get-seeker":
                    String user = request.getParameter("user");
                    Seeker get = seekerService.getSeekerByUsername(user);
                    
                    File curriculum = new File(get.getCurriculumPath());
                    FileInputStream inputStream = new FileInputStream(curriculum);
                    
                    response.setContentType("application/pdf");
                    response.setHeader("Content-disposition", "attachement; filename = " + get.getCurriculumPath());
                    response.getOutputStream().write(inputStream.readAllBytes());
                    inputStream.close();
                    
                    break;
                default: 
                    break;
            }
            
        } catch (InvalidInformationException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ex.printStackTrace();
        } catch (NotFoundException ex){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            ex.printStackTrace();
        }
        
        
    }
    
    
}
