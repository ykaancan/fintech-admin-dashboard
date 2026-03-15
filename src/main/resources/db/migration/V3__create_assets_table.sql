CREATE TABLE assets (
    id                  UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    symbol              VARCHAR(20)  NOT NULL UNIQUE,
    name                VARCHAR(255) NOT NULL,
    asset_type          VARCHAR(20)  NOT NULL CHECK (asset_type IN ('CRYPTO', 'STOCK', 'BOND', 'ETF', 'COMMODITY', 'CASH')),
    current_price       DECIMAL(19, 4),
    currency            VARCHAR(10),
    last_price_update   TIMESTAMP,
    created_at          TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_assets_symbol ON assets (symbol);
CREATE INDEX idx_assets_asset_type ON assets (asset_type);
