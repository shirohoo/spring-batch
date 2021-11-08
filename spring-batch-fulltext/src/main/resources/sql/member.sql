-- member
drop table if exists member;

create table member
(
    id      int primary key auto_increment,
    name    varchar(255),
    age     int,
    address varchar(255)
);

insert into
    member (name, age, address)
    values
        ('홍길동', 32, '서울');

insert into
    member (name, age, address)
    values
        ('아무개', 25, '인천');

insert into
    member (name, age, address)
    values
        ('스프링', 29, '경기도');
