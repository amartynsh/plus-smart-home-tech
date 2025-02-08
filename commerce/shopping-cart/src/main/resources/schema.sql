create TABLE IF NOT EXISTS  carts(
id UUID PRIMARY KEY,
username VARCHAR NOT NULL,
status BOOlEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS products_orders(
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity BIGINT NOT NULL,
    CONSTRAINT carts_pk PRIMARY KEY (order_id, product_id),
    CONSTRAINT fk_order_id FOREIGN KEY (order_id) REFERENCES carts(id) ON DELETE CASCADE
);