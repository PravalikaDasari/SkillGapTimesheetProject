import { Injectable } from "@angular/core";
import { Skill } from "./skill.model";
import { SkillService } from "./skill.service";

@Injectable({providedIn: 'root'})
export class SkillRepo{

  public skill:Skill[]=[];

  constructor(private dataSource:SkillService) {
      this.getSkills();
   }

  getSkills(){
     this.dataSource.getSkills().subscribe(data=>{
      this.skill=data;
    })
  }


  resetForm(): void {
  }

}

