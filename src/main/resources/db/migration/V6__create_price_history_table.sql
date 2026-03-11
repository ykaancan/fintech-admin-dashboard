CREATE TABLE price_history (
    id              UUID           PRIMARY KEY DEFAULT gen_random_uuid(),
    asset_id        UUID           NOT NULL REFERENCES assets(id) ON DELETE CASCADE,
    price           DECIMAL(19, 4) NOT NULL,
    source          VARCHAR(100),
    recorded_at     TIMESTAMP      NOT NULL,
    created_at      TIMESTAMP      NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_price_history_asset_id_recorded_at ON price_history (asset_id, recorded_at);
