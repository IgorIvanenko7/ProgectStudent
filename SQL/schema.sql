-- schema.sql
-- Since we might run the import many times we'll drop if exists

drop table if exists users;
create table users (
   id serial primary key,
   username varchar (255) not null unique
);

--//-- Create type
drop type if exists user_product_type;
create type user_product_type as enum ('COUNT', 'CARD');

--//-- Create product table
drop table if exists products;
create table products (
   id int not null references users on delete cascade,
   numberCount bigint not null,
   balans numeric null,
   typeProduct user_product_type not null,
   unique(id, typeProduct)
);

--//-- Insert example Data
insert into users(username) values ('User1'), ('User2'), ('User3');

insert into products values ((select id from users where username = 'User1'), 77755789878 :: bigint, 777.78 :: numeric , 'CARD' :: user_product_type);
insert into products values ((select id from users where username = 'User1'), 17755789878 :: bigint, 577.78 :: numeric , 'COUNT' :: user_product_type);

insert into products values ((select id from users where username = 'User2'), 57755789878 :: bigint, 1777.78 :: numeric , 'CARD' :: user_product_type);
insert into products values ((select id from users where username = 'User2'), 55755789878 :: bigint, 3577.78 :: numeric , 'COUNT' :: user_product_type);

insert into products values ((select id from users where username = 'User3'), 87755789778 :: bigint, 5777.78 :: numeric , 'CARD' :: user_product_type);
insert into products values ((select id from users where username = 'User3'), 58755789778 :: bigint, 7577.78 :: numeric , 'COUNT' :: user_product_type);
--//--------------------------------

