import { ITutor } from 'app/shared/model/tutor.model';

export interface ISubject {
    id?: number;
    name?: string;
    tutors?: ITutor[];
}

export class Subject implements ISubject {
    constructor(public id?: number, public name?: string, public tutors?: ITutor[]) {}
}
