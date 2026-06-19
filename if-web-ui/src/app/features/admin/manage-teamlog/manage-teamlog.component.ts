import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { TeamlogService } from '../../../core/services/teamlog.service';
import { MemberService } from '../../../core/services/member.service';
import { Member, TeamLog, TeamLogRequest, TeamLogType } from '../../../shared/models/api.model';

@Component({
  selector: 'app-manage-teamlog',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './manage-teamlog.component.html',
  styleUrl: './manage-teamlog.component.scss',
})
export class ManageTeamlogComponent implements OnInit {
  private logSrv = inject(TeamlogService);
  private memberSrv = inject(MemberService);

  logs = signal<TeamLog[]>([]);
  members = signal<Member[]>([]);
  loading = signal(true);
  saving = signal(false);
  error = signal('');

  types: TeamLogType[] = ['JOIN', 'LEAVE', 'ACHIEVEMENT'];

  showForm = signal(false);
  editingId: number | null = null;
  form: TeamLogRequest = this.blank();

  ngOnInit(): void { this.load(); }

  private blank(): TeamLogRequest {
    return { description: '', type: 'JOIN', eventDate: null, memberId: null };
  }

  load(): void {
    this.loading.set(true);
    forkJoin({ logs: this.logSrv.getAll(), members: this.memberSrv.getAll() }).subscribe({
      next: ({ logs, members }) => { this.logs.set(logs); this.members.set(members); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  openCreate(): void { this.editingId = null; this.form = this.blank(); this.error.set(''); this.showForm.set(true); }

  openEdit(l: TeamLog): void {
    this.editingId = l.id;
    this.form = { description: l.description, type: l.type, eventDate: l.eventDate, memberId: l.member?.id ?? null };
    this.error.set('');
    this.showForm.set(true);
  }

  close(): void { this.showForm.set(false); }

  label(t: string): string { return t === 'JOIN' ? 'Gia nhập' : t === 'LEAVE' ? 'Rời đội' : 'Thành tích'; }
  badgeClass(t: string): string { return t === 'JOIN' ? 'badge-join' : t === 'LEAVE' ? 'badge-leave' : 'badge-achievement'; }

  save(): void {
    if (!this.form.description.trim()) { this.error.set('Vui lòng nhập nội dung!'); return; }
    this.saving.set(true);
    const req$ = this.editingId
      ? this.logSrv.update(this.editingId, this.form)
      : this.logSrv.create(this.form);
    req$.subscribe({
      next: () => { this.saving.set(false); this.close(); this.load(); },
      error: (err) => { this.saving.set(false); this.error.set(err.error?.error || 'Lưu thất bại!'); },
    });
  }

  remove(l: TeamLog): void {
    if (!confirm('Xóa nhật ký này?')) return;
    this.logSrv.delete(l.id).subscribe({ next: () => this.load() });
  }
}
