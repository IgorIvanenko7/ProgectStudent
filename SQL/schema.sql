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
   typeProduct user_product_type not null
);

