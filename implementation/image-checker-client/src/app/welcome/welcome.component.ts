import {Component, OnDestroy, OnInit} from '@angular/core';
import {LoginService} from "../login.service";
import {Router} from "@angular/router";
import {BroadcastService} from "@azure/msal-angular";

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit, OnDestroy {

  constructor(private login: LoginService, private router: Router, private broadcastService: BroadcastService) { }

  subscription;

  ngOnInit(): void {
    console.log("Welcome")
    if (this.login.checkAccount()) {
      this.router.navigate(['home']);
    }

    this.subscription = this.broadcastService.subscribe('msal:loginSuccess', () => {
      console.log("login success callback")
      if (this.login.checkAccount()) {
        this.router.navigate(['home']);
      }
    });

  }

  doLogin() {
    this.login.login();
  }

  ngOnDestroy() {
    this.broadcastService.getMSALSubject().next(1);
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}
