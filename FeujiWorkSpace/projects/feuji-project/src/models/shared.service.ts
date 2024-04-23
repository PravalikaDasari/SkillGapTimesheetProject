import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SharedService {

   employeeUrl = 'http://13.48.82.196:8082/api/employee';
   userUrl= 'http://13.48.82.196:8082/api/users';
   accountProjectUrl='http://13.48.82.196:8083/api/accountProjects';
   holidayUrl='http://13.48.82.196:8084/api/holiday';
   accountServiceUrl='http://13.48.82.196:8081/api/accountSave';
   referenceUrl= 'http://13.48.82.196:8089/api/referencetype';

  constructor() { }
}
