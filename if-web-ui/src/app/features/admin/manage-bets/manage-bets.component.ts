import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { BetService } from '../../../core/services/bet.service';
import { GameService } from '../../../core/services/game.service';
import { Bet, BetRequest, BetResult, Game } from '../../../shared/models/api.model';

@Component({
  selector: 'app-manage-bets',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './manage-bets.component.html',
  styleUrl: './manage-bets.component.scss',
})
export class ManageBetsComponent implements OnInit {
  private betSrv = inject(BetService);
  private gameSrv = inject(GameService);

  bets = signal<Bet[]>([]);
  games = signal<Game[]>([]);
  loading = signal(true);
  saving = signal(false);
  error = signal('');

  results: BetResult[] = ['WIN', 'LOSE', 'PENDING'];

  showForm = signal(false);
  editingId: number | null = null;
  form: BetRequest = this.blank();

  ngOnInit(): void { this.load(); }

  private blank(): BetRequest {
    return { opponent: '', result: 'PENDING', amount: null, betDate: null, gameId: 0 };
  }

  load(): void {
    this.loading.set(true);
    forkJoin({ bets: this.betSrv.getAll(), games: this.gameSrv.getAll() }).subscribe({
      next: ({ bets, games }) => { this.bets.set(bets); this.games.set(games); this.loading.set(false); },
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

  openEdit(b: Bet): void {
    this.editingId = b.id;
    this.form = { opponent: b.opponent, result: b.result, amount: b.amount, betDate: b.betDate, gameId: b.game?.id ?? 0 };
    this.error.set('');
    this.showForm.set(true);
  }

  close(): void { this.showForm.set(false); }

  label(r: string): string { return r === 'WIN' ? 'Thắng' : r === 'LOSE' ? 'Thua' : 'Đang chờ'; }
  badgeClass(r: string): string { return r === 'WIN' ? 'badge-win' : r === 'LOSE' ? 'badge-lose' : 'badge-pending'; }

  save(): void {
    if (!this.form.opponent.trim()) { this.error.set('Vui lòng nhập tên đối thủ!'); return; }
    if (!this.form.gameId) { this.error.set('Vui lòng chọn tựa game!'); return; }
    this.saving.set(true);
    const req$ = this.editingId
      ? this.betSrv.update(this.editingId, this.form)
      : this.betSrv.create(this.form);
    req$.subscribe({
      next: () => { this.saving.set(false); this.close(); this.load(); },
      error: (err) => { this.saving.set(false); this.error.set(err.error?.error || 'Lưu thất bại!'); },
    });
  }

  remove(b: Bet): void {
    if (!confirm(`Xóa kèo với "${b.opponent}"?`)) return;
    this.betSrv.delete(b.id).subscribe({ next: () => this.load() });
  }
}
