import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { AvailableComponent } from './available.component';
import { PAGE_ONE_ROUTE } from './page-one/page-one.route';
import { PAGE_TWO_ROUTE } from './page-two/page-two.route';

export const AVAILABLE_ROUTE: Route = {
    path: 'available',
    component: AvailableComponent,
    data: {
        authorities: ['ROLE_TUTOR'],
        pageTitle: 'available.title'
    },
    canActivate: [UserRouteAccessService],
    children: [PAGE_ONE_ROUTE, PAGE_TWO_ROUTE]
};
