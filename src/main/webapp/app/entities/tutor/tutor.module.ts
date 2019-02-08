import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { IterateSharedModule } from 'app/shared';
import {
    TutorComponent,
    TutorDetailComponent,
    TutorUpdateComponent,
    TutorDeletePopupComponent,
    TutorDeleteDialogComponent,
    tutorRoute,
    tutorPopupRoute
} from './';

const ENTITY_STATES = [...tutorRoute, ...tutorPopupRoute];

@NgModule({
    imports: [IterateSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [TutorComponent, TutorDetailComponent, TutorUpdateComponent, TutorDeleteDialogComponent, TutorDeletePopupComponent],
    entryComponents: [TutorComponent, TutorUpdateComponent, TutorDeleteDialogComponent, TutorDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IterateTutorModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
