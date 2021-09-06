import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {SigninComponent} from './components/signin/signin.component';
import {RegisterComponent} from './components/register/register.component';
import {AllcoursesComponent} from "./components/allcourses/allcourses.component";
import {LandingComponent} from "./components/landing/landing.component";
import {DetailsComponent} from "./components/details/details.component";
import {HomeComponent} from "./components/home/home.component";
import {ProfileComponent} from "./components/profile/profile.component";
import {BoardAdminComponent} from "./components/board-admin/board-admin.component";
import {AuthGuard} from "./services/AuthGuard";
import {NotAuthComponent} from "./components/not-auth/not-auth.component";
import {AuthComponent} from "./components/auth/auth.component";
import {MycoursesComponent} from "./components/mycourses/mycourses.component";
import {ListFormationComponent} from "./components/board-admin/list-formation/list-formation.component";
import {ListUserComponent} from "./components/board-admin/list-user/list-user.component";
import {AdminListComponent} from "./components/board-admin/admin-list/admin-list.component";

const routes: Routes = [
  {path: '', pathMatch: 'full', redirectTo: 'home'},
  {
    path: 'auth', component: AuthComponent, children: [
      {
        path: 'signin',
        component: SigninComponent,
      },
      {
        path: 'register',
        component: RegisterComponent,
      },
    ],
  },
  {path: 'signin', component: SigninComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'mycourses', component: MycoursesComponent, canActivate: [AuthGuard]},
  {path: 'allcourses', component: AllcoursesComponent, canActivate: [AuthGuard]},
  {path: 'details/:id', component: DetailsComponent, canActivate: [AuthGuard]},
  {path: 'profile', component: ProfileComponent, canActivate: [AuthGuard]},
  {
    path: 'admin', component: BoardAdminComponent, canActivate: [AuthGuard], data: {roles: ["ROLE_ADMIN"]},
    children: [
      {
        path: 'list_users',
        component: ListUserComponent,
      },
      {
        path: 'list_admins',
        component: AdminListComponent,
      },
      {
        path: 'list_formations',
        component: ListFormationComponent,
      },
    ],
  },
  {path: 'home', component: HomeComponent},
  {path: 'notauthorized', component: NotAuthComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule {
}
