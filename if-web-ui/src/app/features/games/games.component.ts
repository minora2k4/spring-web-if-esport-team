import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GameService } from '../../core/services/game.service';
import { Game } from '../../shared/models/api.model';

@Component({
  selector: 'app-games',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './games.component.html',
  styleUrl: './games.component.scss',
})
export class GamesComponent implements OnInit {
  private gameSrv = inject(GameService);

  games = signal<Game[]>([]);
  loading = signal(true);

  ngOnInit(): void {
    this.gameSrv.getAll().subscribe({
      next: (data) => { this.games.set(data); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  logo(g: Game): string {
    return g.logoUrl || `https://placehold.co/160x160/fee2e2/dc2626?text=${encodeURIComponent(g.name.charAt(0))}`;
  }
}
