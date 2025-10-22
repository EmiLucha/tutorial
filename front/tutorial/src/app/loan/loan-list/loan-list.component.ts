import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { LoanEditComponent } from '../loan-edit/loan-edit.component';
import { LoanService } from '../service/loan.service';
import { Loan } from '../model/loan';
import { Pageable } from '../../core/model/page/pageable';
import { DialogConfirmationComponent } from '../../core/dialog-confirmation/dialog-confirmation.component';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { Client } from '../../client/model/client';
import { ClientService } from '../../client/service/client.service';
import { GameService } from '../../game/service/game.service';
import { Game } from '../../game/model/game';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';

@Component({
    selector: 'app-loan-list',
    standalone: true,
    imports: [MatButtonModule, 
            MatIconModule, 
            MatTableModule, 
            CommonModule, 
            MatPaginator, 
            FormsModule,
            MatFormFieldModule,
            MatInputModule,
            MatSelectModule,
            MatDatepickerModule,
            MatNativeDateModule],
    templateUrl: './loan-list.component.html',
    styleUrl: './loan-list.component.scss'
})
export class LoanListComponent implements OnInit {
    pageNumber: number = 0;
    pageSize: number = 5;
    totalElements: number = 0;

    filterClient: Client;
    filterGame: Game;
    filterDate: Date;
    games: Game[];
    clients: Client[];

    dataSource = new MatTableDataSource<Loan>();
    displayedColumns: string[] = ['id', 'game', 'client', 'checkOutDate', 'returnDate', 'action'];

    constructor(private loanService: LoanService, public dialog: MatDialog, private clientService: ClientService, private gameService: GameService) {}

    ngOnInit(): void {
        this.gameService.getAllGames().subscribe((games) => (this.games = games));
        this.clientService.getClients().subscribe((clients) => (this.clients = clients));
        this.loadPage();
    }

    loadPage(event?: PageEvent) {
                
        const gameId = this.filterGame != null ? this.filterGame.id : null;
        const clientId = this.filterClient != null ? this.filterClient.id : null;
        const date = this.filterDate != null ? this.filterDate : null;

        const pageable: Pageable = {
            pageNumber: this.pageNumber,
            pageSize: this.pageSize,
            sort: [
                {
                    property: 'id',
                    direction: 'ASC',
                },
            ],
        };

        if (event != null) {
            pageable.pageSize = event.pageSize;
            pageable.pageNumber = event.pageIndex;
        }

        this.loanService.getLoans(gameId, clientId, date, pageable).subscribe((data) => {
            this.dataSource.data = data.content;
            this.pageNumber = data.pageable.pageNumber;
            this.pageSize = data.pageable.pageSize;
            this.totalElements = data.totalElements;
        });
    }

    onCleanFilter(): void {
        this.filterGame = null;
        this.filterClient = null;
        this.filterDate = null;
        this.onSearch();
    }

    onSearch(): void {
        this.loadPage();
    }

    createLoan() {
        const dialogRef = this.dialog.open(LoanEditComponent, {
            data: {},
        });

        dialogRef.afterClosed().subscribe((result) => {
            this.ngOnInit();
        });
    }

    deleteLoan(loan: Loan) {
        const dialogRef = this.dialog.open(DialogConfirmationComponent, {
            data: {
                title: 'Eliminar préstamo',
                description:
                    'Atención si borra el préstamo se perderán sus datos.<br> ¿Desea eliminar el préstamo?',
            },
        });

        dialogRef.afterClosed().subscribe((result) => {
            if (result) {
                this.loanService.deleteLoan(loan.id).subscribe((result) => {
                    this.ngOnInit();
                });
            }
        });
    }
}