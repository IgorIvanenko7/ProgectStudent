--//-- Create users table
drop table if exists users;
create table users (
   id serial primary key,
   username varchar (255) not null unique
);

--//-- Create product table
drop table if exists products;
create table products (
   id serial primary key,
   idUser int not null references users on delete cascade,
   numberCount bigint null,
   balans numeric null,
   typeProduct varchar (255) not null,
   unique(id, typeProduct)
);

--//-- insert example Data
insert into users(username) values ('User1'), ('User2'), ('User3');

insert into products(idUser, numberCount, balans, typeProduct)
	values ((select id from users where username = 'User1'), 77755789878, 777.78, 'CARD');
insert into products(idUser, numberCount, balans, typeProduct)
	values((select id from users where username = 'User1'), 17755789878, 577.78, 'COUNT');

insert into products(idUser, numberCount, balans, typeProduct)
	values ((select id from users where username = 'User2'), 57755789878, 1777.78, 'CARD');
insert into products(idUser, numberCount, balans, typeProduct)
	values ((select id from users where username = 'User2'), 55755789878, 3577.78, 'COUNT');

insert into products(idUser, numberCount, balans, typeProduct)
	values ((select id from users where username = 'User3'), 87755789778, 5777.78, 'CARD');
insert into products(idUser, numberCount, balans, typeProduct)
	values ((select id from users where username = 'User3'), 58755789778, 7577.78, 'COUNT');
--//--------------------------------
