ALTER TABLE exercises ADD COLUMN description TEXT NULL;

CREATE TABLE exercise_auxiliary_muscles (
    exercise_id BIGINT NOT NULL,
    body_part_id BIGINT NOT NULL,
    PRIMARY KEY (exercise_id, body_part_id),
    FOREIGN KEY (exercise_id) REFERENCES exercises(id),
    FOREIGN KEY (body_part_id) REFERENCES body_parts(id)
);
