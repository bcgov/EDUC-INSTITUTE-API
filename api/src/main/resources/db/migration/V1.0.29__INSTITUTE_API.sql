CREATE TABLE INSTITUTE_EVENT
(
    EVENT_ID      UUID                             NOT NULL,
    EVENT_PAYLOAD VARCHAR(4000)                       NOT NULL,
    EVENT_STATUS  VARCHAR(50)                        NOT NULL,
    EVENT_TYPE    VARCHAR(100)                       NOT NULL,
    SAGA_ID       UUID,
    EVENT_OUTCOME VARCHAR(100)                       NOT NULL,
    REPLY_CHANNEL VARCHAR(100),
    CREATE_USER   VARCHAR(32)                         NOT NULL,
    CREATE_DATE   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER   VARCHAR(32)                         NOT NULL,
    UPDATE_DATE   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT INSTITUTE_EVENT_PK PRIMARY KEY (EVENT_ID)
);
CREATE INDEX INSTITUTE_EVENT_EVENT_STATUS_IDX ON INSTITUTE_EVENT (EVENT_STATUS);
CREATE INDEX INSTITUTE_EVENT_SAGA_ID_IDX ON INSTITUTE_EVENT (SAGA_ID);
CREATE INDEX INSTITUTE_EVENT_EVENT_TYPE_IDX ON INSTITUTE_EVENT (EVENT_TYPE);


CREATE TABLE INSTITUTE_SHEDLOCK
(
    NAME       VARCHAR(64),
    LOCK_UNTIL TIMESTAMP(3) NULL,
    LOCKED_AT  TIMESTAMP(3) NULL,
    LOCKED_BY  VARCHAR(255),
    CONSTRAINT INSTITUTE_SHEDLOCK_PK PRIMARY KEY (NAME)
);
COMMENT ON TABLE INSTITUTE_SHEDLOCK IS 'This table is used to achieve distributed lock between pods, for schedulers.';
