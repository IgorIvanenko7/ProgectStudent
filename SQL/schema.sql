--//-- Create users table
drop table if exists users;
create table users (
   id serial primary key,
   username varchar (255) not null unique
);

--//-- Create payment table
drop table if exists payments;
create table payments (
   id serial primary key,
   idUser int not null references users on delete cascade,
   sumPay numeric null,
   datePay timestamp
);

--//-- Create limit table
drop table if exists limits;
create table limits (
   id serial primary key,
   idUser int not null references users on delete cascade,
   sumlimit numeric null,
   dateinstall timestamp
);

--//-- insert example Data
insert into users(username) values ('User1'), ('User2'), ('User3');

insert into limits(idUser, sumlimit, dateinstall)
	values ((select id from users where username = 'User1'), 10000.00, current_timestamp);
insert into limits(idUser, sumlimit, dateinstall)
	values ((select id from users where username = 'User2'), 10000.00, current_timestamp);
insert into limits(idUser, sumlimit, dateinstall)
	values ((select id from users where username = 'User3'), 10000.00, current_timestamp);