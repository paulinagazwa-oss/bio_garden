--liquibase formatted sql
--changeset PAULINAGAZWA:6 context:development,production

ALTER TABLE bio_garden.user_account
    ADD COLUMN notifications_enabled BOOLEAN NOT NULL DEFAULT TRUE;

