ALTER TABLE INDEPENDENT_AUTHORITY
ALTER COLUMN authority_number type VARCHAR(4),
ALTER COLUMN authority_number set not null,
ADD CONSTRAINT authority_number_unique UNIQUE (authority_number);