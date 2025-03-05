CREATE TABLE allergies
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE homes_allergies
(
    allergy_id BIGINT NOT NULL,
    home_id    UUID NOT NULL,
    CONSTRAINT pk_homes_allergies PRIMARY KEY (allergy_id, home_id),
    CONSTRAINT fk_homall_on_allergy_entity FOREIGN KEY (allergy_id) REFERENCES allergies (id),
    CONSTRAINT fk_homall_on_home_entity FOREIGN KEY (home_id) REFERENCES homes (id)
);

CREATE TABLE users_allergies
(
    allergy_id BIGINT NOT NULL,
    user_id    UUID NOT NULL,
    CONSTRAINT pk_users_allergies PRIMARY KEY (allergy_id, user_id),
    CONSTRAINT fk_useall_on_allergy_entity FOREIGN KEY (allergy_id) REFERENCES allergies (id),
    CONSTRAINT fk_useall_on_user_entity FOREIGN KEY (user_id) REFERENCES users (id)
);
