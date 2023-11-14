
package com.jbrod.joblink_api.app.services.job_offers;

import com.jbrod.joblink_api.app.db.job_offers.JobCategory;
import com.jbrod.joblink_api.app.db.job_offers.JobCategoryDB;
import com.jbrod.joblink_api.app.exceptions.InvalidInformationException;
import com.jbrod.joblink_api.app.exceptions.NotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de manejo de categorias usando JobCaategoryDB.
 * @author Jorge
 */
public class JobCategoryService {

    private JobCategoryDB jobCategoryDB;
    
    public JobCategoryService(JobCategoryDB jcdb) {
        jobCategoryDB = jcdb;
    }
    
    
    /**
     * Crea una categoria de empleo usando el administrador de base de datos mediante un objeto jobcategory con al menos la categoria a usar.
     * @param jobCategory : Objeto JobCategory con al menos el string category.
     * @return JobCategory: Retorna el objeto con la categoria YA CREADA (con id, category y enabled).
     **/
    public JobCategory createJobCategory(JobCategory jobCategory) throws InvalidInformationException{
        if(jobCategory.getCategory() != null){
            return jobCategoryDB.createCategory(jobCategory);
        }
        throw new InvalidInformationException("El objeto jobcategory que se intenta ingresar no cuenta con un StringCategory.");
    }

    public JobCategory getById(int id) throws NotFoundException{
        Optional<JobCategory> jobcat = jobCategoryDB.selectCategoryById(id);
        return jobcat.orElseThrow( () -> new NotFoundException("No se encontro una categoria con el id indicado."));
    }
    
    public JobCategory getByCategoryString(String category) throws NotFoundException{
        Optional <JobCategory> jobcat = jobCategoryDB.selectCategoryByCategorystring(category);
        return jobcat.orElseThrow( () -> new NotFoundException("No se encontro una categoria con el String de categoria indicado."));
    }
    
    public boolean changeStatusEnabled(int id){
        return jobCategoryDB.changeEnabledStatusById(id);
    }
    
    public List<JobCategory> getAllCategories(){
        return jobCategoryDB.getAllCategories();
    }
    
    public List<JobCategory> getAllEnabledCategories(){
        return jobCategoryDB.getAllEnabledCategories();
    }
}
