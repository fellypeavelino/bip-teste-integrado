import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Beneficio } from '../../models/beneficio.model';
import { BeneficioService } from '../../services/beneficio.service';

@Component({
  selector: 'app-beneficio-form',
  templateUrl: './beneficio-form.component.html',
  styleUrls: ['./beneficio-form.component.css']
})
export class BeneficioFormComponent implements OnInit {
  beneficio: Beneficio = {
    nome: '',
    descricao: '',
    valor: 0,
    ativo: true
  };
  isEditMode = false;
  loading = false;
  error: string | null = null;
  successMessage: string | null = null;

  constructor(
    private beneficioService: BeneficioService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.loadBeneficio(+id);
    }
  }

  loadBeneficio(id: number): void {
    this.loading = true;
    this.beneficioService.getById(id).subscribe({
      next: (data) => {
        this.beneficio = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = err.message;
        this.loading = false;
      }
    });
  }

  onSubmit(): void {
    this.loading = true;
    this.error = null;
    this.successMessage = null;

    if (this.isEditMode && this.beneficio.id) {
      this.beneficioService.update(this.beneficio.id, this.beneficio).subscribe({
        next: () => {
          this.successMessage = 'Benefício atualizado com sucesso!';
          this.loading = false;
          setTimeout(() => this.router.navigate(['/']), 1500);
        },
        error: (err) => {
          this.error = err.message;
          this.loading = false;
        }
      });
    } else {
      this.beneficioService.create(this.beneficio).subscribe({
        next: () => {
          this.successMessage = 'Benefício criado com sucesso!';
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
}