import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Beneficio } from '../../models/beneficio.model';
import { TransferenciaRequest } from '../../models/transferencia.model';
import { BeneficioService } from '../../services/beneficio.service';

@Component({
  selector: 'app-transferencia',
  templateUrl: './transferencia.component.html',
  styleUrls: ['./transferencia.component.css']
})
export class TransferenciaComponent implements OnInit {
  beneficios: Beneficio[] = [];
  transferencia: TransferenciaRequest = {
    origemId: 0,
    destinoId: 0,
    valor: 0
  };
  loading = false;
  loadingBeneficios = false;
  error: string | null = null;
  successMessage: string | null = null;

  constructor(
    private beneficioService: BeneficioService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadBeneficios();
  }

  loadBeneficios(): void {
    this.loadingBeneficios = true;
    this.beneficioService.getAll().subscribe({
      next: (data) => {
        this.beneficios = data.filter(b => b.ativo);
        this.loadingBeneficios = false;
      },
      error: (err) => {
        this.error = err.message;
        this.loadingBeneficios = false;
      }
    });
  }

  onSubmit(): void {
    if (this.transferencia.origemId === this.transferencia.destinoId) {
      this.error = 'Origem e destino não podem ser iguais';
      return;
    }

    if (this.transferencia.valor <= 0) {
      this.error = 'Valor deve ser maior que zero';
      return;
    }

    this.loading = true;
    this.error = null;
    this.successMessage = null;

    this.beneficioService.transferir(this.transferencia).subscribe({
      next: () => {
        this.successMessage = 'Transferência realizada com sucesso!';
        this.loading = false;
        setTimeout(() => this.router.navigate(['/']), 1500);
      },
      error: (err) => {
        this.error = err.message;
        this.loading = false;
      }
    });
  }
}