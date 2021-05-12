import { Component, OnInit } from '@angular/core';
import {LoginService} from "../login.service";
import {Router} from "@angular/router";
import {AuthError, InteractionRequiredAuthError} from "msal";
import {ImageService} from "../image.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(private login: LoginService, private imageService: ImageService) { }

  loggedIn;
  profile;
  navLinks = [
    {
      path: '/home',
      label: 'Home',
      disabled: true
    },
    {
      path: '/checkImage',
      label: 'Check Image',
      disabled: false
    },
    {
      path: '/registerImage',
      label: 'Register Image',
      disabled: true
    },
    {
      path: '/about',
      label: 'About',
      disabled: false
    }
  ]

  ngOnInit(): void {
    this.loggedIn = this.login.checkAccount();
    this.profile = this.login.getIdentity();
  }

  doLogin() {
    this.login.login().then(data => {
      this.ngOnInit();
    });
  }

  doLogout() {
    this.login.logout();
  }

  editProfile() {
    this.login.profileEdit().then(data => {
      console.log('editprofile', data);
      const user = new FormData();
      user.append('name', data.account.name);
      this.imageService.updateUser(user).subscribe(ok => {
        console.log('updateuser', ok);
        this.imageService.snackBarMessage('Account updated.');
      }, error => {
        console.log('updateUser', error);
        this.imageService.snackBarMessage('Error during account update');
      });
      this.ngOnInit();
    });
  }

  resetPassword() {
    this.login.resetPassword();
  }

}
