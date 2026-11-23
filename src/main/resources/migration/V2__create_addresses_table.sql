CREATE TABLE addresses (
    id          UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    user_id     UNIQUEIDENTIFIER NOT NULL,
    street      VARCHAR(255)     NOT NULL,
    city        VARCHAR(100)     NOT NULL,
    number      INT              NOT NULL,
    state       VARCHAR(100)     NOT NULL,
    zip_code    VARCHAR(20)      NOT NULL,
    country     VARCHAR(100)     NOT NULL,
    created_at  DATETIME2        NOT NULL DEFAULT SYSDATETIME(),
    updated_at  DATETIME2        NOT NULL DEFAULT SYSDATETIME(),

    CONSTRAINT fk_addresses_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_addresses_user_id ON addresses (user_id);
