import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ITutor } from 'app/shared/model/tutor.model';
import { AccountService } from 'app/core';
import { TutorService } from './tutor.service';

@Component({
    selector: 'jhi-tutor',
    templateUrl: './tutor.component.html'
})
export class TutorComponent implements OnInit, OnDestroy {
    tutors: ITutor[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected tutorService: TutorService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.tutorService
            .query()
            .pipe(
                filter((res: HttpResponse<ITutor[]>) => res.ok),
                map((res: HttpResponse<ITutor[]>) => res.body)
            )
            .subscribe(
                (res: ITutor[]) => {
                    this.tutors = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInTutors();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ITutor) {
        return item.id;
    }

    registerChangeInTutors() {
        this.eventSubscriber = this.eventManager.subscribe('tutorListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
