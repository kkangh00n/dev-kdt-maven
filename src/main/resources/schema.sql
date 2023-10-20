CREATE TABLE customers
(
    customer_id     BINARY(16)  PRIMARY KEY,
    name            varchar(20) not null,
    email           varchar(50) not null,
    last_login_at   datetime(6) default null,
    created_at       datetime(6) not null default CURRENT_TIMESTAMP(6),
    CONSTRAINT ung_user_email UNIQUE (email)
);