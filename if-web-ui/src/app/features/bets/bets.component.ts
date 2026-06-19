import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BetService } from '../../core/services/bet.service';
import { Bet } from '../../shared/models/api.model';

@Component({
  selector: 'app-bets',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './bets.component.html',
  styleUrl: './bets.component.scss',
})
export class BetsComponent implements OnInit {
  private betSrv = inject(BetService);

  bets = signal<Bet[]>([]);
  loading = signal(true);

  wins = computed(() => this.bets().filter((b) => b.result === 'WIN').length);
  losses = computed(() => this.bets().filter((b) => b.result === 'LOSE').length);
  pending = computed(() => this.bets().filter((b) => b.result === 'PENDING').length);

  ngOnInit(): void {
    this.betSrv.getAll().subscribe({
      next: (data) => { this.bets.set(data); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  badgeClass(r: string): string {
    return r === 'WIN' ? 'badge-win' : r === 'LOSE' ? 'badge-lose' : 'badge-pending';
  }
  label(r: string): string {
    return r === 'WIN' ? 'Thắng' : r === 'LOSE' ? 'Thua' : 'Đang chờ';
  }
}
