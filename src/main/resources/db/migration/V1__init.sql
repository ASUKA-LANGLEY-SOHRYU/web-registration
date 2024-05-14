create table usr (
    id bigserial not null,
    email varchar(255) not null unique ,
    first_name varchar(255) not null ,
    last_name varchar(255),
    phone varchar(50),
    photo_id bigint,
    password varchar(255) not null,
    primary key (id)
);

create table user_role (
    user_id bigint not null,
    roles varchar(255) check (roles in ('USER','MASTER','CLIENT','ADMIN'))
);

create table activation_code(
    user_id bigint not null,
    code varchar(10),
    primary key (user_id)
);

create table image(
    id bigserial not null,
    name varchar(255) not null,
    file oid not null,
    primary key (id)
);

create table client(
    id bigserial not null,
    user_id bigint not null,
    birthdate timestamp not null,
    primary key (id)
);

create table master(

);

alter table if exists user_role
    add constraint fk_user_role_user
    foreign key (user_id) references usr;

alter table if exists activation_code
    add constraint fk_activation_code_user
    foreign key (user_id) references usr;

alter table if exists usr
    add constraint fk_user_image
    foreign key (photo_id) references image;

alter table if exists client
    add constraint fk_client_user
    foreign key (user_id) references usr;