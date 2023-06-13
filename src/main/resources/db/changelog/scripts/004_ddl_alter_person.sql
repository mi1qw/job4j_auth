alter table person
    add column address_id int references address (id);

update person
SET address_id = (select a.id from address a where a.country = 'Neverland')
where address_id IS NULL;

alter table person
    alter column address_id SET NOT NULL;

alter table person
    add constraint person_unique unique (login);

select setval('person_id_seq', 99);

-- alter table authorities add column id serial primary key;
-- alter table authorities alter column authority set not null;
-- alter table authorities add constraint auth_unique unique (authority);
--
-- alter table users drop constraint users_pkey;
-- alter table users add column id serial primary key;
-- alter table users add constraint user_unique unique (username);
-- alter table users add column authority_id int references authorities (id);
--
-- update users
-- SET authority_id =
--         (select a.id from authorities a where a.authority = 'ROLE_USER')
-- where authority_id IS NULL;
--
-- alter table users alter column authority_id SET NOT NULL;
