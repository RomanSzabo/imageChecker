import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {RegisterImageComponent} from './register-image/register-image.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {HomeComponent, InboxDialog} from './home/home.component';
import {MsalInterceptor, MsalModule} from "@azure/msal-angular";
import {MatToolbarModule} from "@angular/material/toolbar";
import {ImageService} from "./image.service";
import {HeaderComponent} from './header/header.component';
import {WelcomeComponent} from './welcome/welcome.component';
import {MatButtonModule} from "@angular/material/button";
import {LoginService} from "./login.service";
import {MatTabsModule} from "@angular/material/tabs";
import {AboutComponent, TokenDialog} from './about/about.component';
import {environment} from "../environments/environment";
import {MatDialogModule} from "@angular/material/dialog";
import {MatGridListModule} from "@angular/material/grid-list";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MaterialFileInputModule} from "ngx-material-file-input";
import {MatIconModule} from "@angular/material/icon";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatTableModule} from "@angular/material/table";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatCardModule} from "@angular/material/card";
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {MatMenuModule} from "@angular/material/menu";
import {CheckImageComponent, SendMessageDialog} from './check-image/check-image.component';
import {MatExpansionModule} from "@angular/material/expansion";
import {MatDividerModule} from "@angular/material/divider";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatInputModule} from "@angular/material/input";

@NgModule({
  declarations: [
    AppComponent,
    RegisterImageComponent,
    HomeComponent,
    HeaderComponent,
    WelcomeComponent,
    AboutComponent,
    TokenDialog,
    CheckImageComponent,
    SendMessageDialog,
    InboxDialog
  ],
  entryComponents: [
    TokenDialog
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MsalModule.forRoot({
        auth: {
          clientId: '<clientid>',
          authority: '<auth>',
          validateAuthority: false,
          redirectUri: environment.redirectUrl,
          postLogoutRedirectUri: environment.postLogOut,
          navigateToLoginRequestUrl: false
        },
        cache: {
          cacheLocation: 'localStorage',
          storeAuthStateInCookie: false, // set to true for IE 11
        },
      },
      {
        popUp: true,
        consentScopes: [
          'User.Read',
          'openid',
          'profile',
        ],
        unprotectedResources: [],
        protectedResourceMap: [
          ['https://graph.microsoft.com/v1.0/me', ['user.read']]
        ],
        extraQueryParameters: {}
      }),
    MatToolbarModule,
    MatButtonModule,
    MatTabsModule,
    MatDialogModule,
    MatGridListModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MaterialFileInputModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatTableModule,
    MatPaginatorModule,
    MatCardModule,
    MatProgressBarModule,
    MatMenuModule,
    MatExpansionModule,
    MatDividerModule,
    MatTooltipModule,
    MatInputModule,
    FormsModule
  ],
  providers: [
    ImageService,
    LoginService,
    MatSnackBar,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: MsalInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
