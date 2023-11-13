
package com.jbrod.joblink_api.app.services.users;

import com.jbrod.joblink_api.app.db.users.Employer;
import com.jbrod.joblink_api.app.db.users.EmployerDB;
import com.jbrod.joblink_api.app.exceptions.InvalidInformationException;
import com.jbrod.joblink_api.app.exceptions.NotFoundException;
import com.mysql.cj.util.StringUtils;
import java.util.Optional;

/**
 * Clase de servicio Empleador.
 * @author Jorge
 */
public class EmployerService {
    
    private EmployerDB employerdb;

    public EmployerService(EmployerDB employerdb) {
        this.employerdb = employerdb; 
    }
    
    /**
     * Crea una tupla de la especializacion Usiario - Empleador en la base de datos verificando que la informacion esté completa.
     * @param employer: especializacion Empleador con toda la informacion disponible.
     * @throws  InvalidInformationException
     * @return Employer : exactamente el mismo empleador recibido como entrada
     **/
    public Employer crateEmployer(Employer employer) throws InvalidInformationException{
        /* Deben estar llenos los siguientes campos: username, mission, vision, payment method*/
        validateEmployer(employer);
        
        return employerdb.createEmployer(employer);
    }
    
    /**
     * Obtiene un Empleador a base de la informacion ingresada en la base de datos por medio del username.
     * @param username: String con el nombre de usuario a buscar en empleador.
     * @throws InvalidInformationException
     * @throws NotFoundException
     * @return Employer : Employer con la informacion completa 
     **/
    public Employer getEmployerByUsername(String username) throws InvalidInformationException, NotFoundException{
        validateUsernameInEmployer(username);
        
        Optional<Employer> employerOpt = employerdb.getEmployerByUsername(username);
        return employerOpt.orElseThrow(  ()  -> new NotFoundException("Usuario no encontrado.") );
    }
    
    
    
    
    public boolean validateEmployer(Employer employer) throws InvalidInformationException{
        if( StringUtils.isEmptyOrWhitespaceOnly(employer.getMission()) 
                || StringUtils.isEmptyOrWhitespaceOnly(employer.getVision())
                || StringUtils.isEmptyOrWhitespaceOnly(employer.getUsername())
                || employer.getPaymentMethod()  == 0
        ){
            throw new InvalidInformationException("Al menos uno de los campos para crear al empleador no es valido."); 
        } else {
            return true; 
        }
    }
    
    public boolean validateUsernameInEmployer(String username) throws InvalidInformationException{
        if( StringUtils.isEmptyOrWhitespaceOnly( username )){
            throw new InvalidInformationException("El campo username se encuentra vacio."); 
        } else {
            return true; 
        }
    }
    
}
