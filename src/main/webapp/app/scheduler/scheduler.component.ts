import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'jhi-scheduler',
    templateUrl: './scheduler.component.html',
    styleUrls: ['scheduler.component.css']
})
export class SchedulerComponent implements OnInit {
    message: string;

    constructor() {
        this.message = 'SchedulerComponent message';
    }

    ngOnInit() {}
}
