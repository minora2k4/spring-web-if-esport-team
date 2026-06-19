import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TeamlogService } from '../../core/services/teamlog.service';
import { TeamLog } from '../../shared/models/api.model';

@Component({
  selector: 'app-team-log',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './team-log.component.html',
  styleUrl: './team-log.component.scss',
})
export class TeamLogComponent implements OnInit {
  private logSrv = inject(TeamlogService);

  logs = signal<TeamLog[]>([]);
  loading = signal(true);

  ngOnInit(): void {
    this.logSrv.getAll().subscribe({
      next: (data) => { this.logs.set(data); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  icon(type: string): string {
    return type === 'JOIN' ? '🟢' : type === 'LEAVE' ? '🔴' : '🏅';
  }
  badgeClass(type: string): string {
    return type === 'JOIN' ? 'badge-join' : type === 'LEAVE' ? 'badge-leave' : 'badge-achievement';
  }
  label(type: string): string {
    return type === 'JOIN' ? 'Gia nhập' : type === 'LEAVE' ? 'Rời đội' : 'Thành tích';
  }
}
