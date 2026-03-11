CREATE TABLE transactions (
    id              UUID           PRIMARY KEY DEFAULT gen_random_uuid(),
    portfolio_id    UUID           NOT NULL REFERENCES portfolios(id) ON DELETE CASCADE,
    asset_id        UUID           NOT NULL REFERENCES assets(id) ON DELETE RESTRICT,
    type            VARCHAR(20)    NOT NULL CHECK (type IN ('BUY', 'SELL', 'DEPOSIT', 'WITHDRAWAL')),
    quantity        DECIMAL(19, 8) NOT NULL,
    price           DECIMAL(19, 4) NOT NULL,
    fee             DECIMAL(19, 4) NOT NULL DEFAULT 0,
    total_amount    DECIMAL(19, 4) NOT NULL,
    notes           TEXT,
    executed_at     TIMESTAMP      NOT NULL,
    created_at      TIMESTAMP      NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_transactions_portfolio_id ON transactions (portfolio_id);
CREATE INDEX idx_transactions_asset_id ON transactions (asset_id);
CREATE INDEX idx_transactions_type ON transactions (type);
CREATE INDEX idx_transactions_executed_at ON transactions (executed_at);
