create TABLE IF NOT EXISTS warehouse_products (
    id   UUID PRIMARY KEY,
    fragile  boolean,
    width DOUBLE PRECISION,
    height DOUBLE PRECISION,
    depth DOUBLE PRECISION,
    weight DOUBLE PRECISION,
    quantity INTEGER
);