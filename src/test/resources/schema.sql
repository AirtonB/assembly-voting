CREATE TABLE schedule
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY,
    description CHARACTER VARYING(255),
    PRIMARY KEY (id)
);

CREATE TABLE session
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    end_session timestamp(6) without time zone,
    start_session timestamp(6) without time zone,
    schedule_id bigint NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (schedule_id) REFERENCES schedule (id)
);