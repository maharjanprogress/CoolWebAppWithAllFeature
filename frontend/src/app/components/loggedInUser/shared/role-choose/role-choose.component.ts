import {Component, forwardRef, Input, OnDestroy, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { ControlValueAccessor, NG_VALUE_ACCESSOR, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import {ResponseStatus, RoleDTO, UserRole} from '../../../../model/api-responses';
import { Subscription } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import {SnackbarService} from "../../../../services/snackbar.service";
import {RoleService} from "../../../../services/role/role.service";

@Component({
  selector: 'app-role-choose',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatFormFieldModule, MatSelectModule, MatProgressSpinnerModule],
  templateUrl: './role-choose.component.html',
  styleUrl: './role-choose.component.scss',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => RoleChooseComponent),
      multi: true
    }
  ]
})
export class RoleChooseComponent implements OnInit, OnDestroy, ControlValueAccessor {
  @Input() exclude: UserRole[] = [];
  @Input() include: UserRole[] | null = null;
  @Input() returnId: boolean = true;
  @Input() label: string = 'Role';

  roles: RoleDTO[] = [];
  isLoading = false;
  private rolesSubscription?: Subscription;

  // --- ControlValueAccessor properties ---
  value: number | string | null = null;
  isDisabled = false;
  onChange: (value: any) => void = () => {};
  onTouched: () => void = () => {};

  constructor(
    private roleService: RoleService,
    private snackbarService: SnackbarService
  ) {}

  ngOnInit(): void {
    this.isLoading = true;
    this.rolesSubscription = this.roleService.getAllRoles().subscribe({
      next: (response) => {
        if (response.status === ResponseStatus.SUCCESS) {
          let filteredRoles = response.details || [];
          if (this.include) {
            filteredRoles = filteredRoles.filter(role => this.include!.includes(role.roleAlias));
          }
          if (this.exclude.length > 0) {
            filteredRoles = filteredRoles.filter(role => !this.exclude.includes(role.roleAlias));
          }
          this.roles = filteredRoles;
        } else {
          this.snackbarService.show(response.message || 'Failed to load roles.', 'error');
        }
        this.isLoading = false;
      },
      error: (err: HttpErrorResponse) => {
        this.snackbarService.show(err.error?.message || 'A server error occurred while loading roles.', 'error');
        this.isLoading = false;
      }
    });
  }

  onSelectionChange(value: any): void {
    this.value = value;
    this.onChange(this.value);
    this.onTouched();
  }

  // --- ControlValueAccessor methods ---
  writeValue(value: any): void { this.value = value; }
  registerOnChange(fn: any): void { this.onChange = fn; }
  registerOnTouched(fn: any): void { this.onTouched = fn; }
  setDisabledState?(isDisabled: boolean): void { this.isDisabled = isDisabled; }

  ngOnDestroy(): void {
    this.rolesSubscription?.unsubscribe();
  }
}
