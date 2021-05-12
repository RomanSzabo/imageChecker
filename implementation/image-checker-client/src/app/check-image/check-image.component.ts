import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {LoginService} from "../login.service";
import {ImageService} from "../image.service";
import {MessageDialogData, MetadataElement, SimilarityReport} from "../interfaces.model";
import {MatPaginator} from "@angular/material/paginator";
import {MatTableDataSource} from "@angular/material/table";
import {isNotNullOrUndefined} from "codelyzer/util/isNotNullOrUndefined";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-check-image',
  templateUrl: './check-image.component.html',
  styleUrls: ['./check-image.component.css']
})
export class CheckImageComponent implements OnInit {

  uploadForm: FormGroup;
  similarityReport: SimilarityReport;
  loading = false;
  loadingButton = false;
  dataSource = new MatTableDataSource<MetadataElement>();
  displayedColumns: string[] = ['key', 'value'];
  isFastCheck = false;

  constructor(private fb: FormBuilder, private loginService: LoginService, private imageService: ImageService, private dialog: MatDialog) { }

  @ViewChild(MatPaginator) paginator: MatPaginator;

  ngOnInit(): void {
    this.uploadForm = this.fb.group({
      image: []
    });
    if (isNotNullOrUndefined(this.imageService.result)) {
      this.similarityReport = this.imageService.result;
      this.imageService.result = null;
      for (let check of this.similarityReport.checkResultsList) {
        check.table = new MatTableDataSource<MetadataElement>();
        check.table.data = this.similarityReport.checkResultsList[0].metadata.metadata;
        check.table.paginator = this.paginator;
      }
    }
  }

  openDialog(image, userId, imageName, userName): void {
    const dialogRef = this.dialog.open(SendMessageDialog, {
      width: '500px',
      data: {image: image, userId: userId, from: '', text: '', imageName: imageName, userName: userName}
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed', result);
      this.imageService.snackBarMessage('Sending message...');
      this.loadingButton = true;
      const data = new FormData();
      data.append('recipient', result.userId);
      data.append('sender', result.from);
      data.append('image', result.image);
      data.append('imageName', result.imageName);
      data.append('text', result.text);
      this.imageService.sendNewMessage(data).subscribe(res => {
        console.log('send message', res);
        this.loadingButton = false;
        this.imageService.snackBarMessage("Message Sent Successfully.")
      }, error => {
        console.log('send message error', error);
        this.loadingButton = false;
        this.imageService.snackBarMessage("Message Sending Error" + error.message);
      });
    });
  }

  onSubmit(form) {
    if (form == null) return;
    this.loading = true;
    const data = new FormData();
    data.append('image', this.uploadForm.get('image').value._files[0]);
    if (this.loginService.checkAccount()) {
      this.imageService.checkImage(data).subscribe(res => {
        this.similarityReport = res;
        for (let check of this.similarityReport.checkResultsList) {
          check.table = new MatTableDataSource<MetadataElement>();
          check.table.data = this.similarityReport.checkResultsList[0].metadata.metadata;
          check.table.paginator = this.paginator;
        }
        this.loading = false;
      }, error => {
        console.log('check error', error);
        this.imageService.snackBarMessage('Error occurred: ' + error.message);
        this.loading = false
      });
    } else {
      this.imageService.checkPublicImage(data).subscribe(res => {
        this.similarityReport = res;
        this.loading = false;
      }, error => {
        console.log('check error', error);
        this.imageService.snackBarMessage('Error occurred: ' + error.message);
        this.loading = false
      });
    }
  }

  isLoggedIn() {
    return this.loginService.checkAccount();
  }

}

@Component({
  selector: 'send-message-dialog',
  templateUrl: 'send-message-dialog.html',
})
export class SendMessageDialog {

  email = new FormControl('', [Validators.email, Validators.required]);
  message = new FormControl('', [Validators.required, Validators.minLength(5)]);

  constructor(
    public dialogRef: MatDialogRef<SendMessageDialog>,
    @Inject(MAT_DIALOG_DATA) public data: MessageDialogData) {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}

