ALTER TABLE orders
    ADD COLUMN payment_status VARCHAR(30)
    NOT NULL DEFAULT 'NOT_PAID';

ALTER TABLE orders
    ADD CONSTRAINT chk_orders_payment_status
    CHECK (payment_status IN (
        'NOT_PAID',
        'PROCESSING',
        'PAID',
        'FAILED',
        'REFUNDED'
    ));
