--liquibase formatted sql
--changeset PAULINAGAZWA:3 context:development,production

CREATE SEQUENCE IF NOT EXISTS bio_garden.plant_companion_seq_id AS
    int START WITH 1
    INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS bio_garden.plant_companion
(
    id BIGINT NOT NULL DEFAULT nextval('bio_garden.plant_companion_seq_id'),
    plant_id BIGINT NOT NULL,
    companion_plant_id BIGINT NOT NULL,
    relationship_type VARCHAR(20) NOT NULL,
    recommended_distance_cm INTEGER,
    bidirectional BOOLEAN NOT NULL DEFAULT true,
    CONSTRAINT plant_companion_pkey PRIMARY KEY (id),
    CONSTRAINT fk_plant_companion_plant FOREIGN KEY (plant_id) REFERENCES bio_garden.plant(id) ON DELETE CASCADE,
    CONSTRAINT fk_plant_companion_companion FOREIGN KEY (companion_plant_id) REFERENCES bio_garden.plant(id) ON DELETE CASCADE,
    CONSTRAINT unique_plant_companion_relationship UNIQUE (plant_id, companion_plant_id, relationship_type),
    CONSTRAINT check_not_self_companion CHECK (plant_id <> companion_plant_id)
);

CREATE INDEX IF NOT EXISTS idx_plant_companion_plant_id ON bio_garden.plant_companion(plant_id);
CREATE INDEX IF NOT EXISTS idx_plant_companion_companion_plant_id ON bio_garden.plant_companion(companion_plant_id);
CREATE INDEX IF NOT EXISTS idx_plant_companion_relationship_type ON bio_garden.plant_companion(relationship_type);

COMMENT ON TABLE bio_garden.plant_companion IS 'Neighbour relationships between plants in the garden';
COMMENT ON COLUMN bio_garden.plant_companion.plant_id IS 'ID of the main plant';
COMMENT ON COLUMN bio_garden.plant_companion.companion_plant_id IS 'ID of the companion plant';
COMMENT ON COLUMN bio_garden.plant_companion.relationship_type IS 'Type of relationship: GOOD (good neighbour), BAD (bad neighbour), NEUTRAL (neutral), COMPANION_ROW (can grow in the same row)';
COMMENT ON COLUMN bio_garden.plant_companion.recommended_distance_cm IS 'Recommended distance between plants in centimeters';
COMMENT ON COLUMN bio_garden.plant_companion.bidirectional IS 'Whether the relationship works both ways (symmetrically)';

