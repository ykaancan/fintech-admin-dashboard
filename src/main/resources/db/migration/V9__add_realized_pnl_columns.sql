-- Add realized P&L tracking to positions (cumulative) and transactions (per-trade)
ALTER TABLE positions
    ADD COLUMN realized_pnl DECIMAL(19, 4) NOT NULL DEFAULT 0;

ALTER TABLE transactions
    ADD COLUMN realized_pnl DECIMAL(19, 4);
