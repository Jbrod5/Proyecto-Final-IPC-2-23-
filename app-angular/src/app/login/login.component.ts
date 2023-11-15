import { Component } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { User } from '../entities/User';
import { UserService } from '../Services/Users/UsersService';
import { FormBuilder } from '@angular/forms';
import { Validators } from '@angular/forms';      


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  user!: User; 
  login! : FormGroup;
  saved!: boolean;

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    
  ){
    this.saved = false; 
  }


  ngOnInit(): void {
    this.login = this.formBuilder.group({
      username: [null, [Validators.required]],
      password: [null, [Validators.required]]
    });   
  }

  submit(): void {
    console.log('submit!!!');
   // if (this.login.valid) {
      console.log('validopibes');
      this.user = this.login.value as User;
      this.userService.createUser(this.user).subscribe({  
        next: (created: User) => {
          console.log("creataeds " + created);
          //this.limpiar();
          this.saved = true;
        },
        error: (error: any) => {
          console.log("error"); 
        }
      });
   // }
  }

}


