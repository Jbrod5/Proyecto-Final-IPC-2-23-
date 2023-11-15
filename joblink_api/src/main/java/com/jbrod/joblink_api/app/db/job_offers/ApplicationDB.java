
package com.jbrod.joblink_api.app.db.job_offers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manejo de apliacion a una oferta de empleo en la base de datos.
 * @author Jorge
 */
public class ApplicationDB {
    
    private static final String URL_MYSQL = "jdbc:mysql://localhost:3306/joblink";
    private static final String USER = "admin"; 
    private static final String PASSWORD = "admin";
    
    //Aplicar (crear)
    //Consultar (todas, todas por oferta, una por oferta-solicitante)
    //Actualizar estado
    //Agregar reporte
    
    private static final String INSERT = "INSERT INTO aplicacion (oferta, solicitante, razon_aplicacion, estado) VALUES (?, ?, ?, ?)";

    private static final String SELECT_ALL = "SELECT * FROM aplicacion";
    private static final String SELECT_BY_OFFER = "SELEC * FROM aplicacion WHERE oferta = ?";
    private static final String SELECT_SPECIFIC_APP = "SELECT * FROM aplicacion WHERE oferta = ? AND solicitante = ?";
    
    private static final String UPDATE_STATE  = "UPDATE aplicacion SET estado = ? WHERE oferta = ? AND solicitante = ?";
    private static final String UPDATE_REPORT = "UPDATE aplicacion SET reporte = ? WHERE oferta = ? AND solicitante = ?";    
    
    private Connection connection; 

    public ApplicationDB() {
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
     * Crea una tupla Aplicacion en la base de datos. 
     * @param application: objeto Application (unicamente prescindible del report y status)
     * @return Application: el mismo objeto application que ingreso en caso de qe se almacene correctamente, null en caso contrario.
     **/
    public Application createApplication (Application application){
        
        try {
            PreparedStatement insert = connection.prepareStatement(INSERT);
            // Insert necesita los siguientes parametros:  oferta, solicitante, razon_aplicacion, estado           
            insert.setInt   (1, application.getOffer ());
            insert.setString(2, application.getSeeker());
            insert.setString(3, application.getReason());
            int statusSelection = 1;
            insert.setInt   (4, statusSelection);
            
            insert.executeUpdate();
            System.out.println("Aplicacion almacenada correctamente en la base de datos.");
            return application;
            
        } catch (SQLException ex) {
            System.out.println("Hubo un error al insertar la aplicacion de empleo en la base de datos.");
            ex.printStackTrace();
        }
        return null; 
    }
    
    
    /**
     * Retorna una lista de todas las aplicaciones encontradas en la base de datos.
     * @return List - Application: lista con los objetos Application entontrados.
     **/
    public List<Application> getAllApplications(){
        
        try {
            PreparedStatement selectAll = connection.prepareStatement(SELECT_ALL);
            
            List <Application> applications = new ArrayList<>();
            ResultSet resultSet = selectAll.executeQuery();
            
            applications = getListApplications(resultSet);
            return applications;
            
        } catch (SQLException ex) {
            System.out.println("Hubo un error al obtener las aplicaciones desde la base de datos: SQLException.");
            ex.printStackTrace();
        }
        return null;
    }
    
    /**
     * Retorna una lista de todas las aplicaciones encontradas en la base de datos segun una oferta de trabajo en concreto.
     * @param offer : int con el id de la oferta que se desea buscar.
     * @return List - Application: lista con los objetos Application entontrados.
     **/
    public List<Application> getAllApplicationsByOffer(int offer){
        try {
            PreparedStatement selectAll = connection.prepareStatement(SELECT_BY_OFFER);
            selectAll.setInt(1, offer);
            
            List <Application> applications = new ArrayList<>();
            ResultSet resultSet = selectAll.executeQuery();
            
            applications = getListApplications(resultSet);
            return applications;
            
        } catch (SQLException ex) {
            System.out.println("Hubo un error al obtener las aplicaciones desde la base de datos: SQLException.");
            ex.printStackTrace();
        }
        return null;
    }
    
    
    /**
     * Retorna una lista de todas las aplicaciones encontradas en la base de datos segun una oferta de trabajo en concreto.
     * @param offer : int con el id de la oferta que se desea buscar.
     * @param seeker : String con el username del solicitante
     * @return List - Application: lista con los objetos Application entontrados.
     **/
    public List<Application> getSpecificApplication(int offer, String seeker){
        try {
            PreparedStatement selectAll = connection.prepareStatement(SELECT_SPECIFIC_APP);
            selectAll.setInt(1, offer);
            selectAll.setString(2, seeker);
            
            List <Application> applications = new ArrayList<>();
            ResultSet resultSet = selectAll.executeQuery();
            
            applications = getListApplications(resultSet);
            return applications;
            
        } catch (SQLException ex) {
            System.out.println("Hubo un error al obtener las aplicaciones desde la base de datos: SQLException.");
            ex.printStackTrace();
        }
        return null;
    }
    
    /**
     * Actualiza el estado de una aplicacion de empleo en base al id de la oferta y el username del solicitante.
     * @param offer : id de la oferta a actualizar.
     * @param seeker: string con el username del solicitante que realiza la aplicacion.
     * @param status : nuevo estado de la aplicacion (consultar en la base de datos los estados disponibles). 
     * @return boolean : true si se ha realizado la actualizacion correctamente.
     **/
    public boolean updateStatus(int offer, String seeker, int status){
        PreparedStatement update;
        try {
            
            update = connection.prepareStatement(UPDATE_STATE);
            // necesita de: id, status
            update.setInt   (1, status);
            update.setInt   (2, offer);
            update.setString(3, seeker);
            
            update.executeUpdate();
            return true; 
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false; 
    }
    
    /**
     * Actualiza el estado de una aplicacion de empleo en base al id de la oferta y el username del solicitante.
     * @param offer : id de la oferta a actualizar.
     * @param seeker: string con el username del solicitante que realiza la aplicacion.
     * @param report : reporte sobre la entrevista. 
     * @return boolean : true si se ha realizado la actualizacion correctamente.
     **/
    public boolean updateReport(int offer, String seeker, String report){
        PreparedStatement update;
        try {
            
            update = connection.prepareStatement(UPDATE_STATE);
            // necesita de: id, status
            update.setString(1, report);
            update.setInt   (2, offer);
            update.setString(3, seeker);
            
            update.executeUpdate();
            return true; 
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false; 
    }
    
    
    
    
    // ---------- Para uso interno de la clase ------------- //
    /**
     * Para uso unico de la clase: devuelve una lista en de JobOffer al convertir las tuplas dentro de un ResultSet.
     * @param resultSet : resultSet con la query ya realizada.
     * @return List - JobOffer : lista con todos los objetos jobOffer encontrados en la consulta.
     **/
    private List<Application> getListApplications(ResultSet resultSet) throws SQLException{
        List<Application> applications = new ArrayList<>();
        
        while(resultSet.next()){
            applications.add(
                new Application(
                    resultSet.getInt("oferta"),
                    resultSet.getString("solicitante"),
                    resultSet.getString("razon_aplicacion"),
                    resultSet.getInt("estado"),
                    resultSet.getString("reporte")
                )
            );
        }
        return applications;
    }
}
