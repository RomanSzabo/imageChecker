# Image Checker Relational SQL Database
To allow store images with more features detected, separate, larger capacity storage 
was introduced to provide storage capacity due to Ethereum limitation of ~75kb per 
transaction.
### Contents
* _create.sql_ - create tables script
* _createAndAddDefaultTechUser.sql_ - sample script to create SQL Server login and
database User
* _drop.sql_ - drop tables script

### Runtime
This project used Azure SQL Server database. To create, simply create SQL Server DB 
and execute SQL scripts to create tables.<br>
The scripts should be working with SQL Server DB and probably (not tested) with others
as well.
### Azure SQL Access
To access from client, client IP address have to be added to Firewall rules (if it is not Azure 
service accessing). <br>
For adding user to DB, follow comments in the _createAndAddDefaultTechUser.sql_ script.