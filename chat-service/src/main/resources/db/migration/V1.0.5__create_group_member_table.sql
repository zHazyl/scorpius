create table if not exists group_member
(
    id           bigint     not null primary key auto_increment,
    group_id bigint not null,
    member_id    binary(16) not null,
    is_admin boolean not null default 0,
    foreign key (group_id) references group_chat(id),
    foreign key (member_id) references chat_profile (user_id),
    unique index (group_id, member_id)
);