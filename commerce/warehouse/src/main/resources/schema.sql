create TABLE IF NOT EXISTS warehouse_products (
    id   UUID PRIMARY KEY,
    fragile  boolean,
    width DOUBLE PRECISION,
    height DOUBLE PRECISION,
    depth DOUBLE PRECISION,
    weight DOUBLE PRECISION,
    quantity INTEGER
);
create TABLE IF NOT EXISTS products_bookings (
   booking_id UUID NOT NULL,
   product_id UUID NOT NULL,
   quantity BIGINT NOT NULL,
    CONSTRAINT products_orders_pk PRIMARY KEY (booking_id, product_id),
    CONSTRAINT fk_order_id FOREIGN KEY (booking_id) REFERENCES warehouse_products(id) ON delete CASCADE
);