import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OffteamService } from '../../core/services/offteam.service';
import { OffteamEvent } from '../../shared/models/api.model';

@Component({
  selector: 'app-offteam',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './offteam.component.html',
  styleUrl: './offteam.component.scss',
})
export class OffteamComponent implements OnInit {
  private offteamSrv = inject(OffteamService);

  events = signal<OffteamEvent[]>([]);
  loading = signal(true);
  lightbox = signal<string | null>(null);

  ngOnInit(): void {
    this.offteamSrv.getAll().subscribe({
      next: (data) => { this.events.set(data); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  cover(e: OffteamEvent): string {
    return e.coverPhotoUrl || e.photoUrls?.[0] || 'https://placehold.co/600x360/eef2ff/6366f1?text=Offteam';
  }

  open(url: string) { this.lightbox.set(url); }
  closeBox() { this.lightbox.set(null); }
}
