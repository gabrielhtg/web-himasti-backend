SHOW TABLES;

SELECT * FROM users;

DESC users ;

ALTER TABLE users MODIFY COLUMN angkatan varchar(6);
ALTER TABLE users MODIFY COLUMN tgl_lahir bigint;
ALTER TABLE users MODIFY COLUMN last_name varchar(100);

CREATE TABLE register_token (
    register_token varchar(100)
);

ALTER TABLE users ADD COLUMN data_lengkap boolean default 0;

SELECT * FROM  register_token ;

DESC register_token ;

ALTER TABLE users MODIFY COLUMN admin boolean default 0;

DELETE from users WHERE username='rafael' ;

SELECT * FROM users;

UPDATE  users  set data_lengkap = 0 where username = "gabriel";

UPDATE users  SET foto_profil = null where username = "gabriel";


