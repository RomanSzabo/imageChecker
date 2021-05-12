import {Component, Inject, OnInit} from '@angular/core';
import {ImageService} from "../image.service";
import {LoginService} from "../login.service";
import {FormControl, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {MessageDialogData, MessageModel} from "../interfaces.model";
import {SendMessageDialog} from "../check-image/check-image.component";


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  profile;

  constructor(private imageService: ImageService, private loginService: LoginService, private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.profile = this.loginService.getIdentity();
  }

  editProfile() {
    this.loginService.profileEdit().then(data => {
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
    this.loginService.resetPassword();
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(InboxDialog, {
      width: '900px',
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed', result);
    });
  }
}

@Component({
  selector: 'inbox-dialog',
  templateUrl: 'inbox-dialog.html',
})
export class InboxDialog implements OnInit{

  inbox: MessageModel[];
  loading = true;
  displayedColumns: string[] = ['from', 'image', 'text', 'date'];

  constructor(public dialogRef: MatDialogRef<InboxDialog>, private imageService: ImageService) {}

  ngOnInit() {
    this.imageService.getMessages().subscribe(data => {
      console.log('inbox', data);
      this.inbox = data;
      this.loading = false;
    }, error => {
      console.log('inbox error', error);
      this.imageService.snackBarMessage('Error obtaining Inbox' + error.message);
      this.loading = false;
    });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
