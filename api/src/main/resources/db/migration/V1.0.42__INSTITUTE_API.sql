CREATE TABLE SCHOOL_MOVE_HISTORY
(
    SCHOOL_MOVE_HISTORY_ID UUID                                NOT NULL,
    TO_SCHOOL_ID           UUID                                NOT NULL,
    FROM_SCHOOL_ID         UUID                                NOT NULL,
    MOVE_DATE              TIMESTAMP                           NOT NULL,
    CREATE_USER            VARCHAR(32)                         NOT NULL,
    CREATE_DATE            TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER            VARCHAR(32)                         NOT NULL,
    UPDATE_DATE            TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT SCHOOL_MOVE_HISTORY_ID_PK PRIMARY KEY (SCHOOL_MOVE_HISTORY_ID)
);

CREATE INDEX SCHOOL_MOVE_HISTORY_TO_SCHOOL_ID_IDX
    ON SCHOOL_MOVE_HISTORY (TO_SCHOOL_ID);

CREATE INDEX SCHOOL_MOVE_HISTORY_FROM_SCHOOL_ID_IDX
    ON SCHOOL_MOVE_HISTORY (FROM_SCHOOL_ID);