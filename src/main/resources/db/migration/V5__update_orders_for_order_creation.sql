ALTER TABLE orders
    DROP CONSTRAINT chk_orders_status;

UPDATE orders
SET status = 'PROCESSING'
WHERE status = 'IN_TRANSIT';

UPDATE orders
SET status = 'COMPLETED'
WHERE status = 'DELIVERED';

ALTER TABLE orders
    ALTER COLUMN status SET DEFAULT 'CREATED';

ALTER TABLE orders
    ADD CONSTRAINT chk_orders_status
    CHECK (status IN (
        'CREATED',
        'CONFIRMED',
        'PROCESSING',
        'COMPLETED',
        'CANCELLED'
    ));

ALTER TABLE orders
    ADD COLUMN updated_at TIMESTAMP WITH TIME ZONE;

UPDATE orders
SET updated_at = created_at;

ALTER TABLE orders
    ALTER COLUMN updated_at SET DEFAULT CURRENT_TIMESTAMP,
    ALTER COLUMN updated_at SET NOT NULL;
