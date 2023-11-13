
package com.jbrod.joblink_api.app.db.users;

import com.fasterxml.jackson.annotation.JsonFormat; 
import jakarta.persistence.Tuple;
import java.time.LocalDate;

/**
 * Representa a un usuario generico dentro del sistema.
 * Hay usuarios especializados como Solicitante y Empleador. 
 * 
 * @author Jorge
 */
public class User {
    
    private String      username; 
    private String          name; 
    private String      password; 
    private String       address; 
    private String         email; 
    private int              cui;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate  birthDate; 
    
    private int type; //   ->  1: admin, 2: empresa, 3: usuario
    
    //Boolean para administrar si la info ha sido compleatada exitosamente
    private boolean profileCompleted;

    
    public User(){
    
    }

    //para registro
    public User(String username, String name, String password, String address, String email, int cui, LocalDate bithDate, int type, boolean profileCompleted) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.address = address;
        this.email = email;
        this.cui = cui;
        this.birthDate = bithDate;
        this.type = type;
        
        this.profileCompleted = profileCompleted;
    }
    
    //para registro
    public User(String username, String name, String password, String address, String email, int cui, String bithDate, int type, boolean profileCompleted) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.address = address;
        this.email = email;
        this.cui = cui;
        this.birthDate = LocalDate.now(); 
        this.type = type;
        this.profileCompleted = profileCompleted;
    }
    
    
    
    // Constructor -> usuario proveniente del fe para aprobar el inicio de sesion
    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
    
    // Constructor -> usuario con info suficiente que retorna el be despues de haberse aprobado el inicio de sesion
    public User(String username, int type, boolean profileCompleted){
        this.username = username; 
        this.type = type; 
        this.profileCompleted = profileCompleted; 
    }

    public boolean isProfileCompleted() {
        return profileCompleted;
    }

    public void setProfileCompleted(boolean profileCompleted) {
        this.profileCompleted = profileCompleted;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCui() {
        return cui;
    }

    public void setCui(int cui) {
        this.cui = cui;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBithDate(LocalDate bithDate) {
        this.birthDate = bithDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    
    
    
}
