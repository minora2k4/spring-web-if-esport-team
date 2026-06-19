import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { AuthService } from '../../../core/services/auth.service';
import { MemberService } from '../../../core/services/member.service';
import { GameService } from '../../../core/services/game.service';
import { TournamentService } from '../../../core/services/tournament.service';
import { BetService } from '../../../core/services/bet.service';
import { OffteamService } from '../../../core/services/offteam.service';
import { TeamlogService } from '../../../core/services/teamlog.service';

interface AdminCard {
  title: string; icon: string; path: string; count: number; color: string;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {
  private auth = inject(AuthService);
  private memberSrv = inject(MemberService);
  private gameSrv = inject(GameService);
  private tournamentSrv = inject(TournamentService);
  private betSrv = inject(BetService);
  private offteamSrv = inject(OffteamService);
  private logSrv = inject(TeamlogService);

  username = this.auth.getUsername() || 'Admin';
  cards = signal<AdminCard[]>([]);

  ngOnInit(): void {
    forkJoin({
      members: this.memberSrv.getAll(),
      games: this.gameSrv.getAll(),
      tournaments: this.tournamentSrv.getAll(),
      bets: this.betSrv.getAll(),
      offteam: this.offteamSrv.getAll(),
      logs: this.logSrv.getAll(),
    }).subscribe({
      next: (d) => this.cards.set([
        { title: 'Thành viên', icon: '👥', path: '/admin/members',     count: d.members.length,     color: '#6366f1' },
        { title: 'Tựa game',   icon: '🎯', path: '/admin/games',       count: d.games.length,       color: '#06b6d4' },
        { title: 'Giải đấu',   icon: '🏆', path: '/admin/tournaments', count: d.tournaments.length, color: '#f59e0b' },
        { title: 'Kèo đấu',    icon: '🎮', path: '/admin/bets',        count: d.bets.length,        color: '#ec4899' },
        { title: 'Offteam',    icon: '📸', path: '/admin/offteam',     count: d.offteam.length,     color: '#10b981' },
        { title: 'Nhật ký',    icon: '📋', path: '/admin/team-log',    count: d.logs.length,        color: '#8b5cf6' },
      ]),
      error: () => {},
    });
  }

  logout() { this.auth.logout(); }
}
