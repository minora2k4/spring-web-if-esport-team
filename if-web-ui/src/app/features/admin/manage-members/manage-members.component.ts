import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { MemberService } from '../../../core/services/member.service';
import { GameService } from '../../../core/services/game.service';
import { UploadService } from '../../../core/services/upload.service';
import { Game, Member, MemberRequest } from '../../../shared/models/api.model';

@Component({
  selector: 'app-manage-members',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './manage-members.component.html',
  styleUrl: './manage-members.component.scss',
})
export class ManageMembersComponent implements OnInit {
  private memberSrv = inject(MemberService);
  private gameSrv = inject(GameService);
  private uploadSrv = inject(UploadService);

  members = signal<Member[]>([]);
  games = signal<Game[]>([]);
  loading = signal(true);
  saving = signal(false);
  uploading = signal(false);
  error = signal('');

  showForm = signal(false);
  editingId: number | null = null;
  form: MemberRequest = this.blank();

  ngOnInit(): void { this.load(); }

  private blank(): MemberRequest {
    return { inGameName: '', realName: '', role: '', achievement: '', avatarUrl: '', isActive: true, gameIds: [] };
  }

  load(): void {
    this.loading.set(true);
    forkJoin({ members: this.memberSrv.getAll(), games: this.gameSrv.getAll() }).subscribe({
      next: ({ members, games }) => { this.members.set(members); this.games.set(games); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  openCreate(): void { this.editingId = null; this.form = this.blank(); this.error.set(''); this.showForm.set(true); }

  openEdit(m: Member): void {
    this.editingId = m.id;
    this.form = {
      inGameName: m.inGameName, realName: m.realName, role: m.role,
      achievement: m.achievement, avatarUrl: m.avatarUrl, isActive: m.isActive,
      gameIds: (m.games || []).map((g) => g.id),
    };
    this.error.set('');
    this.showForm.set(true);
  }

  close(): void { this.showForm.set(false); }

  toggleGame(id: number): void {
    const ids = this.form.gameIds || [];
    this.form.gameIds = ids.includes(id) ? ids.filter((x) => x !== id) : [...ids, id];
  }
  hasGame(id: number): boolean { return (this.form.gameIds || []).includes(id); }

  onAvatar(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (!file) return;
    this.uploading.set(true);
    this.error.set('');
    this.uploadSrv.upload(file).subscribe({
      next: (url) => { this.form.avatarUrl = url; this.uploading.set(false); },
      error: (err) => { this.uploading.set(false); this.error.set(err.error?.error || 'Tải ảnh thất bại!'); },
    });
  }

  save(): void {
    if (!this.form.inGameName.trim()) { this.error.set('Vui lòng nhập tên trong game!'); return; }
    this.saving.set(true);
    const req$ = this.editingId
      ? this.memberSrv.update(this.editingId, this.form)
      : this.memberSrv.create(this.form);
    req$.subscribe({
      next: () => { this.saving.set(false); this.close(); this.load(); },
      error: (err) => { this.saving.set(false); this.error.set(err.error?.error || 'Lưu thất bại!'); },
    });
  }

  remove(m: Member): void {
    if (!confirm(`Xóa thành viên "${m.inGameName}"?`)) return;
    this.memberSrv.delete(m.id).subscribe({ next: () => this.load() });
  }
}
