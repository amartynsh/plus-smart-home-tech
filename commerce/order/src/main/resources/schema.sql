create TABLE IF NOT EXISTS  addresses (
id UUID PRIMARY KEY ,
country VARCHAR(255) NOT NULL,
city VARCHAR(255) NOT NULL,
street VARCHAR(255) NOT NULL,
house VARCHAR(255) NOT NULL,
flat VARCHAR(255) NOT NULL
);

create TABLE IF NOT EXISTS  orders(
id UUID PRIMARY KEY,
username varchar(255) NOT NULL,
cart_id UUID NOT NULL,
payment_id UUID NOT NULL,
delivery_id UUID NOT NULL,
order_state VARCHAR(255),
delivery_weight DOUBLE PRECISION NOT NULL,
delivery_volume DOUBLE PRECISION NOT NULL,
fragile BOOLEAN NOT NULL,
total_price DOUBLE PRECISION NOT NULL,
delivery_price DOUBLE PRECISION NOT NULL,
product_price DOUBLE PRECISION NOT NULL,
address_id UUID NOT NULL,
CONSTRAINT fk_adress_id FOREIGN KEY (address_id) REFERENCES addresses(id) ON delete CASCADE
);

create TABLE IF NOT EXISTS products_orders(
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity INTEGER NOT NULL,
    CONSTRAINT products_orders_pk PRIMARY KEY (order_id, product_id),
    CONSTRAINT fk_order_id FOREIGN KEY (order_id) REFERENCES orders(id) ON delete CASCADE
);