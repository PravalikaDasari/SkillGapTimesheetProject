import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TimesheetHomeService {

  constructor(private http: HttpClient) { }
 prourl="http://13.48.82.196:8084/api/timesheet/getproject";
 tasktypeurl="http://13.48.82.196:8084/api/timesheet/getprojecttasktype"
 taskurl="http://13.48.82.196:8084/api/timesheet/getprojecttask"
  billurl="http://13.48.82.196:8089/api/referencedetails/getref"

  public getproject():Observable<any[]>
  {

    const url1=`${this.prourl}?employeeId=${108}`
    return this.http.get<any[]>(url1)
  }


  public getProjectTaskType(selectedProjectId:any):Observable<any[]>
  {

    const url1=`${this.tasktypeurl}?employeeId=${108}&accountProjectId=${selectedProjectId}`
   const finalproject= this.http.get<any[]>(url1)
   return finalproject;
  }

  public getProjectTask(selectedProjecttaskId:any):Observable<any[]>
  {

    const url1=`${this.taskurl}?taskTypeId=${selectedProjecttaskId}`
   const finalproject= this.http.get<any[]>(url1)
   return finalproject;


  }
  public getBillingType():Observable<any[]>
  {
    
    return this.http.get<any[]>(`${this.billurl}/Attendance Type`)
  
  }

}
