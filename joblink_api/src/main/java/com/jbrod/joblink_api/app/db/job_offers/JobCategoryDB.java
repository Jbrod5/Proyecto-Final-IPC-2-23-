
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
 * Manejo de categorias en la base de datos.
 * @author Jorge
 */
public class JobCategoryDB {


    private static final String URL_MYSQL = "jdbc:mysql://localhost:3306/joblink";
    private static final String USER = "admin"; 
    private static final String PASSWORD = "admin";

    //Una categoria debe poder agregarse, consultarse, habilitarse o desabilitarse, ademas de obtener todas las categorias    
    private static final String INSERT = "INSERT INTO categoriasofertasempleo (categoria, habilitada) VALUES (?,?)";
    private static final String SELECT_BY_ID = "SELECT * FROM categoriasofertasempleo WHERE id = ?";
    private static final String SELECT_BY_CATEGORYSTRING = "SELECT * FROM categoriasofertasempleo WHERE categoria = ?";
    private static final String CHANGE_ENABLED_STATUS = "UPDATE categoriasofertasempleo SET habilitada = ? WHERE id = ?" ;
    private static final String SELECT_ALL = "SELECT * FROM categoriasofertasempleo";
    
    private Connection connection; 

    public JobCategoryDB() {
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
     * Crea una tupla de CategoriasOfertasEmpleo en la base de datos. 
     * @param jobCategory : objeto categoria con la categoria a agregar, debe incluir: id, category, enabled.
     * @return JobCategory: exactamente el mismo objeto si se almaceno en la base de datos correctamente. 
     **/
    public JobCategory createCategory(JobCategory jobCategory){
        
        try {
            
            PreparedStatement insert = connection.prepareStatement(INSERT);
            //El id es autoincremental 
            String jobCategoryString = jobCategory.getCategory();
            insert.setString(1, jobCategoryString);
            //Habilitado por defecto 
            int enabled = 1; 
            insert.setInt(2, enabled);
            
            insert.executeUpdate();
            System.out.println("Categoria almacenada correctamente en la base de datos");
            return selectCategoryByCategorystring(jobCategoryString).get();
            
        } catch (SQLException ex) {
            System.out.println("Hubo un error al insertar en la base de datos.");
            ex.printStackTrace();
        }
        return null; 
    }
    
    /**
     * Devuelve un objeto Optional - JobCategory con la tupla completa de la categoria de interes por medio del id.
     * @param id : int con id de la categoria solicitada.
     * @return Optional - JobCategory.
     **/
    public Optional<JobCategory> selectCategoryById(int id){
        
        try {
            PreparedStatement select = connection.prepareStatement(SELECT_BY_ID);
            select.setInt(1, id);
            
            ResultSet resultSet = select.executeQuery();
            
            if(resultSet.next()){
                return Optional.of(
                    new JobCategory(
                        resultSet.getInt("id"),
                        resultSet.getString("categoria"),
                        resultSet.getInt("habilitada") != 0
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
     * Devuelve un objeto Optional - JobCategory con la tupla completa de la categoria de interes por medio del string de la categoria.
     * @param category : String con la cadena de la categoria solicitada.
     * @return Optional - JobCategory.
     **/
    public Optional<JobCategory> selectCategoryByCategorystring(String category){
        
        try {
            PreparedStatement select = connection.prepareStatement(SELECT_BY_CATEGORYSTRING);
            select.setString(1, category);
            
            ResultSet resultSet = select.executeQuery();
            
            if(resultSet.next()){
                return Optional.of(
                    new JobCategory(
                        resultSet.getInt("id"),
                        resultSet.getString("categoria"),
                        resultSet.getInt("habilitada") != 0
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
     * Cambia el estado habilitado de una categoria en la base de datos a base del id.
     * @param id: id de la categoria a cambiar.
     * @return boolean : true o false representando el nuevo estado habilitado.
     **/
    public boolean changeEnabledStatusById(int id){
        
        //1. Obtener el estatus de la categoria a cambiar
        boolean status = selectCategoryById(id).get().isEnabled();
        //2. Definir el nuevo estado 
        status = !status;
        int newStatus = status ? 1 : 0;
        //3. Actualizar
        try {
            
            PreparedStatement update = connection.prepareStatement(CHANGE_ENABLED_STATUS);
            //Se requiere la siguiente info: habilitada, id
            update.setInt(1, newStatus);
            update.setInt(2, id);
            
            update.executeUpdate(); 
            return true; 
            
        } catch (SQLException ex) {
            System.out.println("Hubo un error al actualizar el estado de la categoria en la base de datos: SQLException.");
            ex.printStackTrace();
        }
        
        return false; 
    }
    
    
    public List<JobCategory> getAllCategories(){
        
        try {
            PreparedStatement selectAll = connection.prepareStatement(SELECT_ALL);
            
            List<JobCategory> categories = new ArrayList<>();
            ResultSet resultSet = selectAll.executeQuery();
            
            while(resultSet.next()){
                categories.add(
                        new JobCategory(
                                resultSet.getInt("id"),
                                resultSet.getString("categoria"),
                                resultSet.getInt("habilitada") != 0
                    )
                );
            }
            return categories;
        } catch (SQLException ex) {
            System.out.println("Hubo un error al obtener las categorias desde la base de datos: SQLException.");
            ex.printStackTrace();
        }
        
        return null;
    }
    
    public List<JobCategory> getAllEnabledCategories(){
        
        try {
            PreparedStatement selectAll = connection.prepareStatement(SELECT_ALL);
            
            List<JobCategory> categories = new ArrayList<>();
            ResultSet resultSet = selectAll.executeQuery();
            
            while(resultSet.next()){
                if(resultSet.getInt("habilitada") != 0){
                    categories.add(
                           new JobCategory(
                                   resultSet.getInt("id"),
                                   resultSet.getString("categoria"),
                                   resultSet.getInt("habilitada") != 0
                       )
                   );
                }
            }
            return categories;
        } catch (SQLException ex) {
            System.out.println("Hubo un error al obtener las categorias desde la base de datos: SQLException.");
            ex.printStackTrace();
        }
        
        return null;
    }
    
}
