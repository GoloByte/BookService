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
    borrowed  BOOLEAN,
    charge_id varchar(36),
    CONSTRAINT fk_charge
        FOREIGN KEY (charge_id)
            REFERENCES charge (id)
);

CREATE TABLE author
(
    id        varchar(36) not null
        constraint pk_author
            primary key,
    name      varchar(255) unique
);

CREATE TABLE book_author
(
    book_id   varchar(36),
    author_id varchar(36),
    constraint fk_book
        foreign key (book_id)
            references book (id),
    constraint fk_author
        foreign key (author_id)
            references author (id),
    primary key (book_id, author_id)
);

