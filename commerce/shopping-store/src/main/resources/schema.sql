CREATE TABLE IF NOT EXISTS products (
    id UUID,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(2000) NOT NULL,
    image_src VARCHAR(200),
    quantity_state VARCHAR(200) NOT NULL,
    product_state VARCHAR(200) NOT NULL,
    rating DOUBLE PRECISION,
    category VARCHAR(200) NOT NULL,
    price DOUBLE PRECISION NOT NULL
);