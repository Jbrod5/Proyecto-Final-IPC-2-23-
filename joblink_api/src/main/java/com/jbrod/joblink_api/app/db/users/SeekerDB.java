
package com.jbrod.joblink_api.app.db.users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 *
 * @author Jorge
 */
public class SeekerDB {
    private static final String URL_MYSQL = "jdbc:mysql://localhost:3306/joblink";
    private static final String USER = "admin"; 
    private static final String PASSWORD = "admin";
    
    private static final String INSERT = "INSERT INTO solicitante (username, curriculum) VALUES (?, ?)";
    private static final String SELECT_BY_USERNAME = "SELECT * FROM solicitante WHERE username = ?";
    
    private Connection connection; 

    public SeekerDB() {
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
     * Crea una tupla de la especializacion de SOLICITANTE en la base de datos, NO el usuario completo en ambas tablas.
     * @param seeker: objeto con la especializacion solicitante, con: username, curriculumpath.
     * @return Seeker: el mismo objeto si se almaceno en la base de datos correctamente. 
     **/
    public Seeker createSeeker(Seeker seeker){
    
        try {
            
            PreparedStatement insert = connection.prepareStatement(INSERT);
            //Insert requiere de la siguiente informacion: username, curriculum
            insert.setString(1, seeker.getUsername());
            insert.setString(2, seeker.getCurriculumPath());
            
            insert.executeUpdate();
            return seeker;
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null; 
    }
    
    //TODO obtener seeker por username
    /**
     * Devuelve un objeto Optional - Seeker con la informacion especializada de un usuario solicitante en la base de datos.
     * @param username: nombre de usuario a buscar en la base de datos.
     * @return Optional - Seeker.
     **/
    public Optional<Seeker> getSeekerByUsername(String username){
        
        try {
            PreparedStatement select = connection.prepareStatement(SELECT_BY_USERNAME);
            //Select requiere la siguiente información: username = ?
            select.setString(1, username);
            
            ResultSet resultSet = select.executeQuery();
            
            if(resultSet.next()){
                return Optional.of(
                    new Seeker(resultSet.getString("username") , 
                            resultSet.getString("curriculum"))
                );
            }
            
            return Optional.empty(); 
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return Optional.empty();
    }
    
}
