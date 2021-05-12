import { Component, OnInit } from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {LoginService} from "../login.service";

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.css']
})
export class AboutComponent implements OnInit {

  constructor(private dialog: MatDialog, private loginService: LoginService) { }

  ngOnInit(): void {
  }

  openDialog() {
    const dialogRef = this.dialog.open(TokenDialog);

    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
    });
  }

  isLoggedIn() {
    return this.loginService.checkAccount();
  }

}

@Component({
  selector: 'token-dialog',
  templateUrl: 'token-dialog.html',
})
export class TokenDialog {
  getToken() {
    return localStorage.getItem("msal.idtoken");
  }
}
