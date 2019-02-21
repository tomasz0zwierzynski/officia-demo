import { Component, OnInit, SystemJsNgModuleLoader } from '@angular/core';
import { CalendarEvent, CalendarEventAction, CalendarEventTimesChangedEvent, CalendarView } from 'angular-calendar';
import { DAYS_OF_WEEK } from 'angular-calendar';
import { Subject } from 'rxjs';
import { startOfDay, endOfDay, subDays, addDays, endOfMonth, isSameDay, isSameMonth, addHours } from 'date-fns';

const colors: any = {
    red: {
        primary: '#ad2121',
        secondary: '#FAE3E3'
    },
    blue: {
        primary: '#1e90ff',
        secondary: '#D1E8FF'
    },
    yellow: {
        primary: '#e3bc08',
        secondary: '#FDF1BA'
    }
};

@Component({
    selector: 'jhi-page-one',
    templateUrl: './page-one.component.html',
    styleUrls: ['page-one.component.css']
})
export class PageOneComponent implements OnInit {
    message: string;

    locale = 'pl';

    weekStartsOn: number = DAYS_OF_WEEK.MONDAY;

    weekendDays: number[] = [DAYS_OF_WEEK.SATURDAY, DAYS_OF_WEEK.SUNDAY];

    viewDate: Date = new Date();

    modalData: {
        action: string;
        event: CalendarEvent;
    };

    actions: CalendarEventAction[] = [
        {
            label: '<i class="fa fa-fw fa-pencil"></i>',
            onClick: ({ event }: { event: CalendarEvent }): void => {
                // this.handleEvent('Edited', event);
            }
        },
        {
            label: '<i class="fa fa-fw fa-times"></i>',
            onClick: ({ event }: { event: CalendarEvent }): void => {
                this.events = this.events.filter(iEvent => iEvent !== event);
                // this.handleEvent('Deleted', event);
            }
        }
    ];

    refresh: Subject<any> = new Subject();

    events: CalendarEvent[] = [
        {
            start: subDays(startOfDay(new Date()), 1),
            end: addDays(new Date(), 1),
            title: 'A 3 day event',
            color: colors.red,
            actions: this.actions,
            allDay: true,
            resizable: {
                beforeStart: true,
                afterEnd: true
            },
            draggable: true
        },
        {
            start: startOfDay(new Date()),
            title: 'An event with no end date',
            color: colors.yellow,
            actions: this.actions
        },
        {
            start: subDays(endOfMonth(new Date()), 3),
            end: addDays(endOfMonth(new Date()), 3),
            title: 'A long event that spans 2 months',
            color: colors.blue,
            allDay: true
        },
        {
            start: addHours(startOfDay(new Date()), 2),
            end: new Date(),
            title: 'A draggable and resizable event',
            color: colors.yellow,
            actions: this.actions,
            resizable: {
                beforeStart: true,
                afterEnd: true
            },
            draggable: true
        },
        {
            start: addHours(new Date(), 16),
            end: addHours(new Date(), 20),
            title: 'Spawned event',
            color: colors.red,
            actions: this.actions,
            allDay: false,
            resizable: {
                beforeStart: true,
                afterEnd: true
            },
            draggable: true
        }
    ];

    activeDayIsOpen = true;

    constructor() {
        console.log('PageOne constructor');
        this.message = 'PageOneComponent message';
    }

    ngOnInit() {}

    onSpawnEvent(event) {
        console.log('spawnEvent');

        this.events.push({
            start: addHours(new Date(), 16),
            end: addHours(new Date(), 20),
            title: 'Spawned event',
            color: colors.red,
            actions: this.actions,
            allDay: false,
            resizable: {
                beforeStart: true,
                afterEnd: true
            },
            draggable: true
        });

        this.refresh.next();
    }
}
