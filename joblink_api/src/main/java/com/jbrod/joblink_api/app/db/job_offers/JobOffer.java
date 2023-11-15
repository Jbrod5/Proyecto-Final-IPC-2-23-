    
package com.jbrod.joblink_api.app.db.job_offers;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

/**
 * Objeto que representa una oferta de empleo en la base de datos.
 * @author Jorge
 */
public class JobOffer {

    private int id; 
    private String name; 
    private String description; 
    private int category; // -> fk JobCategory
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate  publicationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate applicationDeadline;
    
    private int approximateSalary;
    private int modality; // -> fk EmploymentMode
    private String location; 
    private String details;
    private String employer; // -> fk Employer
    private int status; // -> fk estadosOfertasEmpleo (sin representacion como objeto)

    public JobOffer() {
    
    }
    /**
     * Constructor para crear una tupla (sin id, publicationDate, status).
     **/
    public JobOffer(String name, String description, int category, LocalDate applicationDeadline, int approximateSalary, int modality, String location, String details, String employer) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.applicationDeadline = applicationDeadline;
        this.approximateSalary = approximateSalary;
        this.modality = modality;
        this.location = location;
        this.details = details;
        this.employer = employer;
        // id autoincrement, publicationDate today, status 1 "seleccion", 
        
        publicationDate = LocalDate.now();
        status = 1; // estado : "seleccion"
    }
    /**
     * Constructor completo.
     **/
    public JobOffer(int id, String name, String description, int category, LocalDate publicationDate, LocalDate applicationDeadline, int approximateSalary, int modality, String location, String details, String employer, int status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.publicationDate = publicationDate;
        this.applicationDeadline = applicationDeadline;
        this.approximateSalary = approximateSalary;
        this.modality = modality;
        this.location = location;
        this.details = details;
        this.employer = employer;
        this.status = status;
    }

    
    
    
    
    
    
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public LocalDate getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(LocalDate applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public int getApproximateSalary() {
        return approximateSalary;
    }

    public void setApproximateSalary(int approximateSalary) {
        this.approximateSalary = approximateSalary;
    }

    public int getModality() {
        return modality;
    }

    public void setModality(int modality) {
        this.modality = modality;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    
    
    
}
