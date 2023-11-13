
package com.jbrod.joblink_api.app.services.users;

import com.jbrod.joblink_api.app.db.users.User;
import com.jbrod.joblink_api.app.db.users.UserDB;
import com.jbrod.joblink_api.app.exceptions.InvalidInformationException;
import com.jbrod.joblink_api.app.exceptions.NotFoundException;
import com.mysql.cj.util.StringUtils;
import java.util.Optional;

/**
 * Clase de servicio Usuario.
 * @author Jorge
 */
public class UserService {

    private UserDB userdb; 

    public UserService(UserDB userdb) {
        this.userdb = userdb;
    }
    
    
    /**
     * Crea una tupla de usuario en la base de datos verificando que la informacion este completa. 
     * @param user: usuario con toda la informacion disponible. 
     * @throws InvalidInformationException : excepcion lanzada debido a campos invalidos.
     * @return User : Exactamente el mismo usuario que se ingreso como parametro.  
     **/
    public User createUser(User user) throws InvalidInformationException /* Agregar alguna excepcion */ {
        
        /*  Deben estar llenos los campos: 
            username, name, pass, address, email, cui, birthdate, type */
        validateCompleteUserInformation(user);
        
        return userdb.createUser(user);
    }
    
    
    /**
     * Obtiene un usuario mediante un objeto User con sus credenciales (username, password).
     * @param user: usuario con las credenciales (username, password).
     * @throws InvalidInformationException  
     * @throws NotFoundException
     * @return User: un usuario con: username, type y profileCompleted.
     **/
    public User getUserByCredentials(User user) throws InvalidInformationException, NotFoundException{
        validateCredentials(user);
        
        Optional<User> userOpt = userdb.getUserByCredentials(user);
        return userOpt.orElseThrow(  ()  -> new NotFoundException("Usuario no encontrado.") );
    }
    
    /**
     * Marca un usuario como completado en la base de datos usando su username.
     * @param username : String con el username del usuario a marcar como perfil completado.
     * @return boolean : indicando si logró actualizar o no al usuario.
     **/
    public boolean setUserCompleted(String username){
        if(username != null){
            userdb.updateCompletionStatus(username);
            return true; 
        }
        return false; 
    }
    
    
    
    /**
     * Valida la informacion completa de un usuario (usado al crear un usuario).
     * @param user: usuario que debe contener toda la informacion para su creacion.
     * @throws InvalidInformationException 
     * @return boolean.
     **/
    public boolean validateCompleteUserInformation(User user) throws InvalidInformationException{
        if(      ! validateCredentials(user)
                || StringUtils.isEmptyOrWhitespaceOnly(user.getName     () )
                || StringUtils.isEmptyOrWhitespaceOnly(user.getAddress  () )
                || StringUtils.isEmptyOrWhitespaceOnly(user.getEmail    () )
                || user.getCui() == 0
                || user.getBirthDate() == null
                || (user.getType() < 2 || user.getType() > 3 ) // Un usuario registrado solo puede ser: 2 - Empleador, o 3 Solicitante
        ){ 
            throw new InvalidInformationException("Al menos uno de los campos para crear al usuario no es valido."); 
        }else{
            return true; 
        }   
    }
    
    /**
     * Valida las credenciales del usuario (username y password).
     * @param user: usuario con username y password.
     * @throws InvalidInformationException 
     * @return boolean.
     **/
    public boolean validateCredentials(User user) throws InvalidInformationException{
        if(    StringUtils.isEmptyOrWhitespaceOnly( user.getUsername() ) 
            || StringUtils.isEmptyOrWhitespaceOnly(user.getPassword () )){
            throw new InvalidInformationException("Al menos una de las credenciales no es valida.");
        }else{
            return true; 
        }
    }
}
