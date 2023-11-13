
package com.jbrod.joblink_api.app.db.users;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Controlador de usuario comun en la base de datos.
 * @author Jorge
 */
public class UserDB {

    private static final String URL_MYSQL = "jdbc:mysql://localhost:3306/joblink";
    private static final String USER = "admin"; 
    private static final String PASSWORD = "admin";
    
    private static final String INSERT = "INSERT INTO usuario (username, nombre, password, direccion, correo, cui, fecha_nacimiento, tipo, perfil_completado) VALUES (?,?,?,?,?,?,?,?,?)";
    private static final String SELECT_BY_CREDENTIALS = "SELECT * FROM  usuario WHERE username = ? AND password = ?";
    private static final String SELECT_BY_USERNAME = "SELECT * FROM usuario WHERE username = ?";
    private static final String UPDATE_PROFILE_COMPLETED = "UPDATE usuario SET perfil_completado = 1 WHERE username = ?";
    
    private Connection connection; 
    
    
    public UserDB(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL_MYSQL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * Crea una tupla de USUARIO en la base de datos, mas NO su especializacion.
     * @param user: objeto usuario con: username, name, passw, address, email, cui, fecha nac, tipo
     * @return User: el mismo objeto user si se almacenó en la base de datos correctamente.
     **/
    public User createUser(User user){
        
        try {
            PreparedStatement insert = connection.prepareStatement(INSERT);
            //Insert necesita los siguientes parametros: username, nombre, password, direccion, correo, cui, fecha_nacimiento, tipo
            insert.setString(1, user.getUsername    ()                        );
            insert.setString(2, user.getName        ()                        );
            insert.setString(3, user.getPassword    ()                        );
            insert.setString(4, user.getAddress     ()                        );
            insert.setString(5, user.getEmail       ()                        );
            
            insert.setString(6, String.valueOf      (user.getCui()        ) );
            insert.setDate  (7, Date  .valueOf      (user.getBirthDate() ) );
            insert.setString(8, String.valueOf      (user.getType()       ) );
            insert.setInt   (9,  user.isProfileCompleted () ? 1 : 0             );
            
            insert.executeUpdate();
            System.out.println("Usuario almacenado correctamente en la base de datos.");
            return user;
            
        } catch (SQLException ex) {
            System.out.println("Hubo un error al insertar en la base de datos.");
            ex.printStackTrace();
        }
        return null; 
    }
    
    
    /**
     * Devuelve un objeto Optional - User con informacion basica del usuario al encontrar una tupla en la base de datos con sus credenciales (inicio de sesion).
     * @param user: usuario UNICAMENTE con username y password.
     * @return Optional - User.
     **/
    public Optional<User> getUserByCredentials(User user){
        
        try {
            PreparedStatement select = connection.prepareStatement(SELECT_BY_CREDENTIALS);
            select.setString(1, user.getUsername());
            select.setString(2, user.getPassword());
            
            ResultSet resultset = select.executeQuery();
            
            if(resultset.next()){
                return Optional.of(
                    new User(
                        resultset.getString("username"),
                        resultset.getInt("tipo"),
                        resultset.getInt("perfil_completado") != 0
                    )
                );
            }
            
            return Optional.empty();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return Optional.empty(); 
    }
    
    
    
    
    
    
    
    //TODO Actualizar el estado "perfil completado" de un perfil 
    /**
     * Actualiza un perfil como COMPLETADO (1: TinyInt) indicando que la informacion requerida en su especializacion ha sido almacenada.
     * @param username: String con el nombre de usuario que ha completado el registro.
     * @return boolean: true si ha realizado la actualización correctamente, false si no. 
     **/
    public boolean updateCompletionStatus(String username){
    
        try {
            PreparedStatement update = connection.prepareStatement(UPDATE_PROFILE_COMPLETED);
            // Update requiere la siguiente informacion: username = ?
            update.setString(1, username);
            
            update.executeUpdate();
            return true; 
            
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        
        return false;
    }
    
}
