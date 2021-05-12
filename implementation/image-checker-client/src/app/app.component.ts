import {Component, OnDestroy, OnInit} from '@angular/core';
import {BroadcastService, MsalService} from "@azure/msal-angular";
import {CryptoUtils, Logger} from "msal";
import {ImageService} from "./image.service";
import {b2cPolicies, isIE, LoginService} from "./login.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'image-checker-client';
  subscription;

  constructor(private broadcastService: BroadcastService, private authService: MsalService, private loginService: LoginService) {}

  ngOnInit() {
    console.log('url', this.authService.getPostLogoutRedirectUri())

    this.loginService.init();

    this.subscription =  this.broadcastService.subscribe("msal:acquireTokenFailure", (payload) => {
      console.log("Subscription: ", payload);
    });

    this.broadcastService.subscribe('msal:loginSuccess', () => {
      this.loginService.checkAccount();
      this.loginService.setIdentity(this.authService.getAccount().idToken);
      console.log(this.loginService.getIdentity());
    });

    this.broadcastService.subscribe('msal:loginFailure', (error) => {
      console.log('login failed');
      console.log(error);

      // Check for forgot password error
      // Learn more about AAD error codes at https://docs.microsoft.com/en-us/azure/active-directory/develop/reference-aadsts-error-codes
      if (error.errorMessage.indexOf('AADB2C90118') > -1) {
        if (isIE) {
          this.authService.loginRedirect(b2cPolicies.authorities.resetPassword);
        } else {
          this.authService.loginPopup(b2cPolicies.authorities.resetPassword);
        }
      }
    });

  }

  ngOnDestroy() {
    this.broadcastService.getMSALSubject().next(1);
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}
