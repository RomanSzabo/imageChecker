import {Injectable} from "@angular/core";
import {MsalService} from "@azure/msal-angular";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {AuthError, CryptoUtils, InteractionRequiredAuthError, Logger} from "msal";

const GRAPH_ENDPOINT = 'https://graph.microsoft.com/v1.0/me';
export const isIE = window.navigator.userAgent.indexOf('MSIE ') > -1 || window.navigator.userAgent.indexOf('Trident/') > -1;
export const b2cPolicies = {
  names: {
    signUpSignIn: "b2c_1_registerandlogin",
    resetPassword: "b2c_1_passwordreset",
    profileEdit: "b2c_1_profile_edit"
  },
  authorities: {
    signUpSignIn: {
      authority: "<auth>"
    },
    resetPassword: {
      authority: "<auth>"
    },
    profileEdit: {
      authority: "<auth>"
    }
  }
}

@Injectable()
export class LoginService {

  identity: any;

  constructor(private authService: MsalService, private router: Router, private http: HttpClient) {}

  init() {
    this.authService.handleRedirectCallback((authError, response) => {
      if (authError) {
        console.error('Redirect Error: ', authError.errorMessage);
        return;
      }

      console.log('Redirect Success: ', response.accessToken);
    });

    this.authService.setLogger(new Logger((logLevel, message, piiEnabled) => {
      console.log('MSAL Logging: ', message);
    }, {
      correlationId: CryptoUtils.createNewGuid(),
      piiLoggingEnabled: false
    }));

    if (this.checkAccount()) {
      this.identity = this.authService.getAccount().idToken;
    }
  }

  checkAccount() {
    return !!this.authService.getAccount();
  }


  login() {
    if (isIE) {
      this.authService.loginRedirect();
    } else {
      return this.authService.loginPopup();
    }
  }

  logout() {
    this.authService.logout();
  }

  resetPassword() {
    if (isIE) {
      this.authService.loginRedirect(b2cPolicies.authorities.resetPassword);
    } else {
      this.authService.loginPopup(b2cPolicies.authorities.resetPassword);
    }
  }

  profileEdit() {
    if (isIE) {
      this.authService.loginRedirect(b2cPolicies.authorities.profileEdit);
    } else {
      return this.authService.loginPopup(b2cPolicies.authorities.profileEdit);
    }
  }

  getIdentity() {
    return this.identity;
  }

  setIdentity(identity: any) {
    this.identity = identity;
  }

}
