import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', loadComponent: () => import('./features/home/home.component').then(m => m.HomeComponent) },
  { path: 'members', loadComponent: () => import('./features/members/members.component').then(m => m.MembersComponent) },
  { path: 'games', loadComponent: () => import('./features/games/games.component').then(m => m.GamesComponent) },
  { path: 'tournaments', loadComponent: () => import('./features/tournaments/tournaments.component').then(m => m.TournamentsComponent) },
  { path: 'bets', loadComponent: () => import('./features/bets/bets.component').then(m => m.BetsComponent) },
  { path: 'offteam', loadComponent: () => import('./features/offteam/offteam.component').then(m => m.OffteamComponent) },
  { path: 'team-log', loadComponent: () => import('./features/team-log/team-log.component').then(m => m.TeamLogComponent) },

  // Admin
  { path: 'admin/login', loadComponent: () => import('./features/admin/login/login.component').then(m => m.LoginComponent) },
  {
    path: 'admin',
    canActivate: [authGuard],
    children: [
      { path: 'dashboard', loadComponent: () => import('./features/admin/dashboard/dashboard.component').then(m => m.DashboardComponent) },
      { path: 'members', loadComponent: () => import('./features/admin/manage-members/manage-members.component').then(m => m.ManageMembersComponent) },
      { path: 'games', loadComponent: () => import('./features/admin/manage-games/manage-games.component').then(m => m.ManageGamesComponent) },
      { path: 'tournaments', loadComponent: () => import('./features/admin/manage-tournaments/manage-tournaments.component').then(m => m.ManageTournamentsComponent) },
      { path: 'bets', loadComponent: () => import('./features/admin/manage-bets/manage-bets.component').then(m => m.ManageBetsComponent) },
      { path: 'team-log', loadComponent: () => import('./features/admin/manage-teamlog/manage-teamlog.component').then(m => m.ManageTeamlogComponent) },
      { path: 'offteam', loadComponent: () => import('./features/admin/manage-offteam/manage-offteam.component').then(m => m.ManageOffteamComponent) },
    ]
  },
  { path: '**', redirectTo: '' }
];
