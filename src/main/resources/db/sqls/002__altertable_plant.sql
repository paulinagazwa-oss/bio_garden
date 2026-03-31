--liquibase formatted sql
--changeset PAULINAGAZWA:2 context:development,production

ALTER TABLE bio_garden.plant
    ADD COLUMN IF NOT EXISTS days_to_harvest INTEGER,
    ADD COLUMN IF NOT EXISTS creation_date TIMESTAMP,
    ADD COLUMN IF NOT EXISTS last_update_date TIMESTAMP,
    ADD COLUMN IF NOT EXISTS sow_from TIMESTAMP,
    ADD COLUMN IF NOT EXISTS sow_to TIMESTAMP;
