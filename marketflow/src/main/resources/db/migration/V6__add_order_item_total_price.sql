ALTER TABLE order_items
    ADD COLUMN total_price NUMERIC(14, 2);

UPDATE order_items
SET total_price = unit_price * quantity;

ALTER TABLE order_items
    ALTER COLUMN total_price SET NOT NULL;

ALTER TABLE order_items
    ADD CONSTRAINT chk_order_items_total_price
    CHECK (total_price > 0);
