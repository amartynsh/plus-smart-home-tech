create TABLE IF NOT EXISTS  carts(
id UUID PRIMARY KEY,
username VARCHAR NOT NULL,
status BOOlEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS products_orders(
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity BIGINT NOT NULL,
    CONSTRAINT products_orders_pk PRIMARY KEY (order_id, product_id),
    CONSTRAINT fk_order_id FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);