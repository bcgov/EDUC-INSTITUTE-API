CREATE TABLE SCHOOL_MOVE
(
    SCHOOL_MOVE_ID UUID                                NOT NULL,
    TO_SCHOOL_ID           UUID                                NOT NULL,
    FROM_SCHOOL_ID         UUID                                NOT NULL,
    MOVE_DATE              TIMESTAMP                           NOT NULL,
    CREATE_USER            VARCHAR(32)                         NOT NULL,
    CREATE_DATE            TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER            VARCHAR(32)                         NOT NULL,
    UPDATE_DATE            TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT SCHOOL_MOVE_ID_PK PRIMARY KEY (SCHOOL_MOVE_ID)
);

CREATE INDEX SCHOOL_MOVE_TO_SCHOOL_ID_IDX
    ON SCHOOL_MOVE (TO_SCHOOL_ID);

CREATE INDEX SCHOOL_MOVE_FROM_SCHOOL_ID_IDX
    ON SCHOOL_MOVE (FROM_SCHOOL_ID);