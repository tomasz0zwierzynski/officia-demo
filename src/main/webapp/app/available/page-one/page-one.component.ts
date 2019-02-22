import { Component, OnInit, SystemJsNgModuleLoader, ViewChild, TemplateRef } from '@angular/core';
import { CalendarEvent, CalendarEventAction, CalendarEventTimesChangedEvent, CalendarView } from 'angular-calendar';
import { DAYS_OF_WEEK } from 'angular-calendar';
import { Subject } from 'rxjs';
import { startOfDay, endOfDay, subDays, addDays, endOfMonth, isSameDay, isSameMonth, addHours } from 'date-fns';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { IAvailable } from '../../shared/model/available.model';
import { AvailableService } from '../../entities/available.service';
import { filter, map } from 'rxjs/operators';

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
    @ViewChild('modalContent') modalContent: TemplateRef<any>;

    message: string;

    locale = 'pl';

    weekStartsOn: number = DAYS_OF_WEEK.MONDAY;

    weekendDays: number[] = [DAYS_OF_WEEK.SATURDAY, DAYS_OF_WEEK.SUNDAY];

    availables: IAvailable[];

    view: CalendarView = CalendarView.Month;

    viewDate: Date = new Date();

    modalData: {
        action: string;
        event: CalendarEvent;
    };

    actions: CalendarEventAction[] = [
        {
            label: '<i class="fa fa-fw fa-pencil"></i>',
            onClick: ({ event }: { event: CalendarEvent }): void => {
                this.handleEvent('Edited', event);
            }
        },
        {
            label: '<i class="fa fa-fw fa-times"></i>',
            onClick: ({ event }: { event: CalendarEvent }): void => {
                this.events = this.events.filter(iEvent => iEvent !== event);
                this.handleEvent('Deleted', event);
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

    constructor(private modal: NgbModal, private availableService: AvailableService) {
        console.log('PageOne constructor');
        this.message = 'PageOneComponent message';
    }

    ngOnInit() {}

    dayClicked({ date, events }: { date: Date; events: CalendarEvent[] }): void {
        if (isSameMonth(date, this.viewDate)) {
            this.viewDate = date;
            if ((isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) || events.length === 0) {
                this.activeDayIsOpen = false;
            } else {
                this.activeDayIsOpen = true;
            }
        }
    }

    eventTimesChanged({ event, newStart, newEnd }: CalendarEventTimesChangedEvent): void {
        event.start = newStart;
        event.end = newEnd;
        this.handleEvent('Dropped or resized', event);
        this.refresh.next();
    }

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

        this.availableService
            .query()
            .pipe(
                filter((res: HttpResponse<IAvailable[]>) => res.ok),
                map((res: HttpResponse<IAvailable[]>) => res.body)
            )
            .subscribe(
                (res: IAvailable[]) => {
                    console.log(res);
                    this.availables = res;

                    console.log(this.availables);
                },
                (res: HttpErrorResponse) => console.log(res.message)
            );
    }

    handleEvent(action: string, event: CalendarEvent): void {
        this.modalData = { event, action };
        this.modal.open(this.modalContent, { size: 'lg' });
    }
}
