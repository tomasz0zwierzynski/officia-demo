import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITutor } from 'app/shared/model/tutor.model';
import { TutorService } from './tutor.service';

@Component({
    selector: 'jhi-tutor-delete-dialog',
    templateUrl: './tutor-delete-dialog.component.html'
})
export class TutorDeleteDialogComponent {
    tutor: ITutor;

    constructor(protected tutorService: TutorService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.tutorService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'tutorListModification',
                content: 'Deleted an tutor'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-tutor-delete-popup',
    template: ''
})
export class TutorDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ tutor }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(TutorDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.tutor = tutor;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/tutor', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/tutor', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
