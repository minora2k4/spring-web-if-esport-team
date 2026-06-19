import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { GameService } from '../../../core/services/game.service';
import { UploadService } from '../../../core/services/upload.service';
import { Game, GameRequest } from '../../../shared/models/api.model';

@Component({
  selector: 'app-manage-games',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './manage-games.component.html',
  styleUrl: './manage-games.component.scss',
})
export class ManageGamesComponent implements OnInit {
  private gameSrv = inject(GameService);
  private uploadSrv = inject(UploadService);

  games = signal<Game[]>([]);
  loading = signal(true);
  saving = signal(false);
  uploading = signal(false);
  error = signal('');

  showForm = signal(false);
  editingId: number | null = null;
  form: GameRequest = { name: '', logoUrl: '' };

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading.set(true);
    this.gameSrv.getAll().subscribe({
      next: (data) => { this.games.set(data); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  openCreate(): void {
    this.editingId = null;
    this.form = { name: '', logoUrl: '' };
    this.error.set('');
    this.showForm.set(true);
  }

  openEdit(g: Game): void {
    this.editingId = g.id;
    this.form = { name: g.name, logoUrl: g.logoUrl };
    this.error.set('');
    this.showForm.set(true);
  }

  close(): void { this.showForm.set(false); }

  onLogo(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (!file) return;
    this.uploading.set(true);
    this.error.set('');
    this.uploadSrv.upload(file).subscribe({
      next: (url) => { this.form.logoUrl = url; this.uploading.set(false); },
      error: (err) => { this.uploading.set(false); this.error.set(err.error?.error || 'Tải ảnh thất bại!'); },
    });
  }

  save(): void {
    if (!this.form.name.trim()) { this.error.set('Vui lòng nhập tên game!'); return; }
    this.saving.set(true);
    const req$ = this.editingId
      ? this.gameSrv.update(this.editingId, this.form)
      : this.gameSrv.create(this.form);
    req$.subscribe({
      next: () => { this.saving.set(false); this.close(); this.load(); },
      error: (err) => { this.saving.set(false); this.error.set(err.error?.error || 'Lưu thất bại!'); },
    });
  }

  remove(g: Game): void {
    if (!confirm(`Xóa game "${g.name}"?`)) return;
    this.gameSrv.delete(g.id).subscribe({ next: () => this.load() });
  }
}
