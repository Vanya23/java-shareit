drop table IF EXISTS comments;
drop table IF EXISTS bookings;
drop table IF EXISTS items;
drop table IF EXISTS requests;
drop table IF EXISTS users;

CREATE TABLE
    IF NOT EXISTS users
(
    id
    int8
    NOT
    NULL
    GENERATED
    BY
    DEFAULT AS
    IDENTITY,
    full_name
    varchar
(
    256
) NOT NULL,
    email varchar
(
    512
) NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY
(
    id
),
    CONSTRAINT users_un UNIQUE
(
    email
)
    );

CREATE TABLE
    IF NOT EXISTS requests
(
    id
    int8
    NOT
    NULL
    GENERATED
    BY
    DEFAULT AS
    IDENTITY,
    description
    varchar
(
    512
) NOT NULL,
    id_user int8 NOT NULL,
    create_req timestamp NOT NULL,
    CONSTRAINT requests_pk PRIMARY KEY
(
    id
),
    CONSTRAINT requests_fk FOREIGN KEY
(
    id_user
) REFERENCES users
(
    id
)
    );

CREATE TABLE
    IF NOT EXISTS items
(
    id
    int8
    NOT
    NULL
    GENERATED
    BY
    DEFAULT AS
    IDENTITY,
    full_name
    varchar
(
    256
) NOT NULL,
    description varchar
(
    512
) NOT NULL,
    available bool NOT NULL,
    id_user int8 NOT NULL,
    id_requests int8 NULL,
    CONSTRAINT items_pk PRIMARY KEY
(
    id
),
    CONSTRAINT items_fk FOREIGN KEY
(
    id_user
) REFERENCES users
(
    id
),
    CONSTRAINT items_fk_2 FOREIGN KEY
(
    id_requests
) REFERENCES requests
(
    id
)
    );

CREATE TABLE
    IF NOT EXISTS bookings
(
    id
    int8
    NOT
    NULL
    GENERATED
    BY
    DEFAULT AS
    IDENTITY,
    start_booking
    timestamp
    NOT
    NULL,
    end_booking
    timestamp
    NOT
    NULL,
    id_item
    int8
    NOT
    NULL,
    id_user
    int8
    NOT
    NULL,
    status
    varchar
    NOT
    NULL,
    CONSTRAINT
    bookings_pk
    PRIMARY
    KEY
(
    id
),
    CONSTRAINT bookings_fk FOREIGN KEY
(
    id_item
) REFERENCES items
(
    id
),
    CONSTRAINT bookings_fk_1 FOREIGN KEY
(
    id_user
) REFERENCES users
(
    id
)
    );

CREATE TABLE
    IF NOT EXISTS comments
(
    id
    int8
    NOT
    NULL
    GENERATED
    BY
    DEFAULT AS
    IDENTITY,
    text
    varchar
(
    1024
) NOT NULL,
    id_item int8 NOT NULL,
    id_user int8 NOT NULL,
    created_date timestamp NOT NULL,
    CONSTRAINT comments_pk PRIMARY KEY
(
    id
),
    CONSTRAINT comments_fk FOREIGN KEY
(
    id_item
) REFERENCES items
(
    id
),
    CONSTRAINT comments_fk_1 FOREIGN KEY
(
    id_user
) REFERENCES users
(
    id
)
    );