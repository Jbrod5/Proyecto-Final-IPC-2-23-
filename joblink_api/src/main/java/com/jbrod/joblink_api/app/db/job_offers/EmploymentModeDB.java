
package com.jbrod.joblink_api.app.db.job_offers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manejo de modalidades de empleo en la base de datos. 
 * @author Jorge
 */
public class EmploymentModeDB {

    private static final String URL_MYSQL = "jdbc:mysql://localhost:3306/joblink";
    private static final String USER = "admin"; 
    private static final String PASSWORD = "admin";
    
    //Una modalidad debe poder agregarse, consultar por id y por string, y poder consultar el listado de todas las categorias
    private static final String INSERT = "INSERT INTO modalidadempleo (modalidad) VALUES (?)";
    private static final String SELECT_BY_ID = "SELECT * FROM modalidadempleo WHERE id = ?";
    private static final String SELECT_BY_MODALITYSTRING = "SELECT * FROM modalidadempleo WHERE modalidad = ?";
    private static final String SELECT_ALL = "SELECT * FROM modalidadempleo";
    
    private Connection connection; 

    public EmploymentModeDB() {
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
     * Crear una tupla de modalidad empleo en la base de datos.
     * @param employmentMode : objeto de tipo Modalidad de empleo con la modalidad a agregar. Debe incluir al menos el string modality.
     * @return EmploymentMode: el nuevo objeto creado y almacenado en la base de datos.
     **/
    public EmploymentMode createCategory(EmploymentMode employmentMode){
        
        try {
            
            PreparedStatement insert = connection.prepareStatement(INSERT);
            //El id es autoincremental 
            String modality  = employmentMode.getModality();
            insert.setString(1, modality);
            
            insert.executeUpdate();
            System.out.println("Categoria almacenada correctamente en la base de datos");
            return selectEmploymentModeByModalityString(modality).get();
            
        } catch (SQLException ex) {
            System.out.println("Hubo un error al insertar en la base de datos.");
            ex.printStackTrace();
        }
        return null; 
    }
    
    
    
/**
 * Devuelve un objeto Optional - EmploymentMode con la tupla completa desde la base de datos por medio del id.
 * @param id : int con el id de la modalidad de empleo solicitada.
 * @return Optional - EmploymentMode
 **/
public Optional<EmploymentMode> selectEmploymentModeById(int id){
        
        try {
            PreparedStatement select = connection.prepareStatement(SELECT_BY_ID);
            select.setInt(1, id);
            
            ResultSet resultSet = select.executeQuery();
            
            if(resultSet.next()){
                return Optional.of(
                    new EmploymentMode(
                        resultSet.getInt("id"),
                        resultSet.getString("modalidad")
                    )
                );
            }
            
            return Optional.empty();
            
        } catch (SQLException ex) {
            System.out.println("Hubo un error al obtener la categoria desde la base de datos: SQLException.");
            ex.printStackTrace();
        }
        return Optional.empty();
    }
    
    
/**
 * Devuelve un objeto Optional - EmploymentMode con la tupla completa desde la base de datos por medio del string de la modalidad.
 * @param modality : string de la modalidad de empleo solicitada.
 * @return Optional - EmploymentMode
 **/
public Optional<EmploymentMode> selectEmploymentModeByModalityString(String modality){
        
        try {
            PreparedStatement select = connection.prepareStatement(SELECT_BY_MODALITYSTRING);
            select.setString(1, modality);
            
            ResultSet resultSet = select.executeQuery();
            
            if(resultSet.next()){
                return Optional.of(
                    new EmploymentMode(
                        resultSet.getInt("id"),
                        resultSet.getString("modalidad")
                    )
                );
            }
            return Optional.empty();
        } catch (SQLException ex) {
            System.out.println("Hubo un error al obtener la categoria desde la base de datos: SQLException.");
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Devuelve una lista con todas las categorias en la base de datos.
     * @return List - EmploymentMode
     **/
    public List<EmploymentMode> getAllModalities(){
        
        try {
            PreparedStatement selectAll = connection.prepareStatement(SELECT_ALL);
            
            List<EmploymentMode> modalities = new ArrayList<>();
            ResultSet resultSet = selectAll.executeQuery();
            
            while(resultSet.next()){
                modalities.add(
                    new EmploymentMode(
                        resultSet.getInt("id"),
                        resultSet.getString("modalidad")
                    )
                );
            }
            return modalities;
        } catch (SQLException ex) {
            System.out.println("Hubo un error al obtener las categorias desde la base de datos: SQLException.");
            ex.printStackTrace();
        }
        
        return null;
    }



    
    
}


