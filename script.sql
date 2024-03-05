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

create table update_account
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

create table accounting_record
(
    id                  int auto_increment comment '记录的id'
        primary key,
    enterprise_id       int          null comment '对应企业的id',
    month               int          null comment '该碳核算记录对应的月份',
    time                timestamp    null comment '生成该记录的具体时间',
    state               int          null comment '0表示通过审核，1表示等待审核，2表示被拒绝',
    variable_json       varchar(800) null comment '用来描述计算过程所需参数的json字符串',
    result              int          null comment '碳核算结果',
    supporting_material varchar(100) null comment '存文件保存路径'
)
    comment '碳核算记录';
