CREATE TABLE charge
(
    id        varchar(36) not null
        constraint pk_batch_result
            primary key,
    imported_on    TIMESTAMP,
    imported_count integer
);

CREATE TABLE book
(
    id        varchar(36) not null
        constraint pk_book
            primary key,
    name      varchar(255) unique,
--     author_id varchar(255),
    borrowed BOOLEAN,
    charge_id varchar(36),
    CONSTRAINT fk_charge
        FOREIGN KEY (charge_id)
            REFERENCES charge (id)
);