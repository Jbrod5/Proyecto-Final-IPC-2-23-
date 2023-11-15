
package com.jbrod.joblink_api.app.db.job_offers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manejo de ofertas de empleo en la base de datos.
 * @author Jorge
 */
public class JobOfferDB {

    private static final String URL_MYSQL = "jdbc:mysql://localhost:3306/joblink";
    private static final String USER = "admin"; 
    private static final String PASSWORD = "admin";
    
    //Una oferta debe poder crearse, consultarse (una y todas, todas las de un empleador, todas las de una categoria), cambiar de estado.
    private static final String INSERT = "INSERT INTO ofertaempleo (nombre, descripcion, categoria, fecha_publicacion, fecha_limite_aplicacion, salario_aproximado, modalidad, ubicacion, detalles, empleador, estado) VALUES (?,?,   ?,   ?, ?, ?,    ?,   ?, ?,    ?, ?)";
    private static final String SELECT_ALL = "SELECT * FROM ofertaempleo";
    private static final String SELECT_BY_ID = "SELECT * FROM ofertaempleo WHERE id = ?";
    private static final String SELECT_ALL_BY_CATEGORY = "SELECT * FROM ofertaempleo WHERE categoria = ?";
    private static final String SELECT_ALL_BY_EMPLOYER = "SELECT * FROM ofertaempleo WHERE empleador = ?";
    private static final String UPDATE_STATE = "UPDATE ofertaempleo SET estado = ? WHERE id = ?";
    
    private Connection connection; 

