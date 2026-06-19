/* Generic API envelope returned by the backend (BaseController / ApiResponse) */
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

/* ===================== Entities (mirror backend *Response DTOs) ===================== */
export interface Game {
  id: number;
  name: string;
  logoUrl: string;
}

export interface Member {
  id: number;
  inGameName: string;
  realName: string;
  role: string;
  achievement: string;
  avatarUrl: string;
  isActive: boolean;
  games: Game[] | null;
}

export interface Tournament {
  id: number;
  name: string;
  achievement: string;
  startDate: string;
  game: Game | null;
}

export type BetResult = 'WIN' | 'LOSE' | 'PENDING';

export interface Bet {
  id: number;
  opponent: string;
  result: BetResult;
  amount: number;
  betDate: string;
  game: Game | null;
}

export type TeamLogType = 'JOIN' | 'LEAVE' | 'ACHIEVEMENT';

export interface TeamLog {
  id: number;
  description: string;
  type: TeamLogType;
  eventDate: string;
  member: Member | null;
}

export interface OffteamEvent {
  id: number;
  title: string;
  eventDate: string;
  coverPhotoUrl: string;
  photoUrls: string[] | null;
}

/* ===================== Request payloads (mirror backend *Request DTOs) ===================== */
export interface GameRequest {
  name: string;
  logoUrl?: string;
}

export interface MemberRequest {
  inGameName: string;
  realName?: string;
  role?: string;
  achievement?: string;
  avatarUrl?: string;
  isActive?: boolean;
  gameIds?: number[];
}

export interface TournamentRequest {
  name: string;
  achievement?: string;
  startDate?: string | null;
  gameId: number;
}

export interface BetRequest {
  opponent: string;
  result?: BetResult;
  amount?: number | null;
  betDate?: string | null;
  gameId: number;
}

export interface OffteamEventRequest {
  title: string;
  eventDate?: string | null;
  coverPhotoUrl?: string;
  photoUrls?: string[];
}

export interface TeamLogRequest {
  description: string;
  type?: TeamLogType;
  eventDate?: string | null;
  memberId?: number | null;
}

/* ===================== Auth ===================== */
export interface LoginResponse {
  token: string;
  username: string;
  role: string;
}
