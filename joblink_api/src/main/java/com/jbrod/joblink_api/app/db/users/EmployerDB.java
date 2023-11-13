
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
public class EmployerDB {

    private static final String URL_MYSQL = "jdbc:mysql://localhost:3306/joblink";
    private static final String USER = "admin"; 
    private static final String PASSWORD = "admin";
    
    private static final String INSERT = "INSERT INTO empleador (username, vision, mision, metodo_pago) VALUES (?, ?, ? , ?)";
    private static final String SELECT_BY_USERNAME = "SELECT * FROM empleador WHERE username = ?";
    
    private Connection connection; 
    
    public EmployerDB(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL_MYSQL, USER, PASSWORD); 
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }catch(SQLException ex){
            ex.printStackTrace(); 
        }
        
    }
    
    /**
     * Crea una tupla de la especializacion de EMPLEADOR en la base de datos, mas NO el usuario completo (en ambas tablas).
     * @param employer: objeto con la especializacion empleador, con: username, mission, vision y paymentMethdod.
     * @return Emoloyer: el mismo objeto si se almaceno en la base de datos correctamente. 
     **/
    public Employer createEmployer(Employer employer){
        
        try {
            PreparedStatement insert = connection.prepareStatement(INSERT);
            // Insert requiere los siguientes parametros: username, vision, mision, metodo_pago
            insert.setString(1, employer.getUsername      () );
            insert.setString(2, employer.getVision        () );
            insert.setString(3, employer.getMission       () );
            insert.setInt   (4, employer.getPaymentMethod () );
            
            insert.executeUpdate();
            return employer;
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null; 
    }
    
    //TODO obtener empleador por username
    /**
     * Devuelve un objeto Optional - Employer con la informacion especializada de un usuario empleador almacenada en la base de datos.
     * @param username: nombre de usuario a buscar en la base de datos.
     * @return Optional - Employer.
     **/
    public Optional<Employer> getEmployerByUsername(String username){
        
        try {
            PreparedStatement select = connection.prepareStatement(SELECT_BY_USERNAME);
            // Select requiere de la siguiente informacion: username = ?
            select.setString(1, username);
            
            ResultSet resultSet = select.executeQuery();
            
            if(resultSet.next()){
                return Optional.of(
                    new Employer(
                        resultSet.getString("username"),
                        resultSet.getString("mision"),
                        resultSet.getString("vision"),
                        //Por seguridad, no incluir el metodo de pago 
                        0
                    ) 
                );
            }
            
            return Optional.empty();
            
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        return Optional.empty(); 
    }
    
}
