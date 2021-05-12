# Image Checker

## About
Image checker is university project focused on collecting and storing
relevant data for a picture in block chain with possibility to check new images against 
registered (image "plagiarism" checker).
## Repository structure
All source code parts are stored under _implementation_ folder. 

## Technology Stack
The application consists of **server**, **client**, **smart contracts** and **relational SQL Database**. <br>
**Client**: AngularJS client with Angular Material theming <br>
**Server**: Spring Boot REST API server <br>
**Smart Contracts**: Solidity contracts for integrity - storing fingerprints of data in database <br>
**Relational SQL Database**: Relational SQL Server database for storing data<br><br>
_Note: Further details can be found in respective folders_
### Runtime Environmnet
**Microsoft Azure**<br>
**Client**: Ubuntu Virtual Machine - Standard Apache HTTP installed and serving build output of angular on port 80/443 <br>
**Server**: Ubuntu Virtual Machine - running SpringBoot server on port 8443 <br>
**Smart Contracts**: Ethereum Proof-of-Authority Consortium offering from Azure<br>
**SQL Database**: Azure SQL Server

#### Etherchain Lite (Block Chain explorer)
Git hub pull of Etherchain lite is in `~/epirus-blockchain-explorer/etherchain-light` folder. To run just run `npm start` command in a new screen.


