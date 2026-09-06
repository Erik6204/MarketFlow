ALTER TABLE payment_transactions
    ADD COLUMN payment_card_id BIGINT;

ALTER TABLE payment_transactions
    ADD CONSTRAINT fk_payment_transactions_payment_card
    FOREIGN KEY (payment_card_id)
    REFERENCES payment_cards (id)
    ON DELETE RESTRICT;

CREATE INDEX idx_payment_transactions_payment_card_id
    ON payment_transactions (payment_card_id);

ALTER TABLE payment_transactions
    DROP CONSTRAINT chk_payment_transactions_type;

ALTER TABLE payment_transactions
    ADD CONSTRAINT chk_payment_transactions_type
    CHECK (type IN (
        'PAYMENT',
        'SELLER_PAYOUT',
        'PLATFORM_COMMISSION',
        'REFUND',
        'SELLER_PAYOUT_REVERSAL',
        'PLATFORM_COMMISSION_REVERSAL'
    ));

UPDATE orders
SET status = 'CONFIRMED'
WHERE status = 'CREATED'
AND payment_status = 'PAID';
