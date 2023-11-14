
package com.jbrod.joblink_api.app.services.job_offers;

import com.jbrod.joblink_api.app.db.job_offers.EmploymentMode;
import com.jbrod.joblink_api.app.db.job_offers.EmploymentModeDB;
import com.jbrod.joblink_api.app.exceptions.InvalidInformationException;
import com.jbrod.joblink_api.app.exceptions.NotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de manejo de categorias usando EmploymentModeDB.
 * @author Jorge
 */
public class EmploymentModeService {

    private EmploymentModeDB employmentModeDB;

    public EmploymentModeService(EmploymentModeDB employmentModeDB) {
        this.employmentModeDB = employmentModeDB;
    }
    
    
    
    /**
     * Crea una modalidad de empleo en la base de datos.
     * @param employmentMode: Objeto del tipo EmploymentMode con al menos el StringModality.
     * @return EmploymentMode: Objeto con la tupla almancenada en la base de datos.
     **/
    public EmploymentMode createModality(EmploymentMode employmentMode) throws InvalidInformationException{
        if(employmentMode.getModality() != null){
            return employmentModeDB.createCategory(employmentMode);
        }
        throw new InvalidInformationException("El objeto EmploymentMode que se intenta ingresar no cuenta con un StringModality.");
    }
    
    public EmploymentMode getById(int id) throws NotFoundException{
        Optional<EmploymentMode> modality  = employmentModeDB.selectEmploymentModeById(id);
        return modality.orElseThrow( () -> new NotFoundException("No se encontro una categoria con el id indicado."));
    }
    
    public EmploymentMode getByModalityString(String modality) throws NotFoundException{
        Optional<EmploymentMode> modalityObj  = employmentModeDB.selectEmploymentModeByModalityString(modality);
        return modalityObj.orElseThrow( () -> new NotFoundException("No se encontro una categoria con el string indicado."));
    }
    
     public List<EmploymentMode> getAllModalities(){
        return employmentModeDB.getAllModalities();
    }
}
