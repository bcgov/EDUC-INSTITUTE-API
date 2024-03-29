ALTER TABLE INDEPENDENT_AUTHORITY
ALTER COLUMN AUTHORITY_NUMBER type VARCHAR(4),
ADD CONSTRAINT INDEPENDENT_AUTHORITY_AUTHORITY_NUMBER_UK UNIQUE (AUTHORITY_NUMBER);

ALTER TABLE INDEPENDENT_AUTHORITY_HISTORY
    ALTER COLUMN AUTHORITY_NUMBER type VARCHAR(4);

ALTER TABLE INDEPENDENT_AUTHORITY
    ALTER COLUMN AUTHORITY_NUMBER type INTEGER USING AUTHORITY_NUMBER::INTEGER;

ALTER TABLE INDEPENDENT_AUTHORITY_HISTORY
    ALTER COLUMN AUTHORITY_NUMBER type INTEGER USING AUTHORITY_NUMBER::INTEGER;
