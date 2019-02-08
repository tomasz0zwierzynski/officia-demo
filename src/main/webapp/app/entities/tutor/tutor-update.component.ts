import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ITutor } from 'app/shared/model/tutor.model';
import { TutorService } from './tutor.service';
import { IUser, UserService } from 'app/core';
import { ISubject } from 'app/shared/model/subject.model';
import { SubjectService } from 'app/entities/subject';

@Component({
    selector: 'jhi-tutor-update',
    templateUrl: './tutor-update.component.html'
})
export class TutorUpdateComponent implements OnInit {
    tutor: ITutor;
    isSaving: boolean;

    users: IUser[];

    subjects: ISubject[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected tutorService: TutorService,
        protected userService: UserService,
        protected subjectService: SubjectService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ tutor }) => {
            this.tutor = tutor;
        });
        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.subjectService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ISubject[]>) => mayBeOk.ok),
                map((response: HttpResponse<ISubject[]>) => response.body)
            )
            .subscribe((res: ISubject[]) => (this.subjects = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.tutor.id !== undefined) {
            this.subscribeToSaveResponse(this.tutorService.update(this.tutor));
        } else {
            this.subscribeToSaveResponse(this.tutorService.create(this.tutor));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ITutor>>) {
        result.subscribe((res: HttpResponse<ITutor>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackSubjectById(index: number, item: ISubject) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}
