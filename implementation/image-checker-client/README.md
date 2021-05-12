# Image Checker Client
Angual Based client with Angular Material styling. 

## Azure AD B2C
Azure B2C for User handling. Azure/Microsoft MSAL Angular library is used.
### Registration in Azure Portal
https://docs.microsoft.com/en-us/azure/active-directory-b2c/tutorial-create-tenant <br>
https://docs.microsoft.com/en-us/azure/active-directory-b2c/tutorial-register-applications?tabs=app-reg-ga <br>
https://docs.microsoft.com/en-us/azure/active-directory-b2c/tutorial-create-user-flows <br>

After creation of AAD B2C tenant, you can register your application. Only HTTPS redirect URL's are 
accepted (except localhost). Application is registered as Web application. Further Setup or 
adjustment can be done. Screenshots provide information about setup of ImageChecker B2C configuration
in Azure portal - Authentication, API Permissions (needs to be granted by Administrator), User Flows.
<br>
Optional Branding and theme can be chosen as well.
### Angular SetUp
https://docs.microsoft.com/en-us/azure/active-directory/develop/tutorial-v2-angular <br>
https://docs.microsoft.com/en-us/samples/azure-samples/active-directory-javascript-singlepageapp-angular/active-directory-javascript-singlepageapp-angular/ <br>
https://docs.microsoft.com/en-us/azure/active-directory/develop/scenario-spa-overview <br>

Angual MSAL library is set up in app.module with basic configuration and in app.component with 
subscriptions login/logout/forgot passwords events/errors. Check also login.service which is used in application
for correct switching of user flows.<br>
Since MSAL Interceptor was not working for me, manual addition of token is performed in prepareOptions method of image.service (obtaining token from browser storage).

## Contents
### Modules
* About - About text and token Dialog if token is needed to try OpenAPI
* Check-Image - check image functionality 
* Header - Header Toolbar used in all components
* Home - Home component - after login
* Register-Image - register image page
* Welcome - public welcome page 
* app.module - Modules used in application
* app.component - application root, MSAL subscriptions goes here
* app-routing.module - in app routing, MSALGuard securing access to certain paths - only for logged in users
* image.service - communication to server (HTTP communication)
* interfaces.model - models/interface of Request/Response types of server
* login.service - service handling user flows, and login/logout handling

## Others
* assets - logo and images used in app
* angular.json - angular setup
* package.json - all npm dependencies used
* environments - values specific to some environment (local, vm), containing links to server, redirect, environment chosen at build time

### Build
If impoted, first time `npm install` is required to get all dependencies <br>
`ng build -c vm --output-path=dist-vm`

## Certificate for HTTPS Comminication
Free Let's Encrypt initiative certificate used (same for a backend is used as well). Obtained 
and installed according the instructions on Certbot web page (listed below).

## References
Logo generation: [https://favicon.io/](https://favicon.io/) <br>
SSL certificate: [https://certbot.eff.org/](https://certbot.eff.org/) <br>
Apache HTTP server: [http://httpd.apache.org/](http://httpd.apache.org/)


# Angular generated Readme

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 9.1.3.

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `--prod` flag for a production build.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI README](https://github.com/angular/angular-cli/blob/master/README.md).
