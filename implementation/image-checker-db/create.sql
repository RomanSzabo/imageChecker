create table users
(
    userId       varchar(50),
    display_name varchar(200) not null,
    email        varchar(200) not null unique,
    created      datetime     not null,
    modified     datetime,
    constraint users_pk primary key (userId)
);

create table image
(
    imageId            varchar(50),
    imageOwner         varchar(50),
    imageObjectModel   varbinary(max) not null,
    imageMetadataModel varbinary(max) not null,
    created            datetime       not null,
    constraint image_pk primary key (imageId),
    constraint image_owner_fk foreign key (imageOwner) references users,
);

create table messages
(
    messageId varchar(50),
    sender    varchar(120),
    receiver  varchar(50),
    image     varchar(50),
    text      varchar(MAX),
    created   datetime,
    constraint message_pk primary key (messageId),
    constraint messages_user_fk foreign key (receiver) references users,
    constraint messages_image_fk foreign key (image) references image
)