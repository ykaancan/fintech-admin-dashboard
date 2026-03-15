CREATE TABLE positions (
    id              UUID          PRIMARY KEY DEFAULT gen_random_uuid(),
    portfolio_id    UUID          NOT NULL REFERENCES portfolios(id) ON DELETE CASCADE,
    asset_id        UUID          NOT NULL REFERENCES assets(id) ON DELETE RESTRICT,
    quantity        DECIMAL(19, 8) NOT NULL,
    avg_cost_basis  DECIMAL(19, 4) NOT NULL,
    created_at      TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP     NOT NULL DEFAULT NOW(),
    UNIQUE (portfolio_id, asset_id)
);

CREATE INDEX idx_positions_portfolio_id ON positions (portfolio_id);
CREATE INDEX idx_positions_asset_id ON positions (asset_id);
