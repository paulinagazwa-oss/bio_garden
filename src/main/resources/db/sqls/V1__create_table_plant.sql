--liquibase formatted sql
--changeset PAULINAGAZWA:1 context:development,production

CREATE SEQUENCE IF NOT EXISTS bio_garden.plant_seq_id  AS
    int  START  WITH  1
    INCREMENT  BY  1;

CREATE TABLE IF NOT EXISTS bio_garden.plant
(
    id bigint NOT NULL DEFAULT nextval('bio_garden.plant_seq_id'),
    name VARCHAR(255) NOT NULL,
    crop INTEGER NOT NULL,
    CONSTRAINT plant_pkey PRIMARY KEY (id)
);
