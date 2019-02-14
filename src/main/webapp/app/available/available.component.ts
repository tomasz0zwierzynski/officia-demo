import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'jhi-available',
    templateUrl: './available.component.html',
    styleUrls: ['available.component.css']
})
export class AvailableComponent implements OnInit {
    message: string;

    constructor() {
        this.message = 'AvailableComponent message';
    }

    ngOnInit() {}
}
