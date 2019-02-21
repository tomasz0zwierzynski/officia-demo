import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import localePl from '@angular/common/locales/pl';

import { IterateSharedModule } from '../shared';
import {
    CalendarModule,
    DateFormatterParams,
    DateAdapter,
    CalendarNativeDateFormatter,
    CalendarDateFormatter,
    CalendarEventTitleFormatter
} from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';

import { AVAILABLE_ROUTE, AvailableComponent } from './';
import { PageOneComponent } from './page-one/page-one.component';
import { PageTwoComponent } from './page-two/page-two.component';
import { registerLocaleData } from '@angular/common';

registerLocaleData(localePl);

class CustomDateFormatter extends CalendarNativeDateFormatter {
    public dayViewHour({ date, locale }: DateFormatterParams): string {
        return new Intl.DateTimeFormat(locale, { hour: 'numeric' }).format(date);
    }
}

@NgModule({
    imports: [
        IterateSharedModule,
        RouterModule.forRoot([AVAILABLE_ROUTE], { useHash: true }),
        BrowserAnimationsModule,
        CalendarModule.forRoot({
            provide: DateAdapter,
            useFactory: adapterFactory
        })
    ],
    declarations: [AvailableComponent, PageOneComponent, PageTwoComponent],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IterateAppAvailableModule {}
