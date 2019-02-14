import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { SchedulerComponent } from './scheduler.component';
import { PAGE_ONE_ROUTE } from './page-one/page-one.route';
import { PAGE_TWO_ROUTE } from './page-two/page-two.route';

export const SCHEDULER_ROUTE: Route = {
    path: 'scheduler',
    component: SchedulerComponent,
    data: {
        authorities: ['ROLE_STUDENT'],
        pageTitle: 'scheduler.title'
    },
    canActivate: [UserRouteAccessService],
    children: [PAGE_ONE_ROUTE, PAGE_TWO_ROUTE]
};
