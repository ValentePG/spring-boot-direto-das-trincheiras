create table anime_service.anime
(
    id   bigint auto_increment
        primary key,
    name varchar(255) not null,
    constraint UKj7axrh0gq5c58xhjb6p0q7je1
        unique (name)
);

create table anime_service.producer
(
    created_at datetime(6)  not null,
    id         bigint auto_increment
        primary key,
    name       varchar(255) not null
);

