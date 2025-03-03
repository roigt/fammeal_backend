ALTER TABLE users
    DROP CONSTRAINT uc_users_email;

ALTER TABLE users
    DROP CONSTRAINT uc_users_username;

CREATE UNIQUE INDEX IF NOT EXISTS unique_users_email ON users(email) WHERE deleted = false;

CREATE UNIQUE INDEX IF NOT EXISTS unique_users_username ON users(username) WHERE deleted = false;
