--liqubase formatted sql
--changeset PAULINAGAZWA:5 context:development,production

CREATE TABLE bio_garden.user_account (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(320),
    username VARCHAR(100),
    password_hash VARCHAR(255) NOT NULL,
    latitude DECIMAL(8, 5),
    longitude DECIMAL(8, 5),
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_update_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX ux_user_account_email
    ON bio_garden.user_account (LOWER(email));

CREATE UNIQUE INDEX ux_user_account_username
    ON bio_garden.user_account (LOWER(username));
