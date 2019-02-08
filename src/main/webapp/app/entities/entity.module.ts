import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'student',
                loadChildren: './student/student.module#IterateStudentModule'
            },
            {
                path: 'tutor',
                loadChildren: './tutor/tutor.module#IterateTutorModule'
            },
            {
                path: 'subject',
                loadChildren: './subject/subject.module#IterateSubjectModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IterateEntityModule {}
