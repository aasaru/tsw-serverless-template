CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL
);

CREATE TRIGGER set_updated_time
    BEFORE UPDATE ON users
    FOR EACH ROW
EXECUTE PROCEDURE trigger_set_updated_time();