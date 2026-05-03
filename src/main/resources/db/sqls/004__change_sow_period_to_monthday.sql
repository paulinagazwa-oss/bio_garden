--liquibase formatted sql
--changeset PAULINAGAZWA:4 context:development,production

ALTER TABLE bio_garden.plant
    ALTER COLUMN sow_from TYPE VARCHAR(5) USING to_char(sow_from, 'MM-DD'),
    ALTER COLUMN sow_to TYPE VARCHAR(5) USING to_char(sow_to, 'MM-DD');
