create table usr
(
    id bigint auto_increment
        primary key,
    created date not null,
    email varchar(255) not null,
    password varchar(255) not null,
    role varchar(255) null,
    status varchar(255) null,
    updated date not null,
    username varchar(255) not null,
    constraint UK_dfui7gxngrgwn9ewee3ogtgym
        unique (username),
    constraint UK_g9l96r670qkidthshajdtxrqf
        unique (email)
);

create table reports
(
    id bigint auto_increment
        primary key,
    created date not null,
    decline_reason text null,
    description text not null,
    name varchar(255) not null,
    status varchar(255) null,
    updated date not null,
    owner_id bigint null,
    constraint FKs832c5n3x9r9oh34u4motabk2
        foreign key (owner_id) references usr (id)
);

create table report_inspectors
(
    report_id bigint not null,
    usr_id bigint not null,
    constraint FK1k07ywwj71d53r65b8axm5o2m
        foreign key (report_id) references reports (id),
    constraint FK1t2hwrjr8hk8bpt8dq69v7s66
        foreign key (usr_id) references usr (id)
);

create table archive
(
    id bigint auto_increment
        primary key,
    inspector_decision_id bigint null,
    report_id bigint null,
    description varchar(255) null,
    name varchar(255) null,
    created date not null,
    updated date not null,
    decline_reason varchar(255) null,
    status varchar(255) null,
    constraint FK3d2exxgarlelxmd4blmcho7t8
        foreign key (inspector_decision_id) references usr (id),
    constraint FKhxoedsu9fsxxtcf08tp3j44dx
        foreign key (report_id) references reports (id)
);

