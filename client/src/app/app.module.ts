import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AngularMaterialModule} from './angular-material.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AppRoutingModule} from './app-routing.module';
import {FlexLayoutModule} from '@angular/flex-layout';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {SigninComponent} from './components/signin/signin.component';
import {RegisterComponent} from './components/register/register.component';
import {LandingComponent} from './components/landing/landing.component';
import {CardComponent} from './components/card/card.component';
import {ListCardsComponent} from './components/list-cards/list-cards.component';
import {AllcoursesComponent} from './components/allcourses/allcourses.component';
import {AddformationComponent} from './components/addformation/addformation.component';
import {DetailsComponent} from './components/details/details.component';
import {DocumentsComponent} from './components/documents/documents.component';
import {ParticipantsComponent} from './components/participants/participants.component';
import {ListComponent} from './components/participants/list/list.component';
import {ListDComponent} from './components/documents/list-d/list-d.component';
import {CommentsComponent} from './components/comments/comments.component';
import {CommentComponent} from './components/comments/comment/comment.component';
import {HomeComponent} from './components/home/home.component';
import {HttpClientModule} from '@angular/common/http';
import {ProfileComponent} from './components/profile/profile.component';
import {authInterceptorProviders} from "./helpers";
import {BoardAdminComponent} from './components/board-admin/board-admin.component';
import {NotAuthComponent} from './components/not-auth/not-auth.component';
import {AuthComponent} from './components/auth/auth.component';
import {SettingsComponent} from './components/details/settings/settings.component';
import {UpdateComponent} from './components/details/settings/update/update.component';
import {MatFileUploadModule} from 'angular-material-fileupload';
import {MycoursesComponent} from './components/mycourses/mycourses.component';
import {DeleteComponent} from './components/details/delete/delete.component';
import {ListFormationComponent} from './components/board-admin/list-formation/list-formation.component';
import {ListUserComponent} from './components/board-admin/list-user/list-user.component';
import {ListParticipantsComponent} from './components/board-admin/list-formation/list-participants/list-participants.component';
import {AdminListComponent} from './components/board-admin/admin-list/admin-list.component';
import {AddAdminComponent} from './components/board-admin/admin-list/add-admin/add-admin.component';
import {NgxMatDatetimePickerModule, NgxMatTimepickerModule} from '@angular-material-components/datetime-picker';
import {MeetComponent} from './components/details/settings/meet/meet.component';
import {DateTimePickerModule} from '@syncfusion/ej2-angular-calendars';
import {DateInputsModule} from "@progress/kendo-angular-dateinputs";
import {LabelModule} from "@progress/kendo-angular-label";
import {IntlModule} from "@progress/kendo-angular-intl";

@NgModule({
  declarations: [
    AppComponent,
    SigninComponent,
    RegisterComponent,
    LandingComponent,
    CardComponent,
    ListCardsComponent,
    AllcoursesComponent,
    AddformationComponent,
    DetailsComponent,
    DocumentsComponent,
    ParticipantsComponent,
    ListComponent,
    ListDComponent,
    CommentsComponent,
    CommentComponent,
    HomeComponent,
    ProfileComponent,
    BoardAdminComponent,
    NotAuthComponent,
    AuthComponent,
    SettingsComponent,
    UpdateComponent,
    MycoursesComponent,
    DeleteComponent,
    ListFormationComponent,
    ListUserComponent,
    ListParticipantsComponent,
    AdminListComponent,
    AddAdminComponent,
    MeetComponent,

  ],
  imports: [
    AngularMaterialModule,
    BrowserModule, HttpClientModule,
    FlexLayoutModule,
    FormsModule,
    DateTimePickerModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    IntlModule,
    LabelModule,
    DateInputsModule,
    MatFileUploadModule
  ],
  providers: [authInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule {
}
