create TABLE IF NOT EXISTS  carts(
id UUID PRIMARY KEY,
username VARCHAR NOT NULL,
status BOOlEAN NOT NULL
);

create TABLE IF NOT EXISTS  products_carts(
cart_id UUID NOT NULL PRIMARY KEY,
product_id UUID NOT NULL PRIMARY KEY,
quantity INTEGER NOT NULL,
CONSTRAINT products_carts_product_id_fkey FOREIGN KEY (cart_id) REFERENCES shopping_carts(id) ON delete CASCADE
);