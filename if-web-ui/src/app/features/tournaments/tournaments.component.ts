import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TournamentService } from '../../core/services/tournament.service';
import { Tournament } from '../../shared/models/api.model';

@Component({
  selector: 'app-tournaments',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './tournaments.component.html',
  styleUrl: './tournaments.component.scss',
})
export class TournamentsComponent implements OnInit {
  private tournamentSrv = inject(TournamentService);

  tournaments = signal<Tournament[]>([]);
  loading = signal(true);

  ngOnInit(): void {
    this.tournamentSrv.getAll().subscribe({
      next: (data) => { this.tournaments.set(data); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }
}
