import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { MemberService } from '../../core/services/member.service';
import { TournamentService } from '../../core/services/tournament.service';
import { GameService } from '../../core/services/game.service';
import { BetService } from '../../core/services/bet.service';
import { Member, Tournament } from '../../shared/models/api.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent implements OnInit {
  private memberSrv = inject(MemberService);
  private tournamentSrv = inject(TournamentService);
  private gameSrv = inject(GameService);
  private betSrv = inject(BetService);

  members = signal<Member[]>([]);
  tournaments = signal<Tournament[]>([]);

  stats = signal({ members: 0, games: 0, tournaments: 0, wins: 0 });

  ngOnInit(): void {
    forkJoin({
      members: this.memberSrv.getActive(),
      tournaments: this.tournamentSrv.getAll(),
      games: this.gameSrv.getAll(),
      bets: this.betSrv.getAll(),
    }).subscribe({
      next: ({ members, tournaments, games, bets }) => {
        this.members.set(members.slice(0, 4));
        this.tournaments.set(tournaments.slice(0, 3));
        this.stats.set({
          members: members.length,
          games: games.length,
          tournaments: tournaments.length,
          wins: bets.filter((b) => b.result === 'WIN').length,
        });
      },
      error: () => {},
    });
  }
}
