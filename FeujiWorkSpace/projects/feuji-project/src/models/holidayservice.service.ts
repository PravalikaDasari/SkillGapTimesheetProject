import { Injectable } from "@angular/core";

import { Observable, throwError } from "rxjs"; // Import throwError from RxJS
import { catchError } from 'rxjs/operators'; // Import catchError from RxJS operators
import { Holiday } from "./holiday.model";
import { HttpClient, HttpHeaders } from "@angular/common/http";

@Injectable({providedIn: 'root'})
export class HolidayService {
  public holiday:Holiday[]=[]
  constructor(private http:HttpClient) { }


  private apiUrl='http://13.48.82.196:8084/api/holiday';
  getholiday():Observable<Holiday[]>{
   return this.http.get<[Holiday]>(`http://13.48.82.196:8084/api/holiday`);

  }
  saveholiday(holiday:Holiday):Observable<Holiday>{
      return this.http.post<Holiday>(`http://13.48.82.196:8084/api/holiday/save`,holiday);
  }
    
  updateHoliday(holiday:Holiday):Observable<String>{
    return this.http.put(`http://13.48.82.196:8084/api/holiday/update`,holiday,{responseType:'text'});
  }
  

  deleteRow(holidayId:any){
    const headers=new HttpHeaders({'content-Type':'application/json'});
    return this.http.delete<Holiday>(`${this.apiUrl}/delete/${holidayId}`);
    
  }

  getHolidayByYear( year:number):Observable<any>{
    
    return this.http.get<any>(`http://13.48.82.196:8084/api/holiday/getHolidayByYear/${year}`)

  }
  
}
