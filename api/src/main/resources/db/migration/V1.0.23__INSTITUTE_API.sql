CREATE TABLE INDEPENDENT_SCHOOL_FUNDING_GROUP
(
    SCHOOL_FUNDING_GROUP_ID             UUID NOT NULL,
    SCHOOL_ID                           UUID NOT NULL,
    SCHOOL_GRADE_CODE                   VARCHAR(10) NOT NULL,
    SCHOOL_FUNDING_GROUP_CODE           VARCHAR(10) NOT NULL,
    CREATE_USER                         VARCHAR(100)         NOT NULL,
    CREATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                         VARCHAR(100)         NOT NULL,
    UPDATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT SCHOOL_FUNDING_GROUP_ID_PK PRIMARY KEY (SCHOOL_FUNDING_GROUP_ID)
);

CREATE TABLE INDEPENDENT_SCHOOL_FUNDING_GROUP_HISTORY
(
    SCHOOL_FUNDING_GROUP_HISTORY_ID     UUID NOT NULL,
    SCHOOL_FUNDING_GROUP_ID             UUID NOT NULL,
    SCHOOL_ID                           UUID NOT NULL,
    SCHOOL_GRADE_CODE                   VARCHAR(10) NOT NULL,
    SCHOOL_FUNDING_GROUP_CODE           VARCHAR(10) NOT NULL,
    CREATE_USER                         VARCHAR(100)         NOT NULL,
    CREATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                         VARCHAR(100)         NOT NULL,
    UPDATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT SCHOOL_FUNDING_GROUP_HISTORY_ID_PK PRIMARY KEY (SCHOOL_FUNDING_GROUP_HISTORY_ID)
);

CREATE TABLE SCHOOL_FUNDING_GROUP_CODE
(
    SCHOOL_FUNDING_GROUP_CODE   VARCHAR(10)                         NOT NULL,
    LABEL                       VARCHAR(30)                         NOT NULL,
    DESCRIPTION                 VARCHAR(255)                        NOT NULL,
    LEGACY_CODE                 VARCHAR(10),
    DISPLAY_ORDER               NUMERIC   DEFAULT 1                 NOT NULL,
    EFFECTIVE_DATE              TIMESTAMP                           NOT NULL,
    EXPIRY_DATE                 TIMESTAMP                           NOT NULL,
    CREATE_USER                 VARCHAR(32)                         NOT NULL,
    CREATE_DATE                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                 VARCHAR(32)                         NOT NULL,
    UPDATE_DATE                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT SCHOOL_FUNDING_GROUP_CODE_PK PRIMARY KEY (SCHOOL_FUNDING_GROUP_CODE)
);

ALTER TABLE INDEPENDENT_SCHOOL_FUNDING_GROUP
    ADD CONSTRAINT FK_SCHOOL_FUNDING_FUNDING_CODE FOREIGN KEY (SCHOOL_FUNDING_GROUP_CODE)
        REFERENCES SCHOOL_FUNDING_GROUP_CODE (SCHOOL_FUNDING_GROUP_CODE);

ALTER TABLE INDEPENDENT_SCHOOL_FUNDING_GROUP
    ADD CONSTRAINT FK_SCHOOL_FUNDING_GRADE_CODE FOREIGN KEY (SCHOOL_GRADE_CODE)
        REFERENCES SCHOOL_GRADE_CODE (SCHOOL_GRADE_CODE);

ALTER TABLE INDEPENDENT_SCHOOL_FUNDING_GROUP_HISTORY
    ADD CONSTRAINT FK_SCHOOL_FUNDING_HISTORY_FUNDING_ID FOREIGN KEY (SCHOOL_FUNDING_GROUP_ID)
        REFERENCES INDEPENDENT_SCHOOL_FUNDING_GROUP (SCHOOL_FUNDING_GROUP_ID);

ALTER TABLE INDEPENDENT_SCHOOL_FUNDING_GROUP_HISTORY
    ADD CONSTRAINT FK_SCHOOL_FUNDING_HISTORY_FUNDING_CODE FOREIGN KEY (SCHOOL_FUNDING_GROUP_CODE)
        REFERENCES SCHOOL_FUNDING_GROUP_CODE (SCHOOL_FUNDING_GROUP_CODE);

ALTER TABLE INDEPENDENT_SCHOOL_FUNDING_GROUP_HISTORY
    ADD CONSTRAINT FK_SCHOOL_FUNDING_HISTORY_GRADE_CODE FOREIGN KEY (SCHOOL_GRADE_CODE)
        REFERENCES SCHOOL_GRADE_CODE (SCHOOL_GRADE_CODE);

INSERT INTO SCHOOL_FUNDING_GROUP_CODE (LEGACY_CODE, SCHOOL_FUNDING_GROUP_CODE, LABEL, DESCRIPTION,
                                       DISPLAY_ORDER, EFFECTIVE_DATE, EXPIRY_DATE, CREATE_USER,
                                       CREATE_DATE, UPDATE_USER, UPDATE_DATE)
VALUES ('01', 'GROUP1', 'Group 1', 'Group 1 Funding Group', 1,
        to_date('2020-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
        to_date('2099-12-31 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'IDIR/MVILLENE',
        to_date('2019-12-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'IDIR/MVILLENE',
        to_date('2019-12-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO SCHOOL_FUNDING_GROUP_CODE (LEGACY_CODE, SCHOOL_FUNDING_GROUP_CODE, LABEL, DESCRIPTION,
                                       DISPLAY_ORDER, EFFECTIVE_DATE, EXPIRY_DATE, CREATE_USER,
                                       CREATE_DATE, UPDATE_USER, UPDATE_DATE)
VALUES ('02', 'GROUP2', 'Group 2', 'Group 2 Funding Group', 2,
        to_date('2020-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
        to_date('2099-12-31 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'IDIR/MVILLENE',
        to_date('2019-12-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'IDIR/MVILLENE',
        to_date('2019-12-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO SCHOOL_FUNDING_GROUP_CODE (LEGACY_CODE, SCHOOL_FUNDING_GROUP_CODE, LABEL, DESCRIPTION,
                                       DISPLAY_ORDER, EFFECTIVE_DATE, EXPIRY_DATE, CREATE_USER,
                                       CREATE_DATE, UPDATE_USER, UPDATE_DATE)
VALUES ('03', 'GROUP3', 'Group 3', 'Group 3 Funding Group', 3,
        to_date('2020-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
        to_date('2099-12-31 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'IDIR/MVILLENE',
        to_date('2019-12-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'IDIR/MVILLENE',
        to_date('2019-12-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO SCHOOL_FUNDING_GROUP_CODE (LEGACY_CODE, SCHOOL_FUNDING_GROUP_CODE, LABEL, DESCRIPTION,
                                       DISPLAY_ORDER, EFFECTIVE_DATE, EXPIRY_DATE, CREATE_USER,
                                       CREATE_DATE, UPDATE_USER, UPDATE_DATE)
VALUES ('04', 'GROUP4', 'Group 4', 'Group 4 Funding Group', 4,
        to_date('2020-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
        to_date('2099-12-31 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'IDIR/MVILLENE',
        to_date('2019-12-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'IDIR/MVILLENE',
        to_date('2019-12-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'));