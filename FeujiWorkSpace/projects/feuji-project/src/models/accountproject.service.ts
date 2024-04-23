
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Accountproject } from './accountproject.model';

@Injectable({
  providedIn: 'root'
})
export class AccountprojectService {


  apiUrl = 'http://13.48.82.196:8083'; // Replace with your actual API URL
  billurl="http://13.48.82.196:8089/api/referencedetails/getref"
  constructor(private http: HttpClient) {}

  getAccountProjects(): Observable<Accountproject[]> {
    const url = `${this.apiUrl}/api/accountProjects/account-project-dto`;
    return this.http.get<Accountproject[]>(url);
  }

// Save Account
  saveAccountProject(projectData: Accountproject): Observable<Accountproject> {
    const headers = {'content-type':'application/json'};
    return this.http.post<Accountproject>(`http://13.48.82.196:8083/api/accountProjects/save`, projectData,{headers});
  }

  // Account Name
  getAccountName():Observable<any[]>{
    return this.http.get<any[]>(`${this.apiUrl}/api/accountProjects/getaccount`);
  }

  // Employee Name
  getEmployeeName():Observable<any[]>{
    return this.http.get<any[]>(`${this.apiUrl}/api/accountProjects/getEmployee`);
  }

  public getPriorityType():Observable<any[]>
  {

    return this.http.get<any[]>(`${this.billurl}/Account Project Priority`)

  }

  public getStatusType():Observable<any[]>
  {

    return this.http.get<any[]>(`${this.billurl}/Account Project Status`)

  }

  getByUUId(uuid: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/api/accountProjects/getByUuid/${uuid}`);
  }


  getAccountProjectByUuid(uuid: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/api/accountProjects/getAccountProjectUpdate/${uuid}`);
  }
  
  deleteRow(projectId: number):Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/api/accountProjects/delete/${projectId}`,projectId );
  }



    updateAccountProject(accountData:Accountproject): Observable<Accountproject> {
      return this.http.put<Accountproject>(`${this.apiUrl}/api/accountProjects/updateAccountProject`, accountData);
    }
    deleteEmployee(accountProjectId:number):Observable<any>{

      return this.http.delete<any>(`${this.apiUrl}/api/accountProjects/deleteAccProject/${accountProjectId}`)
    }

}
