
package com.jbrod.joblink_api.app.services.users;

import com.jbrod.joblink_api.app.db.users.Seeker;
import com.jbrod.joblink_api.app.db.users.SeekerDB;
import com.jbrod.joblink_api.app.exceptions.InvalidInformationException;
import com.jbrod.joblink_api.app.exceptions.NotFoundException;
import com.mysql.cj.util.StringUtils;
import java.util.Optional;

/**
 *
 * @author Jorge
 */
public class SeekerService {
    
    private SeekerDB seekerdb; 
    
    public SeekerService(SeekerDB seekerdb){
        this.seekerdb = seekerdb; 
    }
    
    /**
     * Crea una tupla de la especializacion Usuario - Solicitante en la base de datos.
     * @param seeker: especializacion Seeker con toda la informacion disponible (username, curriculum path ).
     * @throws InvalidInformationException
     * @return Seeker : exactamente el mismo seeker recibido como entrada
     **/
    public Seeker createSeeker(Seeker seeker) throws InvalidInformationException{
        // Deben estar llenos los siguientes campos: username, curriculum
        validateSeeker(seeker);
        
        return seekerdb.createSeeker(seeker);
    }
    
    /**
     * Obtiene un Solicitante a base de la informacion ingresada en la base de datos por medio del username.
     * @param username: String con el nombre de usuario a buscar en solicitante.
     * @throws InvalidInformationException
     * @throws NotFoundException
     * @return Seeker : Seeker con la informacion completa 
     **/
    public Seeker getSeekerByUsername(String username) throws InvalidInformationException, NotFoundException{
        validateUsername(username);
        
        Optional<Seeker> seekeropt = seekerdb.getSeekerByUsername(username);
        return seekeropt.orElseThrow(() -> new NotFoundException ("Usuario no encontrado.") );
    }
    
    
    
    
    public boolean validateSeeker(Seeker seeker) throws InvalidInformationException{
        if(       StringUtils.isEmptyOrWhitespaceOnly(seeker.getUsername      ())
                ||StringUtils.isEmptyOrWhitespaceOnly(seeker.getCurriculumPath())
        ){
            throw new InvalidInformationException("Al menos uno de los campos dentro de seeker es invalido.");
        }else{
            return true; 
        }
    }
        
    public boolean validateUsername(String username) throws InvalidInformationException{
        if( StringUtils.isEmptyOrWhitespaceOnly(username)){
            throw new InvalidInformationException("El campo username no es valido.");
        }else{
            return true; 
        }
    }
    
}
