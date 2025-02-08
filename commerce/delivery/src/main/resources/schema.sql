create TABLE IF NOT EXISTS addresses (
id UUID PRIMARY KEY,
country VARCHAR NOT NULL,
city VARCHAR NOT NULL,
street VARCHAR NOT NULL,
house VARCHAR NOT NULL,
flat VARCHAR NOT NULL
);

create TABLE IF NOT EXISTS deliveries (
id UUID PRIMARY KEY,
from_address_id UUID NOT NULL,
to_address_id UUID NOT NULL,
order_id UUID NOT NULL,
deliveryState VARCHAR NOT null,
CONSTRAINT fk_from_address_id FOREIGN KEY (from_address_id) REFERENCES addresses(id) ON delete CASCADE,
CONSTRAINT fk_to_address_id FOREIGN KEY (to_address_id) REFERENCES addresses(id) ON delete CASCADE
);
