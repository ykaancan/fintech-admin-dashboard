CREATE TABLE portfolio_snapshots (
    id              UUID           PRIMARY KEY DEFAULT gen_random_uuid(),
    portfolio_id    UUID           NOT NULL REFERENCES portfolios(id) ON DELETE CASCADE,
    total_value     DECIMAL(19, 4) NOT NULL,
    total_cost      DECIMAL(19, 4) NOT NULL,
    profit_loss     DECIMAL(19, 4) NOT NULL,
    snapshot_date   DATE           NOT NULL,
    created_at      TIMESTAMP      NOT NULL DEFAULT NOW(),
    UNIQUE (portfolio_id, snapshot_date)
);

CREATE INDEX idx_portfolio_snapshots_portfolio_id ON portfolio_snapshots (portfolio_id);
CREATE INDEX idx_portfolio_snapshots_snapshot_date ON portfolio_snapshots (snapshot_date);