    public JobOfferDB() {
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
     * Crea una tupla OfertaEmpleo en la base de datos. 
     * @param offer: objeto JobOffer (unicamente prescindible del id)
     * @return JobOffer: el mismo objeto offer que ingreso en caso de qe se almacene correctamente, null en caso contrario.
     **/
    public JobOffer createOffer (JobOffer offer){
        
        try {
            PreparedStatement insert = connection.prepareStatement(INSERT);
            // Insert necesita los siguientes parametros: 
            //nombre, descripcion, categoria, fecha_publicacion, fecha_limite_aplicacion, 
            //salario_aproximado, modalidad, ubicacion, detalles, empleador, estado
            
            insert.setString(1, offer.getName               ()                                      );
            insert.setString(2, offer.getDescription        ()                                      );
            insert.setInt   (3, offer.getCategory           ()                                      );
            insert.setDate  (4, Date.valueOf                (offer.getPublicationDate    () )   );
            insert.setDate  (5, Date.valueOf                (offer.getApplicationDeadline() )   );
            insert.setInt   (6, offer.getApproximateSalary  ()                                      );
            insert.setInt   (7, offer.getModality           ()                                      );
            insert.setString(8, offer.getLocation           ()                                      );
            insert.setString(9, offer.getDetails            ()                                      );
            insert.setString(10, offer.getEmployer          ()                                      );
            insert.setInt   (11, offer.getStatus            ()                                      );
            
            insert.executeUpdate();
            System.out.println("Oferta almacenada correctamente en la base de datos.");
            return offer;
            
        } catch (SQLException ex) {
            System.out.println("Hubo un error al insertar la oferta de empleo en la base de datos.");
            ex.printStackTrace();
        }
        return null; 
    }
    
    
    
    public Optional<JobOffer> selectJobOfferById(int id){
        try {
            PreparedStatement select = connection.prepareStatement(SELECT_BY_ID);
            select.setInt(1, id);
            
            ResultSet resultSet = select.executeQuery();
            JobOffer offer = getByResultSet(resultSet);
            
            return Optional.of(offer);
            
        } catch (SQLException ex) {
            System.out.println("Hubo un error al obtener la oferta de empleo desde la base de datos: SQLException.");
            ex.printStackTrace();
        }
        return Optional.empty();
    }
    
    
    
    /**
     * Retorna una lista de todas las ofertas encontradas en la base de datos.
     * @return List - JobOffer : lista con los objetos JobOffer entontrados.
     **/
    public List<JobOffer> getAllOffers(){
        
        try {
            PreparedStatement selectAll = connection.prepareStatement(SELECT_ALL);
            
            List <JobOffer> offers = new ArrayList<>();
            ResultSet resultSet = selectAll.executeQuery();
            
            offers = getListOffers(resultSet);
            return offers;
            
        } catch (SQLException ex) {
            System.out.println("Hubo un error al obtener las ofertas de empleo desde la base de datos: SQLException.");
            ex.printStackTrace();
        }
        return null;
    }
    
    
    /**
     * Retorna una lista de todas las ofertas encontradas en la base de datos que coincidan con una categoria.
     * @param category : int con el id de la categoria a buscar.
     * @return List - JobOffer : lista con los objetos JobOffer entontrados.
     **/
    public List<JobOffer> getAllOffersByCategory(int category){
        
        try {
            PreparedStatement selectAll = connection.prepareStatement(SELECT_ALL_BY_CATEGORY);
            selectAll.setInt(1, category);
            
            
            List <JobOffer> offers = new ArrayList<>();
            ResultSet resultSet = selectAll.executeQuery();
            
            offers = getListOffers(resultSet);
            return offers;
            
        } catch (SQLException ex) {
            System.out.println("Hubo un error al obtener las ofertas de empleo desde la base de datos: SQLException.");
            ex.printStackTrace();
        }
        return null;
    }
    
    /**
     * Retorna una lista de todas las ofertas encontradas en la base de datos que coincidan con un empleador.
     * @param employer : String con el username del empleador a buscar.
     * @return List - JobOffer : lista con los objetos JobOffer entontrados.
     **/
    public List<JobOffer> getAllOffersByEmployer(String employer){
        
        try {
            PreparedStatement selectAll = connection.prepareStatement(SELECT_ALL_BY_EMPLOYER);
            selectAll.setString(1, employer);
            
            
            List <JobOffer> offers = new ArrayList<>();
            ResultSet resultSet = selectAll.executeQuery();
            
            offers = getListOffers(resultSet);
            return offers;
            
        } catch (SQLException ex) {
            System.out.println("Hubo un error al obtener las ofertas de empleo desde la base de datos: SQLException.");
            ex.printStackTrace();
        }
        return null;
    }
    
    /**
     * Actualiza el estado de una oferta de empleo en base al id de la oferta.
     * @param id : id de la oferta a actualizar.
     * @param status : nuevo estado de la oferta (consultar en la base de datos los estados disponibles). 
     * @return boolean : true si se ha realizado la actualizacion correctamente.
     **/
    public boolean updateStatus(int id, int status){
        PreparedStatement update;
        try {
            
            update = connection.prepareStatement(UPDATE_STATE);
            // necesita de: id, status
            update.setInt(1, id);
            update.setInt(2, status);
            
            update.executeUpdate();
            return true; 
            
        } catch (SQLException ex) {
            Logger.getLogger(JobOfferDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false; 
    }
    
    
    
    
    
    // -------------------- Para uso interno de la clase --------------------------//
    
    /**
     * Para uso unico de la clase: devuelve una lista en de JobOffer al convertir las tuplas dentro de un ResultSet.
     * @param resultSet : resultSet con la query ya realizada.
     * @return List - JobOffer : lista con todos los objetos jobOffer encontrados en la consulta.
     **/
    private List<JobOffer> getListOffers(ResultSet resultSet) throws SQLException{
        List<JobOffer> offers = new ArrayList<>();
        
        while(resultSet.next()){
            offers.add(
                new JobOffer(
                    resultSet.getInt("id"),
                    resultSet.getString("nombre"),
                    resultSet.getString("descripcion"),
                    resultSet.getInt("categoria"),
                    resultSet.getDate("fecha_publicacion").toLocalDate(),
                    resultSet.getDate("fecha_limite_aplicacion").toLocalDate(),
                    resultSet.getInt("salario_aproximado"),
                    resultSet.getInt("modalidad"),
                    resultSet.getString("ubicacion"),
                    resultSet.getString("detalles"),
                    resultSet.getString("empleador"),
                    resultSet.getInt("estado")
                )
            );
        }
        return offers;
    }
    
    /**
     * Para uso unico de la clase: devuelve un objeto JobOffer al convertir la tupla dentro de un ResultSet.
     * @param resultSet : resultSet con la query ya realizada.
     * @return JobOffer : objeto jobOffer encontrado en la consulta.
     **/
    private JobOffer getByResultSet( ResultSet resultSet) throws SQLException{
        if(resultSet.next()){
            return new JobOffer(
                resultSet.getInt("id"),
                resultSet.getString("nombre"),
                resultSet.getString("descripcion"),
                resultSet.getInt("categoria"),
                resultSet.getDate("fecha_publicacion").toLocalDate(),
                resultSet.getDate("fecha_limite_aplicacion").toLocalDate(),
                resultSet.getInt("salario_aproximado"),
                resultSet.getInt("modalidad"),
                resultSet.getString("ubicacion"),
                resultSet.getString("detalles"),
                resultSet.getString("empleador"),
                resultSet.getInt("estado")
            );     
        }
        return null; 
    }

    
}