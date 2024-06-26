import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { SkillService } from '../../../../models/skill.service';
import { EmployeeSkillService } from '../../../services/employee-skill.service';

@Component({
  selector: 'app-employee-display',
  templateUrl: './employee-display.component.html',
  styleUrl: './employee-display.component.css'
})
export class EmployeeDisplayComponent {

  skillCategories: string[] = [];
  technicalCategories: string[] = [];
  selectedSkillCategory: string ='';
  selectedTechnicalCategory: string='';

  constructor(private http: HttpClient,private empskillService:EmployeeSkillService) {}

  onSkillCategoryChange() {
    
    if (this.selectedSkillCategory) {
      this.fetchTechnicalCategories(this.selectedSkillCategory);
    }
  }

  fetchTechnicalCategories(skillCategory: string) {
    const apiUrl = `http://13.48.82.196:8099/api/referencedetails/getref/${skillCategory}`;
    this.http.get<string[]>(apiUrl).subscribe(
      (response) => {
        this.technicalCategories = response;
      },
      (error) => {
        console.error('Error occurred while fetching technical categories', error);
      }
    );
  }


  skills = [
    { skillType: '', skillName: '', competencyLevel: '',  certification: ''}
  ];



  addTaskRow() {
    this.skills.push({ skillType: '', skillName: '', competencyLevel: '', certification: ''});
  }
 
  submit() {
  }
 
  ngOnInit(){
  }


}
