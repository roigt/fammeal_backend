CREATE SEQUENCE IF NOT EXISTS tokens_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE homes
(
    id                       UUID    NOT NULL,
    name                     VARCHAR(255),
    vegetarian               BOOLEAN NOT NULL,
    lunchAutomaticGeneration BOOLEAN NOT NULL,
    dinerAutomaticGeneration BOOLEAN NOT NULL,
    CONSTRAINT pk_homes PRIMARY KEY (id)
);

CREATE TABLE permissions
(
    role    VARCHAR(255),
    home_id UUID NOT NULL,
    user_id UUID NOT NULL,
    CONSTRAINT pk_permissions PRIMARY KEY (home_id, user_id)
);

CREATE TABLE tokens
(
    id        BIGINT  NOT NULL,
    tokenType VARCHAR(255),
    token     VARCHAR(255),
    expiresAt TIMESTAMP WITHOUT TIME ZONE,
    used      BOOLEAN NOT NULL,
    user_id   UUID,
    CONSTRAINT pk_tokens PRIMARY KEY (id)
);

CREATE TABLE users
(
    id                UUID    NOT NULL,
    deleted           BOOLEAN NOT NULL,
    username          VARCHAR(255),
    email             VARCHAR(255),
    firstname         VARCHAR(255),
    lastname          VARCHAR(255),
    password          VARCHAR(255),
    profilePictureUrl VARCHAR(255),
    vegetarian        BOOLEAN NOT NULL,
    verified          BOOLEAN NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

ALTER TABLE permissions
    ADD CONSTRAINT FK_PERMISSIONS_ON_HOME FOREIGN KEY (home_id) REFERENCES homes (id);

ALTER TABLE permissions
    ADD CONSTRAINT FK_PERMISSIONS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE tokens
    ADD CONSTRAINT FK_TOKENS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);