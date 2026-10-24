import { Component, OnInit, OnDestroy, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService } from '../../../services/auth.service';
import { ThemeService } from '../../../services/theme.service';
import { User, Admin } from '../../../models/user.model';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit, OnDestroy {
  isLoggedIn = false;
  currentUser: User | Admin | null = null;
  isDarkMode = false;
  isUserDropdownOpen = false;
  
  private subscriptions = new Subscription();

  constructor(
    private authService: AuthService,
    private themeService: ThemeService,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    // Subscribe to auth state
    this.subscriptions.add(
      this.authService.isLoggedIn$.subscribe((isLoggedIn: boolean) => {
        this.isLoggedIn = isLoggedIn;
      })
    );

    this.subscriptions.add(
      this.authService.currentUser$.subscribe((user: User | Admin | null) => {
        this.currentUser = user;
      })
    );

    // Subscribe to theme state
    this.subscriptions.add(
      this.themeService.isDarkMode$.subscribe((isDark: boolean) => {
        this.isDarkMode = isDark;
      })
    );

    // Close dropdown when clicking outside (only in browser)
    if (isPlatformBrowser(this.platformId)) {
      document.addEventListener('click', (event: Event) => {
        const target = event.target as HTMLElement;
        if (!target.closest('.user-dropdown')) {
          this.isUserDropdownOpen = false;
        }
      });
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  toggleTheme(): void {
    this.themeService.toggleTheme();
  }

  logout(): void {
    this.closeUserDropdown();
    this.authService.logout();
    this.router.navigate(['/']);
  }

  navigateToLogin(): void {
    this.router.navigate(['/auth/signin']);
  }

  navigateToSignUp(): void {
    this.router.navigate(['/auth/signup']);
  }

  // Helper methods for template
  getUserDisplayName(): string {
    if (!this.currentUser) return '';
    
    if (this.isAdmin(this.currentUser)) {
      // Admin user
      return this.currentUser.name;
    } else {
      // Regular user
      return `${this.currentUser.firstName} ${this.currentUser.lastName}`;
    }
  }

  getUserInitials(): string {
    if (!this.currentUser) return '';
    
    if (this.isAdmin(this.currentUser)) {
      // Admin user
      const names = this.currentUser.name.split(' ');
      return names.length > 1 
        ? `${names[0].charAt(0)}${names[names.length - 1].charAt(0)}` 
        : names[0].charAt(0);
    } else {
      // Regular user
      return `${this.currentUser.firstName?.charAt(0) || ''}${this.currentUser.lastName?.charAt(0) || ''}`;
    }
  }

  getUserEmail(): string {
    if (!this.currentUser) return '';
    return this.currentUser.email;
  }

  getUserContact(): string {
    if (!this.currentUser) return '';
    
    if (this.isAdmin(this.currentUser)) {
      return ''; // Admins don't have contact info
    } else {
      return this.currentUser.contact || '';
    }
  }

  getUserCollege(): string {
    if (!this.currentUser) return '';
    
    if (this.isAdmin(this.currentUser)) {
      return 'Admin Access'; // Admin indicator
    } else {
      return this.currentUser.college || '';
    }
  }

  toggleUserDropdown(): void {
    this.isUserDropdownOpen = !this.isUserDropdownOpen;
  }

  closeUserDropdown(): void {
    this.isUserDropdownOpen = false;
  }

  private isAdmin(user: User | Admin): user is Admin {
    return 'name' in user && 'adminId' in user && !('userId' in user);
  }


}
