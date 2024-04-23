import { Injectable } from "@angular/core";
import { User } from "./user.model";
import { UserService } from "./user.service";
import { Employee } from "./employee.model";
@Injectable({providedIn: 'root'})
export class UserRepo
{
  empDataById: any;


constructor(private userService: UserService) {}

  public loggedInuserData: any;
  empId: Employee = new Employee(0,'','','','','','','','',new Date(),0,'','',0,'',new Date(),'','',0,new Date(),'',new Date(),0,'');

  getUserData(data: any) {
    this.loggedInuserData = data;
    this.getEmployeeById();
  }

  getEmployeeById() {
    const userEmpId = this.loggedInuserData.userEmpId;

    if (userEmpId) {
      this.userService.getEmployeeByid(userEmpId).subscribe(
        (result:any) => {
          this.empDataById=result[0];
        },
        (error) => {
          console.error('Error fetching employee details', error);
        }
      );
    } else {
      console.error('Invalid userEmpId');
    }
  }
}