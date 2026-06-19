import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { OffteamService } from '../../../core/services/offteam.service';
import { UploadService } from '../../../core/services/upload.service';
import { OffteamEvent, OffteamEventRequest } from '../../../shared/models/api.model';

@Component({
  selector: 'app-manage-offteam',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './manage-offteam.component.html',
  styleUrl: './manage-offteam.component.scss',
})
export class ManageOffteamComponent implements OnInit {
  private offteamSrv = inject(OffteamService);
  private uploadSrv = inject(UploadService);

  events = signal<OffteamEvent[]>([]);
  loading = signal(true);
  saving = signal(false);
  uploading = signal(false);
  error = signal('');

  showForm = signal(false);
  editingId: number | null = null;
  form: OffteamEventRequest = this.blank();
  photos = signal<string[]>([]);

  ngOnInit(): void { this.load(); }

  private blank(): OffteamEventRequest {
    return { title: '', eventDate: null, coverPhotoUrl: '', photoUrls: [] };
  }

  load(): void {
    this.loading.set(true);
    this.offteamSrv.getAll().subscribe({
      next: (data) => { this.events.set(data); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  openCreate(): void {
    this.editingId = null;
    this.form = this.blank();
    this.photos.set([]);
    this.error.set('');
    this.showForm.set(true);
  }

  openEdit(e: OffteamEvent): void {
    this.editingId = e.id;
    this.form = { title: e.title, eventDate: e.eventDate, coverPhotoUrl: e.coverPhotoUrl, photoUrls: e.photoUrls || [] };
    this.photos.set([...(e.photoUrls || [])]);
    this.error.set('');
    this.showForm.set(true);
  }

  close(): void { this.showForm.set(false); }

  onCover(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (!file) return;
    this.uploading.set(true);
    this.error.set('');
    this.uploadSrv.upload(file).subscribe({
      next: (url) => { this.form.coverPhotoUrl = url; this.uploading.set(false); },
      error: (err) => { this.uploading.set(false); this.error.set(err.error?.error || 'Tải ảnh thất bại!'); },
    });
  }

  onPhotos(event: Event): void {
    const files = (event.target as HTMLInputElement).files;
    if (!files?.length) return;
    this.uploading.set(true);
    this.error.set('');
    forkJoin(Array.from(files).map((f) => this.uploadSrv.upload(f))).subscribe({
      next: (urls) => { this.photos.update((cur) => [...cur, ...urls]); this.uploading.set(false); },
      error: (err) => { this.uploading.set(false); this.error.set(err.error?.error || 'Tải ảnh thất bại!'); },
    });
  }

  removePhoto(url: string): void {
    this.photos.update((cur) => cur.filter((p) => p !== url));
  }

  save(): void {
    if (!this.form.title.trim()) { this.error.set('Vui lòng nhập tiêu đề!'); return; }
    this.form.photoUrls = this.photos();
    this.saving.set(true);
    const req$ = this.editingId
      ? this.offteamSrv.update(this.editingId, this.form)
      : this.offteamSrv.create(this.form);
    req$.subscribe({
      next: () => { this.saving.set(false); this.close(); this.load(); },
      error: (err) => { this.saving.set(false); this.error.set(err.error?.error || 'Lưu thất bại!'); },
    });
  }

  remove(e: OffteamEvent): void {
    if (!confirm(`Xóa sự kiện "${e.title}"?`)) return;
    this.offteamSrv.delete(e.id).subscribe({ next: () => this.load() });
  }
}
