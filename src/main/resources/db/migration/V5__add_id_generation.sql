create sequence users_id_seq;
alter table users
    alter id set default nextval('users_id_seq');

create sequence roles_id_seq;
alter table roles
    alter id set default nextval('roles_id_seq');

create sequence profiles_id_seq;
alter table profiles
    alter id set default nextval('profiles_id_seq');