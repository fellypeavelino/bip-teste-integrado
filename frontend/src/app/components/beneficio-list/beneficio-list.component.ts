import { Component, OnInit } from '@angular/core';
import { Beneficio } from '../../models/beneficio.model';
import { BeneficioService } from '../../services/beneficio.service';

@Component({
  selector: 'app-beneficio-list',
  templateUrl: './beneficio-list.component.html',
  styleUrls: ['./beneficio-list.component.css']
})
export class BeneficioListComponent implements OnInit {
  beneficios: Beneficio[] = [];
  loading = false;
  error: string | null = null;

  constructor(private beneficioService: BeneficioService) { }

  ngOnInit(): void {
    this.loadBeneficios();
  }

  loadBeneficios(): void {
    this.loading = true;
    this.error = null;
    
    this.beneficioService.getAll().subscribe({
      next: (data) => {
        this.beneficios = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = err.message;
        this.loading = false;
      }
    });
  }

  deleteBeneficio(id: number): void {
    if (confirm('Tem certeza que deseja excluir este benefício?')) {
      this.beneficioService.delete(id).subscribe({
        next: () => {
          this.loadBeneficios();
        },
        error: (err) => {
          this.error = err.message;
        }
      });
    }
  }
}