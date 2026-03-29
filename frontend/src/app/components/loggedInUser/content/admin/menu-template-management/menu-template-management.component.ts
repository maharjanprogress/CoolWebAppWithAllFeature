import {Component, OnDestroy, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatSelectModule} from '@angular/material/select';
import {HttpErrorResponse} from '@angular/common/http';
import {Subscription} from 'rxjs';
import {MenuTemplateDTO, ResponseStatus, SubMenuDTO} from '../../../../../model/api-responses';
import {SnackbarService} from '../../../../../services/snackbar.service';
import {MenuService} from '../../../../../services/menu/menu.service';
import {RoleChooseComponent} from '../../../shared/role-choose/role-choose.component';

interface PrimaryMenuOption {
  id: number;
  title: string;
  url: string;
  parentMenuId: number | null;
  parentMenuTitle: string | null;
  isFallbackPrimary: boolean;
}

interface MenuGroup {
  id: number;
  title: string;
  subMenus: PrimaryMenuOption[];
}

@Component({
  selector: 'app-menu-template-management',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RoleChooseComponent,
    MatButtonModule,
    MatFormFieldModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSelectModule
  ],
  templateUrl: './menu-template-management.component.html',
  styleUrl: './menu-template-management.component.scss'
})
export class MenuTemplateManagementComponent implements OnInit, OnDestroy {
  readonly form = new FormGroup({
    roleId: new FormControl<number | null>(null, Validators.required),
    primaryMenuId: new FormControl<number | null>(null)
  });

  template: MenuTemplateDTO | null = null;
  primaryMenuOptions: PrimaryMenuOption[] = [];
  menuGroups: MenuGroup[] = [];
  isTemplateLoading = false;
  isSaving = false;
  emptyStateMessage = 'Select a role to inspect its menu template.';

  private readonly subscriptions = new Subscription();

  constructor(
    private menuService: MenuService,
    private snackbarService: SnackbarService
  ) {}

  ngOnInit(): void {
    this.subscriptions.add(
      this.form.controls.roleId.valueChanges.subscribe(roleId => {
        if (roleId == null) {
          this.clearLoadedTemplate('Select a role to inspect its menu template.');
          return;
        }

        this.loadTemplate(roleId);
      })
    );
  }

  savePrimaryMenu(): void {
    const roleId = this.form.controls.roleId.value;
    if (roleId == null || this.template == null) {
      return;
    }

    const payload: MenuTemplateDTO = {
      roleId,
      menuIds: this.template.menuIds,
      primaryMenuId: this.form.controls.primaryMenuId.value ?? null,
      subMenuDTOList: this.template.subMenuDTOList
    };

    this.isSaving = true;
    this.subscriptions.add(
      this.menuService.saveMenuTemplate(payload).subscribe({
        next: (response) => {
          if (response.status === ResponseStatus.SUCCESS && response.detail) {
            this.applyLoadedTemplate(response.detail);
            this.snackbarService.show(response.message || 'Primary submenu updated successfully.', 'success');
          } else {
            this.snackbarService.show(response.message || 'Failed to save the primary submenu.', 'error');
          }
          this.isSaving = false;
        },
        error: (err: HttpErrorResponse) => {
          this.isSaving = false;
          this.snackbarService.show(err.error?.message || 'A server error occurred while saving the primary submenu.', 'error');
        }
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  selectPrimaryMenu(menuId: number | null): void {
    if (this.template === null || this.isTemplateLoading || this.isSaving) {
      return;
    }

    this.form.controls.primaryMenuId.setValue(menuId);
  }

  isPrimaryMenu(menuId: number): boolean {
    return this.form.controls.primaryMenuId.value === menuId;
  }

  groupHasPrimary(group: MenuGroup): boolean {
    return group.subMenus.some(subMenu => this.isPrimaryMenu(subMenu.id));
  }

  groupIsFallback(group: MenuGroup): boolean {
    return this.form.controls.primaryMenuId.value === null && group.subMenus[0]?.isFallbackPrimary === true;
  }

  trackByMenuId(_: number, option: PrimaryMenuOption): number {
    return option.id;
  }

  trackByGroupId(_: number, group: MenuGroup): number {
    return group.id;
  }

  private loadTemplate(roleId: number): void {
    this.isTemplateLoading = true;
    this.template = null;
    this.primaryMenuOptions = [];
    this.menuGroups = [];
    this.emptyStateMessage = 'Loading menu template...';

    this.subscriptions.add(
      this.menuService.getTemplateByRoleId(roleId).subscribe({
        next: (response) => {
          if (response.status === ResponseStatus.SUCCESS && response.detail) {
            this.applyLoadedTemplate(response.detail);
          } else {
            this.clearLoadedTemplate(response.message || 'No menu template found for the selected role.');
          }
          this.isTemplateLoading = false;
        },
        error: (err: HttpErrorResponse) => {
          this.isTemplateLoading = false;
          this.clearLoadedTemplate(err.error?.message || 'A server error occurred while loading the menu template.');
        }
      })
    );
  }

  private applyLoadedTemplate(template: MenuTemplateDTO): void {
    this.template = template;
    this.primaryMenuOptions = this.buildPrimaryMenuOptions(template.subMenuDTOList || []);
    this.menuGroups = this.buildMenuGroups(this.primaryMenuOptions);

    const primaryMenuId = this.primaryMenuOptions.some(option => option.id === template.primaryMenuId)
      ? template.primaryMenuId
      : null;

    this.form.controls.primaryMenuId.setValue(primaryMenuId, {emitEvent: false});
    this.emptyStateMessage = this.primaryMenuOptions.length === 0
      ? 'This role has a template, but no sub-menus are currently assigned to it.'
      : '';
  }

  private clearLoadedTemplate(message: string): void {
    this.template = null;
    this.primaryMenuOptions = [];
    this.menuGroups = [];
    this.form.controls.primaryMenuId.setValue(null, {emitEvent: false});
    this.emptyStateMessage = message;
  }

  private buildPrimaryMenuOptions(subMenus: SubMenuDTO[]): PrimaryMenuOption[] {
    return subMenus
      .filter(subMenu => subMenu.parentMenuId != null)
      .map((subMenu, index) => ({
        id: subMenu.id,
        title: subMenu.title,
        url: subMenu.url,
        parentMenuId: subMenu.parentMenuId,
        parentMenuTitle: subMenu.parentMenuTitle,
        isFallbackPrimary: index === 0
      }));
  }

  private buildMenuGroups(options: PrimaryMenuOption[]): MenuGroup[] {
    const groups = new Map<number, MenuGroup>();

    for (const option of options) {
      if (option.parentMenuId == null) {
        continue;
      }

      const existingGroup = groups.get(option.parentMenuId);
      if (existingGroup) {
        existingGroup.subMenus.push(option);
        continue;
      }

      groups.set(option.parentMenuId, {
        id: option.parentMenuId,
        title: option.parentMenuTitle || 'Untitled Menu',
        subMenus: [option]
      });
    }

    return Array.from(groups.values());
  }
}
