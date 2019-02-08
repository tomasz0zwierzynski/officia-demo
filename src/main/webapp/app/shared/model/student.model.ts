import { IUser } from 'app/core/user/user.model';

export interface IStudent {
    id?: number;
    fullname?: string;
    user?: IUser;
}

export class Student implements IStudent {
    constructor(public id?: number, public fullname?: string, public user?: IUser) {}
}
