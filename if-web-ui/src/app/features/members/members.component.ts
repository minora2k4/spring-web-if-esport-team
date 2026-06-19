import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MemberService } from '../../core/services/member.service';
import { Member } from '../../shared/models/api.model';

@Component({
  selector: 'app-members',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './members.component.html',
  styleUrl: './members.component.scss',
})
export class MembersComponent implements OnInit {
  private memberSrv = inject(MemberService);

  members = signal<Member[]>([]);
  loading = signal(true);
  selected = signal<Member | null>(null);

  ngOnInit(): void {
    this.memberSrv.getActive().subscribe({
      next: (data) => { this.members.set(data); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  avatar(m: Member): string {
    return m.avatarUrl || `https://placehold.co/240x240/fee2e2/dc2626?text=${encodeURIComponent(m.inGameName.charAt(0))}`;
  }

  openDetail(m: Member): void { this.selected.set(m); }
  closeDetail(): void { this.selected.set(null); }
}
