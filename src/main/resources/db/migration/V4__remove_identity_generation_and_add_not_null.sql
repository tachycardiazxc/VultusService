--removing generated default as identity

alter table users
    alter id drop identity,
    alter id drop default,
    alter password set not null,
    alter username set not null;

alter table roles
    ALTER id DROP identity,
    alter id drop default;

alter table profiles
    ALTER id DROP identity,
    alter id drop default,
    alter user_id set NOT NULL,
    add unique (user_id),
    alter first_name set not null,
    alter last_name set not null,
    alter phone set not null,
    add unique (phone),
    alter birthday set not null,
    alter position set not null,
    alter subdivision set not null,
    alter work_phone set not null,
    alter work_place set not null,
    alter description set not null,
    alter status_name set not null,
    alter status_text set not null,
    alter super_busy set not null;

--adding not null and unique
