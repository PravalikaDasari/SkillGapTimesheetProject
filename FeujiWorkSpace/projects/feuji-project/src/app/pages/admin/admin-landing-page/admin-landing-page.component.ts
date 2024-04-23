import { Component } from '@angular/core';

@Component({
  selector: 'app-admin-landing-page',
  templateUrl: './admin-landing-page.component.html',
  styleUrl: './admin-landing-page.component.css'
})
export class AdminLandingPageComponent {


  public empName:string='';
  public user:any;

  ngOnInit(){
    const user = localStorage.getItem("user");
    if (user) {
      this.user = JSON.parse(user);
      this.empName=this.user.firstName
    }
  }

}
