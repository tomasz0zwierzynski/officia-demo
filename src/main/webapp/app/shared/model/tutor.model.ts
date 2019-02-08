import { IUser } from 'app/core/user/user.model';
import { ISubject } from 'app/shared/model/subject.model';

export interface ITutor {
    id?: number;
    fullname?: string;
    user?: IUser;
    subjects?: ISubject[];
}

export class Tutor implements ITutor {
    constructor(public id?: number, public fullname?: string, public user?: IUser, public subjects?: ISubject[]) {}
}
