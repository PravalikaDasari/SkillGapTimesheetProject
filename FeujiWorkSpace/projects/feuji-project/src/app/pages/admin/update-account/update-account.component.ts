import { Component, OnInit } from '@angular/core';
import { AccountserviceService } from '../../../../models/accountservice.service';
import { ActivatedRoute } from '@angular/router';
import { Account } from '../../../../models/account.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'update-account',
  templateUrl: './update-account.component.html',
  styleUrl: './update-account.component.css'
})
export class UpdateAccountComponent  implements OnInit {
  constructor(private ref:ActivatedRoute,private accountService :AccountserviceService){}
      public accountid:string="";




      public account:any=Account;
      public emplyoee: any[] = [];
  public businessUnitType: any[] = [];
  public statusTypes:any[]=[];
  public account1:any[]=[];
  public acc:any;
   ngOnInit(): void {
    
    this.accountid  = history.state.account.uuId;
    this.send(this.accountid)
  this.getAccount();
this.getBusinessUnitType();
this.getStatusType();
this.getEmployeeName();
   }

   send(accountId:string){
     this.accountService.getAccountByUuId(accountId).subscribe(
     (items)=>{
     this.acc=items[0];

     }

   )
   }
   updateAccount(uuid:any,isDeleted:boolean) {
    this.acc.uuId=uuid;
    

    this.acc.isDeleted=isDeleted;

    this.accountService.updateAccount(this.acc).subscribe({
      next: (res) => {
        this.account = res;
        
        Swal.fire('Success', 'Data updated', 'success');
      },
      error: (error) => {
        console.error('There was an error!', error);
        Swal.fire('Error', 'Failed to update data: ' + error.message, 'error');
      }
    });
}
getEmployeeName(): void {

  this.accountService.getEmployeeName().subscribe(
    (data) => {
      this.emplyoee = data;
    },
    error => {
      console.error('Error occurred while fetching account names:', error);
    }
  );
}



getBusinessUnitType() {
  this.accountService.getBusinessUnitType().subscribe((businessUnit:any[]) => {
    this.businessUnitType = businessUnit;
  });
}
getStatusType() {
  this.accountService.getStatusType().subscribe((status:any[]) => {

    this.statusTypes = status;
  });
}
getAccount(){
  this.accountService.getAccount().subscribe(data=>{
   this.account1=data;
 })

}

}
