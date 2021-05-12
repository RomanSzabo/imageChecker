import {Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {ImageService} from "../image.service";
import {MatPaginator} from "@angular/material/paginator";
import {MatTableDataSource} from "@angular/material/table";
import {Metadata, MetadataElement, SimilarityReport} from "../interfaces.model";
import {LoginService} from "../login.service";

@Component({
  selector: 'app-register-image',
  templateUrl: './register-image.component.html',
  styleUrls: ['./register-image.component.css']
})
export class RegisterImageComponent implements OnInit {

  uploadForm: FormGroup;
  loading = false;
  loading2 = false;
  loading3 = false;
  loading4 = false;
  noImage = true;
  imageToDisplay;
  metadata: Metadata;
  dataSource = new MatTableDataSource<MetadataElement>();
  displayedColumns: string[] = ['key', 'value'];
  imageSize;
  registrationResult: SimilarityReport;
  matCard = [
    {
      icon: 'info',
      color: 'primary',
      background: 'white',
      text: 'Select image using file picker below to preview'
    },
    {
      icon: 'info',
      color: 'primary',
      background: 'white',
      text: 'To register image, click on register button'
    },
    {
      icon: 'warning',
      color: 'accent',
      background: '#fff3cd',
      text: 'Image might be too big to fit in storage'
    },
    {
      icon: 'error',
      color: 'warn',
      background: '#f8d7da',
      text: 'Error'
    },
    {
      icon: 'check_circle',
      color: 'success',
      background: '#d4edda',
      text: 'Registration successful'
    }
  ];
  currentCard = this.matCard[0];

  constructor(private fb: FormBuilder, private imageService: ImageService, private loginService: LoginService) { }

  @ViewChild(MatPaginator) paginator: MatPaginator;

  ngOnInit(): void {
    this.uploadForm = this.fb.group({
      image: []
    })
  }

  onChange(form) {
    console.log(form);
    if (form.image === null) {
      this.dataSource = new MatTableDataSource<MetadataElement>();
      this.imageSize = 0;
      this.noImage = true;
      this.currentCard = this.matCard[0];
      return;
    }
    console.log("On change", this.uploadForm.get('image'));
    this.resetCards();
    this.currentCard = this.matCard[1];
    this.loading = true;
    this.loading2 = true;
    //this.loading3 = true;
    const data = new FormData();
    data.append('image', this.uploadForm.get('image').value._files[0]);
    console.log(data);
    this.imageService.getKeypoitsImage(data).subscribe(data => {
      console.log("data", data);
      this.createImageFromBlob(data);
      if (this.currentCard.icon === 'info') {
        this.currentCard = this.matCard[1];
      }
      this.loading = false;
      this.noImage = false;
    }, error => {
      this.loading = false;
      this.currentCard = this.matCard[3];
      this.currentCard.text = this.currentCard.text + ': Error in obtaining key points: ' + error.message;
      this.timer();
      console.log("error", error);
    });
    this.imageService.getMetadata(data).subscribe((data: Metadata) => {
      this.metadata = data;
      this.dataSource.data = this.metadata.metadata;
      this.dataSource.paginator = this.paginator;
      this.loading2 = false;
    }, error => {
      this.loading2 = false;
      this.currentCard = this.matCard[3];
      this.currentCard.text = this.currentCard.text + ': Error in obtaining metadata: ' + error.message;
      this.timer();
      console.log("error", error);
    });
  }

  onSubmit(form) {
    if (form.image === null) return;
    this.loading4 = true;
    const user = this.loginService.getIdentity();
    const data = new FormData();
    data.append('image', this.uploadForm.get('image').value._files[0]);
    data.append('displayName', user.name);
    data.append('email', user.emails[0]);
    console.log(data);
    this.imageService.registerImage(data).subscribe(data => {
      console.log(data);
      this.registrationResult = data;
      if (this.registrationResult.registered) {
        this.currentCard = this.matCard[4];
      } else if (!this.registrationResult.passed) {
        this.currentCard = this.matCard[3];
        this.imageService.result = this.registrationResult;
        this.currentCard.text = 'Registration not successful. Image has not passed similarity check.'
      } else {
        this.currentCard = this.matCard[3];
        this.currentCard.text = 'Registration not successful.';
      }
      this.loading4 = false;
    }, error => {
      this.loading4 = false;
      console.log('registration error', error);
      this.currentCard = this.matCard[3];
      this.currentCard.text = 'Registration not successful. Error: ' + error.message;
    })
  }

  /**
   * Image display https://stackoverflow.com/questions/45530752/getting-image-from-api-in-angular-4-5
   * @param image
   */
  private createImageFromBlob(image: Blob) {
    let reader = new FileReader();
    reader.addEventListener("load", () => {
      this.imageToDisplay = reader.result;
    }, false);

    if (image) {
      reader.readAsDataURL(image);
    }
  }

  private async timer() {
    await this.delay(10000);
    this.currentCard = this.matCard[0];
  }

  delay(ms: number) {
    return new Promise( resolve => setTimeout(resolve, ms) );
  }

  private resetCards() {
    this.matCard = [
      {
        icon: 'info',
        color: 'primary',
        background: 'white',
        text: 'Select image using file picker below to preview'
      },
      {
        icon: 'info',
        color: 'primary',
        background: 'white',
        text: 'To register image, click on register button'
      },
      {
        icon: 'warning',
        color: 'accent',
        background: '#fff3cd',
        text: 'Image might be too big to fit in storage'
      },
      {
        icon: 'error',
        color: 'warn',
        background: '#f8d7da',
        text: 'Error'
      },
      {
        icon: 'check_circle',
        color: 'primary',
        background: '#d4edda',
        text: 'Registration successful'
      }
    ];
  }

}
