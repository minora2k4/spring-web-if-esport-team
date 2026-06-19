import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { TournamentService } from '../../../core/services/tournament.service';
import { GameService } from '../../../core/services/game.service';
import { Game, Tournament, TournamentRequest } from '../../../shared/models/api.model';

@Component({
  selector: 'app-manage-tournaments',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './manage-tournaments.component.html',
  styleUrl: './manage-tournaments.component.scss',
})
export class ManageTournamentsComponent implements OnInit {
  private tournamentSrv = inject(TournamentService);
  private gameSrv = inject(GameService);

  tournaments = signal<Tournament[]>([]);
  games = signal<Game[]>([]);
  loading = signal(true);
  saving = signal(false);
  error = signal('');

  showForm = signal(false);
  editingId: number | null = null;
  form: TournamentRequest = this.blank();

  ngOnInit(): void { this.load(); }

  private blank(): TournamentRequest {
    return { name: '', achievement: '', startDate: null, gameId: 0 };
  }

  load(): void {
    this.loading.set(true);
    forkJoin({ tournaments: this.tournamentSrv.getAll(), games: this.gameSrv.getAll() }).subscribe({
      next: ({ tournaments, games }) => { this.tournaments.set(tournaments); this.games.set(games); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  openCreate(): void {
    this.editingId = null;
    this.form = this.blank();
    this.form.gameId = this.games()[0]?.id ?? 0;
    this.error.set('');
    this.showForm.set(true);
  }

  openEdit(t: Tournament): void {
    this.editingId = t.id;
    this.form = { name: t.name, achievement: t.achievement, startDate: t.startDate, gameId: t.game?.id ?? 0 };
    this.error.set('');
    this.showForm.set(true);
  }

  close(): void { this.showForm.set(false); }

  save(): void {
    if (!this.form.name.trim()) { this.error.set('Vui lòng nhập tên giải đấu!'); return; }
    if (!this.form.gameId) { this.error.set('Vui lòng chọn tựa game!'); return; }
    this.saving.set(true);
    const req$ = this.editingId
      ? this.tournamentSrv.update(this.editingId, this.form)
      : this.tournamentSrv.create(this.form);
    req$.subscribe({
      next: () => { this.saving.set(false); this.close(); this.load(); },
      error: (err) => { this.saving.set(false); this.error.set(err.error?.error || 'Lưu thất bại!'); },
    });
  }

  remove(t: Tournament): void {
    if (!confirm(`Xóa giải đấu "${t.name}"?`)) return;
    this.tournamentSrv.delete(t.id).subscribe({ next: () => this.load() });
  }
}
