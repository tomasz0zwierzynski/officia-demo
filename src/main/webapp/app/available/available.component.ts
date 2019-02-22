import { Component, OnInit, ViewChild, TemplateRef } from '@angular/core';
import { DAYS_OF_WEEK, CalendarView, CalendarEvent, CalendarEventAction, CalendarEventTimesChangedEvent } from 'angular-calendar';
import { IAvailable, Available } from '../shared/model/available.model';
import { Subject } from 'rxjs';
import { subDays, startOfDay, addDays, addHours, isSameMonth, isSameDay, addMinutes } from 'date-fns';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AvailableService } from '../entities/available.service';
import { filter, map } from 'rxjs/operators';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

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
    },
    green: {
        primary: '#78C0A8',
        secondary: '#4f8774'
    }
};

@Component({
    selector: 'jhi-available',
    templateUrl: './available.component.html',
    styleUrls: ['available.component.css']
})
export class AvailableComponent implements OnInit {
    @ViewChild('modalContent') modalContent: TemplateRef<any>;

    message: string;

    locale = 'pl';

    weekStartsOn: number = DAYS_OF_WEEK.MONDAY;

    weekendDays: number[] = [DAYS_OF_WEEK.SATURDAY, DAYS_OF_WEEK.SUNDAY];

    availables: IAvailable[];

    view: CalendarView = CalendarView.Month;

    // musi być - trik żeby widzieć tego enuma w html
    CalendarView = CalendarView;

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

    defaultFrom = { hour: 16, minute: 0 };
    defaultTo = { hour: 20, minute: 0 };

    refresh: Subject<any> = new Subject();

    events: CalendarEvent[] = [];

    activeDayIsOpen = false;

    currentAvailableDay = new Date();

    constructor(private modal: NgbModal, private availableService: AvailableService) {
        console.log('PageOne constructor');
        this.message = 'PageOneComponent message';
        this.currentAvailableDay.setHours(0, 0, 0, 0);
        this.loadAvailables();
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
    }

    handleEvent(action: string, event: CalendarEvent): void {
        this.modalData = { event, action };
        // this.modal.open(this.modalContent, { size: 'lg' });
    }

    addAvailableRegion() {
        this.events.push({
            start: addMinutes(this.currentAvailableDay, this.defaultFrom.hour * 60 + this.defaultFrom.minute),
            end: addMinutes(this.currentAvailableDay, this.defaultTo.hour * 60 + this.defaultTo.minute),
            title: 'Available time',
            color: colors.green,
            actions: this.actions,
            allDay: false,
            resizable: {
                beforeStart: true,
                afterEnd: true
            },
            draggable: true
        });

        console.log(this.currentAvailableDay);
        console.log(this.events);

        this.currentAvailableDay = addDays(this.currentAvailableDay, 1);

        this.refresh.next();
    }

    loadAvailables() {
        this.availableService
            .query()
            .pipe(
                filter((res: HttpResponse<IAvailable[]>) => res.ok),
                map((res: HttpResponse<IAvailable[]>) => res.body)
            )
            .subscribe(
                (res: IAvailable[]) => {
                    this.availables = res;
                    console.log('Available received: ' + res);
                    this.availables.forEach(available => {
                        this.events.push({
                            start: new Date(addHours(available.begin, 1)),
                            end: new Date(addHours(available.end, 1)),
                            title: 'Free time',
                            color: colors.green,
                            actions: this.actions,
                            allDay: false,
                            resizable: {
                                beforeStart: true,
                                afterEnd: true
                            },
                            draggable: true
                        });
                    });

                    console.log(this.events);
                    this.refresh.next();
                },
                (res: HttpErrorResponse) => console.log(res.message)
            );
    }

    saveAvailables() {
        console.log('Saving availables...');
        console.log(this.events);
        this.events.forEach(event => {
            console.log('http request');
            this.availableService.create(new Available(null, event.start, event.end, null)).subscribe(res => {
                this.availables.forEach(available => {
                    this.availableService.delete(available.id).subscribe(resource => {
                        this.events = [];
                        this.loadAvailables();
                    });
                });
            });
        });
    }
}
