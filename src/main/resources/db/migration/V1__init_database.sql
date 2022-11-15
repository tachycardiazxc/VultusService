create table users
(
    id       int,
    username varchar(64),
    password varchar(64),

    constraint pk_users primary key (id)
);

create table roles
(
    id   int,
    name varchar(64),

    constraint pk_roles primary key (id)
);

create table user_roles
(
    user_id int,
    role_id int,

    constraint fk_user_id foreign key (user_id) references users (id),
    constraint fk_role_id foreign key (role_id) references roles (id)
);

create table profiles
(
    id          int,
    user_id     int,
    first_name  varchar(64),
    last_name   varchar(64),
    surname     varchar(64),
    phone       varchar(64),
    birthday    date,
    position    varchar(64),
    subdivision varchar(64),
    work_phone  varchar(64),
    work_place  int,
    description text,
    status_name varchar(32),
    status_text varchar(128),
    super_busy  boolean,
    photo_url   varchar(128),

    constraint pk_profiles primary key (id),
    constraint fk_user_id foreign key (user_id) references users (id)

)