import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Tutor } from 'app/shared/model/tutor.model';
import { TutorService } from './tutor.service';
import { TutorComponent } from './tutor.component';
import { TutorDetailComponent } from './tutor-detail.component';
import { TutorUpdateComponent } from './tutor-update.component';
import { TutorDeletePopupComponent } from './tutor-delete-dialog.component';
import { ITutor } from 'app/shared/model/tutor.model';

@Injectable({ providedIn: 'root' })
export class TutorResolve implements Resolve<ITutor> {
    constructor(private service: TutorService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITutor> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Tutor>) => response.ok),
                map((tutor: HttpResponse<Tutor>) => tutor.body)
            );
        }
        return of(new Tutor());
    }
}

export const tutorRoute: Routes = [
    {
        path: '',
        component: TutorComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'iterateApp.tutor.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: TutorDetailComponent,
        resolve: {
            tutor: TutorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'iterateApp.tutor.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: TutorUpdateComponent,
        resolve: {
            tutor: TutorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'iterateApp.tutor.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: TutorUpdateComponent,
        resolve: {
            tutor: TutorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'iterateApp.tutor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const tutorPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: TutorDeletePopupComponent,
        resolve: {
            tutor: TutorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'iterateApp.tutor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
