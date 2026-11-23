CREATE TABLE users (
    id              UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    name            VARCHAR(150)     NOT NULL,
    email           VARCHAR(255)     NOT NULL,
    password        VARCHAR(100)     NOT NULL,
    social_id       VARCHAR(11)      NOT NULL,
    phone_number    VARCHAR(20)      NOT NULL,
    birth_date      DATE             NOT NULL,
    is_active       BIT              NOT NULL DEFAULT 1,
    role            VARCHAR(20)      NOT NULL DEFAULT 'CLIENT',

    created_at      DATETIME2        NOT NULL DEFAULT SYSDATETIME(),
    updated_at      DATETIME2        NOT NULL DEFAULT SYSDATETIME(),

    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT uk_users_social_id UNIQUE (social_id),
    CONSTRAINT ck_users_role CHECK (role IN ('CLIENT', 'ADMIN'))
);
