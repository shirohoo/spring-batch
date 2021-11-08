-- person
drop table if exists person;

create table person
(
    id      bigint primary key auto_increment,
    address varchar(255),
    age     int,
    name    varchar(255)
);

insert into
    person (name, age, address)
    values
        ('홍길동', 32, '서울');

insert into
    person (name, age, address)
    values
        ('아무개', 25, '인천');

insert into
    person (name, age, address)
    values
        ('스프링', 29, '경기도');
