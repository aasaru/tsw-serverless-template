CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users (id),
    amount NUMERIC NOT NULL,
    from_Currency VARCHAR(3) NOT NULL,
    to_Currency VARCHAR(3) NOT NULL,
    order_type VARCHAR(50) NOT NULL,
    created_time TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TRIGGER set_updated_time
    BEFORE UPDATE ON orders
    FOR EACH ROW
EXECUTE PROCEDURE trigger_set_updated_time();