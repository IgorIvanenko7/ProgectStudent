drop table if exists users;

create table users (
   id serial primary key,
   username varchar (255) unique
);