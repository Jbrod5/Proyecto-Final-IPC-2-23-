import { Injectable } from "@angular/core";
import { User } from "src/app/entities/User";
import { HttpClient } from "@angular/common/http"
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})

export class UserService{

    readonly API_URL = "http://localhost:8080/joblink_api/v1/";

    constructor(private httpCLient: HttpClient){}

    public createUser(user: User): Observable<User>{
        console.log ('conectando a joblink api: ' + user)
        return this.httpCLient.post<User>(this.API_URL + "users?" + "action=login", user);
    }

}   