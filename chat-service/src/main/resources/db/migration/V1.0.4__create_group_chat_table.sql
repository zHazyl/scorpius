create table if not exists group_chat
(
    id           bigint     not null primary key auto_increment,
    group_name    varchar(100) not null,
    created_date datetime(6) not null
);