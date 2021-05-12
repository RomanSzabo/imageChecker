-- Execute against master db to create login for user
create login <user> with password '<password>';

-- Execute against image checker db to create user in db for login
create user <user> from login <user>

-- Execute against image checker db, add read+write role
alter role db_datareader add member <user>
alter role db_datawriter add member <user>