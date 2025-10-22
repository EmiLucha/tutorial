import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Pageable } from '../../core/model/page/pageable';
import { Loan } from '../model/loan';
import { LoanPage } from '../model/loan-page';
import { HttpClient } from '@angular/common/http';
@Injectable({
    providedIn: 'root',
})
export class LoanService {
    constructor(private http: HttpClient) {}

    private baseUrl = 'http://localhost:8080/loan';

    getLoans(gameId?: number, clientId?: number, date?: Date, pageable?: Pageable): Observable<LoanPage> {
        return this.http.post<LoanPage>(this.composeFindUrl(gameId, clientId, date), { pageable: pageable });
    }

    private composeFindUrl(gameId?: number, clientId?: number, date?: Date): string {
        const params = new URLSearchParams();
        if (gameId) {
            params.set('idGame', gameId.toString());
        }
        if (clientId) {
            params.set('idClient', clientId.toString());
        }
        if (date) {
            params.set('date', date.toString());
        }
        const queryString = params.toString();
        return queryString ? `${this.baseUrl}?${queryString}` : this.baseUrl;
    }

    saveLoan(loan: Loan): Observable<Loan> {
        const { id } = loan;
        const url = id ? `${this.baseUrl}/${id}` : this.baseUrl;
        return this.http.put<Loan>(url, loan);
    }

    deleteLoan(idLoan: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${idLoan}`);
    }
}