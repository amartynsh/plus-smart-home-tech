create TABLE IF NOT EXISTS  orders(
id UUID PRIMARY KEY,
cart_id UUID NOT NULL,
payment_id UUID NOT NULL,
delivery_id UUID NOT NULL,
order_state VARCHAR(255),
delivery_weight DOUBLE PRECISION NOT NULL,
delivery_volume DOUBLE PRECISION NOT NULL,
fragile BOOLEAN NOT NULL,
total_price DOUBLE PRECISION NOT NULL,
delivery_price DOUBLE PRECISION NOT NULL,
product_price DOUBLE PRECISION NOT NULL
);

CREATE TABLE IF NOT EXISTS products_orders(
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity BIGINT NOT NULL,
    CONSTRAINT products_orders_pk PRIMARY KEY (order_id, product_id),
    CONSTRAINT fk_order_id FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);