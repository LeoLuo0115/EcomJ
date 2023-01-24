CREATE TABLE user (
    user_id     varchar(36)     not null,
    password    varchar(32)    not null,
    primary key(user_id)
) ENGINE = InnoDB CHARSET=utf8mb4;

-- 常用sql语法 

ALTER TABLE user ADD account_name VARCHAR(200) not null unique;

ALTER TABLE user MODIFY column account_name VARCHAR(36) not null unique; 

ALTER TABLE user CHANGE column account_name user_name VARCHAR(36) not null unique;

ALTER TABLE user DROP photo;

INSERT INTO user (password, user_name, user_id) VALUES ('test', 'test', '1'); 

SELECT user_id FROM user WHERE user_name = "test";

SELECT * FROM user WHERE user_name = "test";

UPDATE user SET password = "123" where user_name = "test";