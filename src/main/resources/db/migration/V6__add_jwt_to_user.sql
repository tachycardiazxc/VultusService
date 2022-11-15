alter table users
    add column if not exists jwt_token varchar(128) unique