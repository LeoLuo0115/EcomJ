--use skillup;
--create table user(
--user_id varchar(32) not null,
--password varchar(32) not null,
--primary key (user_id)
--) ENGINE = InnoDB charset = utf8mb4;
--
--alter table user add user_name varchar(200) not null unique;
--alter table user modify user_name varchar(400) not null unique;
--alter table user change user_name account_name varchar(400) not null unique;
--insert into user (password,user_id,account_name) values ("Fantasy","1","Jay");
--
--select account_name password from user where user_id = "1";
--
--update user set password = "123"

CREARE Table user(
user_id varchar(36) not null,
user_name varchar(100) not null unique,
password varchar(32) not null,
primary key(user_id)
)ENGINE = InnoDB CHARSET = utf8mb4;

INSERT into user(user_id,user_name,password) values ("1","test","test");