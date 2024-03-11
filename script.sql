create table account
(
    account_id      int auto_increment
        primary key,
    account_name    varchar(255) null,
    password        varchar(255) null,
    phone           varchar(255) null,
    email           varchar(255) null,
    type            int          null,
    public_key      varchar(255) null,
    file            varchar(255) null,
    enterprise_type int          null,
    secret_key      varchar(255) null,
    t_coin          double       null comment '炭币数'
)
    comment '用户表';

create table accounting_record
(
    id                  int auto_increment comment '记录的id'
        primary key,
    enterprise_id       int          null comment '对应企业的id',
    month               varchar(64)  null comment '该碳核算记录对应的月份',
    time                timestamp    null comment '生成该记录的具体时间',
    state               int          null comment '0表示通过审核，1表示等待审核，2表示被拒绝',
    variable_json       varchar(800) null comment '用来描述计算过程所需参数的json字符串',
    result              varchar(100) null comment '碳核算结果',
    supporting_material varchar(100) null comment '存文件保存路径',
    conductor_id        int          null comment '处理这条请求的数据审核员的id'
)
    comment '碳核算请求';

create table cmessage
(
    account_id int         not null
        primary key,
    t_remain   double      null,
    month      varchar(64) null,
    `limit`    double      null comment '额度'
);

create table quota_sale
(
    id         int auto_increment
        primary key,
    quota      double      null comment '出售的额度',
    seller_id  int         null comment '售者id',
    unit_price double      null comment '单价',
    month      varchar(64) null
)
    comment '在售额度';

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
    conductor_id            int          null comment '处理这条申请的管理员的id',
    state                   int          null
)
    comment '注册申请表';

create table transaction
(
    transaction_id int auto_increment
        primary key,
    amount         double    null,
    buyer_id       int       null,
    sale_id        int       null comment '对应quota_sale的id，这里就不设外键了',
    cost           double    null,
    complete_time  timestamp null
)
    comment '交易表';

create table update_account
(
    account_id      int auto_increment
        primary key,
    account_name    varchar(255) null,
    password        varchar(255) null,
    phone           varchar(255) null,
    email           varchar(255) null,
    type            int          null,
    enterprise_type int          null,
    file            varchar(255) null,
    conductor_id    int          null comment '处理这条申请的管理员的id'
)
    comment '修改信息申请表';


