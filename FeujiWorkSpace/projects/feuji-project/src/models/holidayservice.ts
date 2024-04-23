import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Holiday } from "./holiday.model";


@Injectable({
  providedIn: 'root'
})
export class HolidayService {
  public holiday:Holiday[]=[]
  constructor(private http:HttpClient) { }

  getholiday():Observable<Holiday[]>{
   return this.http.get<[Holiday]>(`http://13.48.82.196:8084/api/holiday`);

  }
  saveholiday(holiday:Holiday):Observable<Holiday>{
      return this.http.post<Holiday>(`http://13.48.82.196:8084/api/holiday`,holiday);
  }

  deleteHoliday(HolidayId:number){
    return this.http.delete(`http://13.48.82.196:8088/api/holiday/delete/${HolidayId}`);

  }
}