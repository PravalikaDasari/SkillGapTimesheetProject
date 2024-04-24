import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AccountprojectService } from '../../../../models/accountproject.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-account-project-display',
  templateUrl: './account-project-display.component.html',
  styleUrl: './account-project-display.component.css'
})
export class AccountProjectDisplayComponent implements OnInit{


  public accountProject:any={};
  constructor(private accountProjectService: AccountprojectService ,private router: Router ){}
  ngOnInit(){
    this.getAccountProjects();
  }




  getAccountProjects(){
    this.accountProjectService.getAccountProjects().subscribe(data=>{
      this.accountProject=data;
    })

  }


  

  


 editItem(project: any) {
  
 
    this.router.navigate(['/admin/project-update'],  { state: {project} });
  }


  removeTask(accountProjectId:number) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'You will not be able to recover this holiday!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, keep it'
    }).then((result) => {
      if (result.isConfirmed) {
        console.log("component deleted", accountProjectId);
        this.accountProjectService.deleteEmployee(accountProjectId).subscribe(res => {
          Swal.fire({
            title: 'Deleted Successfully',
         
            icon: 'success',
            showCancelButton: true,
            confirmButtonText: 'OK',
            
          })
          this.getAccountProjects();
        });
      } else if (result.dismiss === Swal.DismissReason.cancel) {
        // Handle cancel action
        // No deletion occurred
      }
    });
  
  }
}






