ALTER TABLE INDEPENDENT_AUTHORITY_HISTORY
ALTER COLUMN AUTHORITY_NUMBER type INTEGER USING AUTHORITY_NUMBER::INTEGER;
