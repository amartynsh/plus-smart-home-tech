create TABLE IF NOT EXISTS  carts(
id UUID PRIMARY KEY,
username VARCHAR NOT NULL,
status BOOlEAN NOT NULL
);

create TABLE IF NOT EXISTS  products_carts(
cart_id UUID NOT NULL,
product_id UUID NOT NULL,
quantity INTEGER NOT NULL,
CONSTRAINT carts_product_id_fkey FOREIGN KEY (cart_id) REFERENCES carts(id) ON delete CASCADE,
CONSTRAINT carts_pk PRIMARY KEY (cart_id, product_id)
);