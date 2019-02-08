import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITutor } from 'app/shared/model/tutor.model';

@Component({
    selector: 'jhi-tutor-detail',
    templateUrl: './tutor-detail.component.html'
})
export class TutorDetailComponent implements OnInit {
    tutor: ITutor;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ tutor }) => {
            this.tutor = tutor;
        });
    }

    previousState() {
        window.history.back();
    }
}
