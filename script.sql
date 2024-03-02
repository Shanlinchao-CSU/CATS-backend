create table account
(
    account_id         int auto_increment
        primary key,
    account_name       varchar(255) null,
    password           varchar(255) null,
    phone              varchar(255) null,
    email              varchar(255) null,
    type               int          null,
    enterprise_type    int          null,
    enterprise_address varchar(255) null,
    file               varchar(255) null
);

create table cmessage
(
    account_id int not null
        primary key,
    climit     int null
);

create table register_application
(
    register_application_id int auto_increment
        primary key,
    account_name            varchar(255) null,
    password                varchar(255) null,
    phone                   varchar(255) null,
    email                   varchar(255) null,
    type                    int          null,
    file_address            varchar(255) null,
    enterprise_type         int          null,
    enterprise_address      varchar(255) null
);

create table transaction
(
    transaction_id int auto_increment
        primary key,
    t_from         varchar(255) null,
    t_to           varchar(255) null,
    amount         int          null,
    start_time     datetime     null,
    complete_time  datetime     null,
    state          tinyint(1)   null
);


