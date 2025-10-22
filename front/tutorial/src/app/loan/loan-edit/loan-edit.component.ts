import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { LoanService } from '../service/loan.service';
import { Loan } from '../model/loan';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Game } from '../../game/model/game';
import { Client } from '../../client/model/client';
import { ClientService } from '../../client/service/client.service';
import { GameService } from '../../game/service/game.service';
import { CommonModule } from '@angular/common';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { DateAdapter, MAT_DATE_FORMATS, MAT_NATIVE_DATE_FORMATS, MatNativeDateModule, NativeDateAdapter } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { differenceInDays } from 'date-fns';

@Component({
  selector: 'app-loan-edit',
  standalone: true,
  imports: [CommonModule,
            FormsModule, 
            ReactiveFormsModule, 
            MatFormFieldModule, 
            MatInputModule,
            MatSelectModule,
            MatDatepickerModule,
            MatNativeDateModule,
            MatButtonModule, 
            MatIconModule, 
            MatTableModule,
            MatDialogModule
          ],
    providers: [
    { provide: DateAdapter, useClass: NativeDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: MAT_NATIVE_DATE_FORMATS },
  ],
  templateUrl: './loan-edit.component.html',
  styleUrl: './loan-edit.component.scss'
})
export class LoanEditComponent implements OnInit{

  loanForm!: FormGroup;
  games: Game[];
  clients: Client[];

    constructor(
        public dialogRef: MatDialogRef<LoanEditComponent>,
        @Inject(MAT_DIALOG_DATA) public data: {loan : Loan},
        private loanService: LoanService,
        private clientService: ClientService,
        private gameService: GameService,
        private fb: FormBuilder
    ) {}

    ngOnInit(): void {
        const loan = this.data.loan ? Object.assign({}, this.data.loan) : new Loan();
        
        this.loanForm = this.fb.group({
          id: [{ value: loan.id, disabled: true }],
          game: [loan.game, [Validators.required]],
          client: [loan.client, [Validators.required]],
          checkOutDate: [loan.checkOutDate, [Validators.required]],
          returnDate: [loan.returnDate, [Validators.required]],
        });

        this.clientService.getClients().subscribe((data) => (this.clients = data));
        this.gameService.getAllGames().subscribe((data) => (this.games = data));

    }

    onSave() {

      if (this.loanForm.invalid) {
        this.loanForm.markAllAsTouched();
        return;
      }

      const loanToSave = this.loanForm.getRawValue();

      // Fecha de recogida tiene que ser anterior a la de la devolución
      if(loanToSave.checkOutDate > loanToSave.returnDate){
          this.loanForm.get('returnDate')?.setErrors({ antesDe: true });
          return;
      } else {
          this.loanForm.get('returnDate')?.setErrors(null);
      }

      // Préstamo ha de ser de 14 días o menos
      if(Math.abs(differenceInDays(loanToSave.checkOutDate, loanToSave.returnDate)) > 14){
          this.loanForm.get('returnDate')?.setErrors({ demasiadosDias: true });
          return;
      } else {
          this.loanForm.get('returnDate')?.setErrors(null);
      }


      this.loanService.saveLoan(loanToSave).subscribe({
        next:() => {
          this.dialogRef.close();
        },
        error: (err) => {
          // El mismo juego no puede estar prestado el mismo día
          if(err.error?.error === "NotAvailable"){
            this.loanForm.get('game')?.setErrors({ exists: true });
          }

          // El mismo cliente no puede tener más de dos juegos el mismo día
          if(err.error?.error === "ManyLoans"){
            this.loanForm.get('client')?.setErrors({ exists: true });
          }
        }
      });  
    }

    onClose() {
        this.dialogRef.close();
    }

}
