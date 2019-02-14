import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IterateSharedModule } from '../shared';

import { SCHEDULER_ROUTE, SchedulerComponent } from './';
import { PageOneComponent } from './page-one/page-one.component';
import { PageTwoComponent } from './page-two/page-two.component';

@NgModule({
    imports: [IterateSharedModule, RouterModule.forRoot([SCHEDULER_ROUTE], { useHash: true })],
    declarations: [SchedulerComponent, PageOneComponent, PageTwoComponent],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IterateAppSchedulerModule {}
