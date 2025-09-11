create table t_employee
(
    id          int auto_increment primary key,
    age         int             null,
    company_id  int             null,
    gender      varchar(255)    null,
    name        varchar(255)    null,
    salary      double          null,
    status      boolean         null
);