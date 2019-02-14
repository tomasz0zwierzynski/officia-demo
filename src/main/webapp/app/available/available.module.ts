import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IterateSharedModule } from '../shared';

import { AVAILABLE_ROUTE, AvailableComponent } from './';
import { PageOneComponent } from './page-one/page-one.component';
import { PageTwoComponent } from './page-two/page-two.component';

@NgModule({
    imports: [IterateSharedModule, RouterModule.forRoot([AVAILABLE_ROUTE], { useHash: true })],
    declarations: [AvailableComponent, PageOneComponent, PageTwoComponent],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IterateAppAvailableModule {}
