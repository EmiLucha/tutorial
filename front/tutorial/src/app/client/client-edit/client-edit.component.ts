import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ClientService } from '../service/client.service';
import { Client } from '../model/client';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
    selector: 'app-client-edit',
    standalone: true,
    imports: [FormsModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule ],
    templateUrl: './client-edit.component.html',
    styleUrl: './client-edit.component.scss'
})
export class ClientEditComponent implements OnInit {

    clientForm!: FormGroup;

    constructor(
        public dialogRef: MatDialogRef<ClientEditComponent>,
        @Inject(MAT_DIALOG_DATA) public data: {client : Client},
        private clientService: ClientService,
        private fb: FormBuilder
    ) {}

    ngOnInit(): void {
        const client = this.data.client ? Object.assign({}, this.data.client) : new Client();
        
        this.clientForm = this.fb.group({
          id: [{ value: client.id, disabled: true }],
          name: [client.name, [Validators.required]]
        });
    }

    onSave() {

      if (this.clientForm.invalid) {
        this.clientForm.markAllAsTouched();
        return;
      }

      const clientToSave = this.clientForm.getRawValue();

      this.clientService.saveClient(clientToSave).subscribe({
        next:() => {
          this.dialogRef.close();
        },
        error: (err) => {
          if(err.status === 409){
            this.clientForm.get('name')?.setErrors({ exists: true });
          }
        }
      });  
    }

    onClose() {
        this.dialogRef.close();
    }


} 