import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RegisterImageComponent} from "./register-image/register-image.component";
import {HomeComponent} from "./home/home.component";
import {MsalGuard} from "@azure/msal-angular";
import {WelcomeComponent} from "./welcome/welcome.component";
import {AboutComponent} from "./about/about.component";
import {CheckImageComponent} from "./check-image/check-image.component";


const routes: Routes = [
  {
    path: 'registerImage', component: RegisterImageComponent, canActivate: [MsalGuard]
  },
  {
    path: 'home', component: HomeComponent, canActivate: [MsalGuard]
  },
  {
    path: 'about', component: AboutComponent
  },
  {
    path: 'checkImage', component: CheckImageComponent
  },
  {
    path: '', component: WelcomeComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: false})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
