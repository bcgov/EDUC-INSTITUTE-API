ALTER TABLE SCHOOL ADD COLUMN VENDOR_CODE VARCHAR(10);

CREATE TABLE VENDOR_CODE
(
    VENDOR_CODE                       VARCHAR(10)             NOT NULL,
    LABEL                             VARCHAR(30) NOT NULL,
    DESCRIPTION                       VARCHAR(255) NOT NULL,
    DISPLAY_ORDER                     NUMERIC DEFAULT 1       NOT NULL,
    EFFECTIVE_DATE                    TIMESTAMP               NOT NULL,
    EXPIRY_DATE                       TIMESTAMP               NOT NULL,
    CREATE_USER                       VARCHAR(32)             NOT NULL,
    CREATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                       VARCHAR(32)             NOT NULL,
    UPDATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- todo we need to add the two possible values for vendor code (is null a third options?)