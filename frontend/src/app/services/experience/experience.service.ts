import { Injectable } from '@angular/core';
import {JobExperienceDTO, jobExperienceResponse} from "../../model/api-responses";
import {environment} from "../../../environments/environment";
import {ApiService} from "../mainApi/api.service";
import {Observable} from "rxjs";

// @ts-ignore
export const STATIC_JOB_DATA: JobExperienceDTO[] = [
    {
      "id": 6,
      "title": "Architectural Intern",
      "company": "Hyphen Design and Consultant Pvt. Ltd.",
      "location": null,
      "employmentType": "internship",
      "description": "Worked as an intern under professional architects and engineers on residential and interior projects. Participated in design development, drafting, and site supervision activities to gain practical exposure to architectural workflows.",
      "achievements": [
        "Prepared working drawings and presentation sheets for residential projects under mentor guidance.",
        "Assisted in interior design projects by producing layout plans and 3D visualizations.",
        "Visited project sites to document existing conditions and assist in site measurements."
      ],
      "technologiesUsed": [
        "AutoCAD",
        "SketchUp",
        "Lumion",
        "Photoshop"
      ],
      "startDate": "2024-09-08",
      "endDate": "2024-11-30",
      "isCurrent": false,
      "displayOrder": 1,
      "insertUser": "admin",
      "editUser": "N/A",
      "photos": [],
      "extra": null
    },
    {
      "id": 8,
      "title": "Freelance Interior Designer",
      "company": "Residential Apartment Project",
      "location": null,
      "employmentType": "freelance",
      "description": "Designed the interior of a two-bedroom apartment, focusing on functionality, minimalism, and cost efficiency. Handled client meetings, space planning, and final presentation drawings.",
      "achievements": [
        "Delivered design layouts that optimized lighting and furniture placement.",
        "Provided material and color palette suggestions aligning with client preferences.",
        "Produced detailed 3D renders for client approval."
      ],
      "technologiesUsed": [
        "SketchUp",
        "V-Ray",
        "AutoCAD",
        "Photoshop"
      ],
      "startDate": "2024-03-01",
      "endDate": "2024-04-20",
      "isCurrent": false,
      "displayOrder": 3,
      "insertUser": "admin",
      "editUser": "N/A",
      "photos": [],
      "extra": null
    },
    {
      "id": 9,
      "title": "Community Center Design – Academic Project",
      "company": "Nepal Engineering College",
      "location": null,
      "employmentType": "academic",
      "description": "Collaborated with a team of students to design a sustainable and inclusive community center that encourages social interaction and recreation.",
      "achievements": [
        "Contributed to site analysis, conceptual design, and presentation drawings.",
        "Focused on accessibility and open-space design for community engagement.",
        "Produced final drawings and renders showcased in the departmental exhibition."
      ],
      "technologiesUsed": [
        "AutoCAD",
        "SketchUp",
        "Lumion",
        "InDesign"
      ],
      "startDate": "2023-08-01",
      "endDate": "2023-11-30",
      "isCurrent": false,
      "displayOrder": 4,
      "insertUser": "admin",
      "editUser": "N/A",
      "photos": [
        {
          "id": 14,
          "imageUrl": "http://res.cloudinary.com/dcqktjyqh/image/upload/v1760064965/portfolio_images/xpyg0eamidy9lipqdbft.jpg",
          "caption": "this is a real good test",
          "displayOrder": 1
        }
      ],
      "extra": null
    },
    {
      "id": 10,
      "title": "Research on Sustainable Building Materials in Nepal",
      "company": "Nepal Engineering College – Department of Architecture",
      "location": null,
      "employmentType": "academic",
      "description": "Conducted a study on locally available sustainable materials suitable for modern residential design in Nepal. The project emphasized environmental impact and material lifecycle assessment.",
      "achievements": [
        "Collected and analyzed data on traditional and alternative eco-friendly materials.",
        "Published a short paper summarizing material comparisons and environmental impact.",
        "Presented findings at a departmental research seminar."
      ],
      "technologiesUsed": [
        "MS Excel",
        "AutoCAD",
        "Photoshop"
      ],
      "startDate": "2023-12-01",
      "endDate": "2024-02-15",
      "isCurrent": false,
      "displayOrder": 5,
      "insertUser": "admin",
      "editUser": "N/A",
      "photos": [
        {
          "id": 15,
          "imageUrl": "http://res.cloudinary.com/dcqktjyqh/image/upload/v1760064994/portfolio_images/w0io5crnijczpp9qhinx.jpg",
          "caption": "this is another good exp",
          "displayOrder": 0
        }
      ],
      "extra": null
    },
    {
      "id": 11,
      "title": "Parametric Design Workshop Participant",
      "company": "Parametric Architecture Nepal",
      "location": null,
      "employmentType": "training",
      "description": "Attended a hands-on workshop focusing on computational and parametric design tools for architectural innovation.",
      "achievements": [
        "Learned fundamentals of parametric modeling and algorithmic design processes.",
        "Created experimental façade designs using generative modeling techniques.",
        "Collaborated with peers on a final concept model presented at the workshop closing."
      ],
      "technologiesUsed": [
        "Rhinoceros 3D",
        "Grasshopper",
        "Photoshop"
      ],
      "startDate": "2024-07-10",
      "endDate": "2024-07-15",
      "isCurrent": false,
      "displayOrder": 6,
      "insertUser": "admin",
      "editUser": "N/A",
      "photos": [],
      "extra": null
    },
    {
      "id": 7,
      "title": "Final Year Architectural Thesis Project",
      "company": "Nepal Engineering College",
      "location": null,
      "employmentType": "academic",
      "description": "Completed a comprehensive architectural thesis focused on sustainable urban housing in the Kathmandu Valley. The project explored energy-efficient materials and spatial optimization for dense urban areas.",
      "achievements": [
        "Developed conceptual and detailed designs for a multi-family urban housing complex.",
        "Created 3D visualizations and walkthroughs demonstrating sustainable design elements.",
        "Presented the final project to a jury panel and received positive feedback on design clarity."
      ],
      "technologiesUsed": [
        "Revit",
        "SketchUp",
        "AutoCAD",
        "Photoshop",
        "Lumion"
      ],
      "startDate": "2025-01-15",
      "endDate": "2025-06-15",
      "isCurrent": false,
      "displayOrder": 2,
      "insertUser": "admin",
      "editUser": "N/A",
      "photos": [],
      "extra": {
        "modelURL": "https://test.ismart.devanasoft.com.np/ismart/bankApps/test/portfolio/sadikshya/FinalYearArchitecturalThesisProject7/experi.glb"
      }
    }
  ];

@Injectable({
  providedIn: 'root'
})
export class ExperienceService {

  private userId=environment.userId;

  constructor(private apiService: ApiService) { }

  getExperience():Observable<jobExperienceResponse>{
    return this.apiService.getWithoutAuth<jobExperienceResponse>(`/api/experiences/users/viewAll/${this.userId}`);
  }
}
