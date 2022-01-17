create table transaction (
    transaction_id BIGSERIAL not null,
    account_id varchar(10) not null,
    description varchar(255) null,
    transaction_type varchar(10) not null,
    transaction_date TIMESTAMP not null,
    amount numeric not null
)