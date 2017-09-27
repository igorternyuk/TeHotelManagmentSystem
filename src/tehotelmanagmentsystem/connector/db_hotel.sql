/**
 * Author:  igor
 * Created: Jun 18, 2017
 */

/*create database db_hotel;
use db_hotel;

create table id_types(
    id int not null auto_increment primary key,
    name varchar(100)
);

create table rooms(
    id int not null auto_increment primary key,
    room_type enum('Single', 'Double', 'Family'),
    isFree boolean
);

create table customers (
    id_customer int not null auto_increment primary key,
    first_name varchar(100) not null,
    surname varchar(100),
    adress varchar(100),
    post_code varchar(100),
    mobile varchar(15),
    email varchar(40),
    nationality varchar(50),
    date_of_birth date,
    id_doc int not null,
    gender enum('M','F'),
    check_in_date date,
    check_out_date date,
    meal enum('NO_MEAL', 'ONE_MEAL', 'TWO_MEALS', 'THREE_MEALS'),
    room int not null,
    foreign key(room) references rooms(id),
    foreign key(id_doc) references id_types(id)
);

--drop table customers;

select * from customers;

select * from id_types;*/

select * from customers;