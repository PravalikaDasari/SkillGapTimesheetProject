import { Injectable } from "@angular/core";
import { Holiday } from "./holiday.model";
import { HolidayService } from "./holidayservice.service";

@Injectable({providedIn: 'root'})
export class holidayRepo{

  public holiday:Holiday[]=[];
  public hol:Holiday[]=[];

  constructor(private holidayService:HolidayService) {
      this.getholiday()
   }

   getholiday(){
     this.holidayService.getholiday().subscribe(data=>{
      this.holiday=data;
    })
  }

  getHolidayDetails():Holiday[]{
    return this.holiday;
  }

  SendHoliday(holiday:Holiday){
    this.holidayService.saveholiday(holiday).subscribe((res)=>{
    });
    
    

    
    
  }

 
}