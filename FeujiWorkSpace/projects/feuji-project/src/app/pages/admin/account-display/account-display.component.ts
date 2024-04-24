import { Component, OnInit } from '@angular/core';
import { AccountserviceService } from '../../../../models/accountservice.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-account-display',
  templateUrl: './account-display.component.html',
  styleUrl: './account-display.component.css'
})
export class AccountDisplayComponent implements OnInit {
  public account:any[]=[];
  constructor(private accountService: AccountserviceService, private router: Router ){}
  ngOnInit(){
    this.getAccount();
  }
  getAccount(){
    this.accountService.getAccount().subscribe(data=>{
     this.account=data;
   })

}

editItem(account: any) {
 
  this.router.navigate(['/admin/update-account'],{state: {account} });
}
removeTask(accountId:number) {
  Swal.fire({
    title: 'Are you sure?',
    text: 'You will not be able to recover this holiday!',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Yes, delete it!',
    cancelButtonText: 'No, keep it'
  }).then((result) => {
    if (result.isConfirmed) {
      console.log("component deleted", accountId);
      this.accountService.deleteEmployee(accountId).subscribe(res => {
        Swal.fire({
          title: 'Deleted Successfully',
       
          icon: 'success',
          showCancelButton: true,
          confirmButtonText: 'OK',
          
        })
        this.getAccount();
      });
    } else if (result.dismiss === Swal.DismissReason.cancel) {
      // Handle cancel action
      // No deletion occurred
    }
  });

}
}
