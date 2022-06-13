CREATE TABLE SCHOOL
(
    SCHOOL_ID                           UUID NOT NULL,
    DISTRICT_ID                         UUID NOT NULL,
    INDEPENDENT_AUTHORITY_ID            UUID,
    SCHOOL_NUMBER                       VARCHAR(5) NOT NULL,
    FAX_NUMBER                          VARCHAR(10),
    PHONE_NUMBER                        VARCHAR(10),
    EMAIL                               VARCHAR(255),
    WEBSITE                             VARCHAR(255),
    DISPLAY_NAME                        VARCHAR(255) NOT NULL,
    SCHOOL_ORGANIZATION_CODE            VARCHAR(10),
    SCHOOL_CATEGORY_CODE                VARCHAR(10) NOT NULL,
    FACILITY_TYPE_CODE                  VARCHAR(10) NOT NULL,
    OPENED_DATE                         TIMESTAMP NOT NULL,
    CLOSED_DATE                         TIMESTAMP,
    CREATE_USER                         VARCHAR(32)         NOT NULL,
    CREATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                         VARCHAR(32)         NOT NULL,
    UPDATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT SCHOOL_ID_PK PRIMARY KEY (SCHOOL_ID)
);

CREATE TABLE SCHOOL_HISTORY
(
    SCHOOL_HISTORY_ID                   UUID NOT NULL,
    SCHOOL_ID                           UUID NOT NULL,
    DISTRICT_ID                         UUID NOT NULL,
    INDEPENDENT_AUTHORITY_ID            UUID,
    SCHOOL_NUMBER                       VARCHAR(5) NOT NULL,
    FAX_NUMBER                          VARCHAR(10),
    PHONE_NUMBER                        VARCHAR(10),
    EMAIL                               VARCHAR(255),
    WEBSITE                             VARCHAR(255),
    DISPLAY_NAME                        VARCHAR(255) NOT NULL,
    ASSET_NUMBER                        VARCHAR(10) NOT NULL,
    SCHOOL_ORGANIZATION_CODE            VARCHAR(10) NOT NULL,
    SCHOOL_CATEGORY_CODE                VARCHAR(10) NOT NULL,
    FACILITY_TYPE_CODE                  VARCHAR(10) NOT NULL,
    OPENED_DATE                         TIMESTAMP NOT NULL,
    CLOSED_DATE                         TIMESTAMP,
    CREATE_USER                         VARCHAR(32)         NOT NULL,
    CREATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                         VARCHAR(32)         NOT NULL,
    UPDATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT SCHOOL_HISTORY_ID_PK PRIMARY KEY (SCHOOL_HISTORY_ID)
);

CREATE TABLE SCHOOL_CATEGORY_CODE
(
    SCHOOL_CATEGORY_CODE              VARCHAR(10)           NOT NULL,
    LABEL                             VARCHAR(30),
    DESCRIPTION                       VARCHAR(255),
    DISPLAY_ORDER                     NUMERIC DEFAULT 1       NOT NULL,
    EFFECTIVE_DATE                    TIMESTAMP                   NOT NULL,
    EXPIRY_DATE                       TIMESTAMP                   NOT NULL,
    CREATE_USER                       VARCHAR(32)           NOT NULL,
    CREATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                       VARCHAR(32)           NOT NULL,
    UPDATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT SCHOOL_CATEGORY_CODE_PK PRIMARY KEY (SCHOOL_CATEGORY_CODE)
);

CREATE TABLE SCHOOL_ORGANIZATION_CODE
(
    SCHOOL_ORGANIZATION_CODE          VARCHAR(10)           NOT NULL,
    LABEL                             VARCHAR(30),
    DESCRIPTION                       VARCHAR(255),
    DISPLAY_ORDER                     NUMERIC DEFAULT 1       NOT NULL,
    EFFECTIVE_DATE                    TIMESTAMP                   NOT NULL,
    EXPIRY_DATE                       TIMESTAMP                   NOT NULL,
    CREATE_USER                       VARCHAR(32)           NOT NULL,
    CREATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                       VARCHAR(32)           NOT NULL,
    UPDATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT SCHOOL_ORGANIZATION_CODE_PK PRIMARY KEY (SCHOOL_ORGANIZATION_CODE)
);

CREATE TABLE FACILITY_TYPE_CODE
(
    FACILITY_TYPE_CODE                VARCHAR(10)           NOT NULL,
    LABEL                             VARCHAR(30),
    DESCRIPTION                       VARCHAR(255),
    DISPLAY_ORDER                     NUMERIC DEFAULT 1       NOT NULL,
    EFFECTIVE_DATE                    TIMESTAMP                   NOT NULL,
    EXPIRY_DATE                       TIMESTAMP                   NOT NULL,
    CREATE_USER                       VARCHAR(32)           NOT NULL,
    CREATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                       VARCHAR(32)           NOT NULL,
    UPDATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT FACILITY_TYPE_CODE_PK PRIMARY KEY (FACILITY_TYPE_CODE)
);

CREATE TABLE SCHOOL_GRADE
(
    SCHOOL_GRADE_ID                     UUID NOT NULL,
    SCHOOL_ID                           UUID NOT NULL,
    SCHOOL_GRADE_CODE                   VARCHAR(10) NOT NULL,
    CREATE_USER                         VARCHAR(32)         NOT NULL,
    CREATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                         VARCHAR(32)         NOT NULL,
    UPDATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT SCHOOL_GRADE_ID_PK PRIMARY KEY (SCHOOL_GRADE_ID)
);

CREATE TABLE SCHOOL_GRADE_CODE
(
    SCHOOL_GRADE_CODE                 VARCHAR(10)           NOT NULL,
    LABEL                             VARCHAR(30),
    DESCRIPTION                       VARCHAR(255),
    DISPLAY_ORDER                     NUMERIC DEFAULT 1       NOT NULL,
    EFFECTIVE_DATE                    TIMESTAMP                   NOT NULL,
    EXPIRY_DATE                       TIMESTAMP                   NOT NULL,
    CREATE_USER                       VARCHAR(32)           NOT NULL,
    CREATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                       VARCHAR(32)           NOT NULL,
    UPDATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT SCHOOL_GRADE_CODE_PK PRIMARY KEY (SCHOOL_GRADE_CODE)
);

CREATE TABLE NEIGHBORHOOD_LEARNING
(
    NEIGHBORHOOD_LEARNING_ID            UUID NOT NULL,
    SCHOOL_ID                           UUID NOT NULL,
    NEIGHBORHOOD_LEARNING_TYPE_CODE     VARCHAR(10) NOT NULL,
    CREATE_USER                         VARCHAR(32)         NOT NULL,
    CREATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                         VARCHAR(32)         NOT NULL,
    UPDATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT NEIGHBORHOOD_LEARNING_ID_PK PRIMARY KEY (NEIGHBORHOOD_LEARNING_ID)
);

CREATE TABLE NEIGHBORHOOD_LEARNING_TYPE_CODE
(
    NEIGHBORHOOD_LEARNING_TYPE_CODE   VARCHAR(10)           NOT NULL,
    LABEL                             VARCHAR(30),
    DESCRIPTION                       VARCHAR(255),
    DISPLAY_ORDER                     NUMERIC DEFAULT 1       NOT NULL,
    EFFECTIVE_DATE                    TIMESTAMP                   NOT NULL,
    EXPIRY_DATE                       TIMESTAMP                   NOT NULL,
    CREATE_USER                       VARCHAR(32)           NOT NULL,
    CREATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                       VARCHAR(32)           NOT NULL,
    UPDATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT NEIGHBORHOOD_LEARNING_TYPE_CODE_PK PRIMARY KEY (NEIGHBORHOOD_LEARNING_TYPE_CODE)
);

CREATE TABLE NOTE
(
    NOTE_ID                             UUID NOT NULL,
    SCHOOL_ID                           UUID,
    DISTRICT_ID                         UUID,
    INDEPENDENT_AUTHORITY_ID            UUID,
    CONTENT                             VARCHAR(4000) NOT NULL,
    CREATE_USER                         VARCHAR(32)         NOT NULL,
    CREATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                         VARCHAR(32)         NOT NULL,
    UPDATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT NOTE_ID_PK PRIMARY KEY (NOTE_ID)
);

CREATE TABLE DISTRICT
(
    DISTRICT_ID                         UUID NOT NULL,
    DISTRICT_NUMBER                     VARCHAR(3) NOT NULL,
    FAX_NUMBER                          VARCHAR(10),
    PHONE_NUMBER                        VARCHAR(10),
    EMAIL                               VARCHAR(255),
    WEBSITE                             VARCHAR(255),
    DISPLAY_NAME                        VARCHAR(255) NOT NULL,
    DISTRICT_REGION_CODE                VARCHAR(10) NOT NULL,
    OPENED_DATE                         TIMESTAMP NOT NULL,
    CLOSED_DATE                         TIMESTAMP,
    CREATE_USER                         VARCHAR(32)         NOT NULL,
    CREATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                         VARCHAR(32)         NOT NULL,
    UPDATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT DISTRICT_ID_PK PRIMARY KEY (DISTRICT_ID)
);

CREATE TABLE DISTRICT_REGION_CODE
(
    DISTRICT_REGION_CODE              VARCHAR(10)           NOT NULL,
    LABEL                             VARCHAR(30),
    DESCRIPTION                       VARCHAR(255),
    DISPLAY_ORDER                     NUMERIC DEFAULT 1       NOT NULL,
    EFFECTIVE_DATE                    TIMESTAMP                   NOT NULL,
    EXPIRY_DATE                       TIMESTAMP                   NOT NULL,
    CREATE_USER                       VARCHAR(32)           NOT NULL,
    CREATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                       VARCHAR(32)           NOT NULL,
    UPDATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT DISTRICT_REGION_CODE_PK PRIMARY KEY (DISTRICT_REGION_CODE)
);

CREATE TABLE DISTRICT_HISTORY
(
    DISTRICT_HISTORY_ID                 UUID NOT NULL,
    DISTRICT_ID                         UUID NOT NULL,
    DISTRICT_NUMBER                     VARCHAR(3) NOT NULL,
    FAX_NUMBER                          VARCHAR(10),
    PHONE_NUMBER                        VARCHAR(10),
    EMAIL                               VARCHAR(255),
    WEBSITE                             VARCHAR(255),
    DISPLAY_NAME                        VARCHAR(255) NOT NULL,
    DISTRICT_REGION_CODE                VARCHAR(10) NOT NULL,
    OPENED_DATE                         TIMESTAMP NOT NULL,
    CLOSED_DATE                         TIMESTAMP,
    CREATE_USER                         VARCHAR(32)         NOT NULL,
    CREATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                         VARCHAR(32)         NOT NULL,
    UPDATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT DISTRICT_HISTORY_ID_PK PRIMARY KEY (DISTRICT_HISTORY_ID)
);

CREATE TABLE INDEPENDENT_AUTHORITY
(
    INDEPENDENT_AUTHORITY_ID            UUID NOT NULL,
    AUTHORITY_NUMBER                    VARCHAR(3) NOT NULL,
    FAX_NUMBER                          VARCHAR(10),
    PHONE_NUMBER                        VARCHAR(10),
    EMAIL                               VARCHAR(255),
    DISPLAY_NAME                        VARCHAR(255) NOT NULL,
    AUTHORITY_GROUP_CODE                VARCHAR(10) NOT NULL,
    AUTHORITY_TYPE_CODE                 VARCHAR(10) NOT NULL,
    OPENED_DATE                         TIMESTAMP NOT NULL,
    CLOSED_DATE                         TIMESTAMP,
    CREATE_USER                         VARCHAR(32)         NOT NULL,
    CREATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                         VARCHAR(32)         NOT NULL,
    UPDATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT INDEPENDENT_AUTHORITY_ID_PK PRIMARY KEY (INDEPENDENT_AUTHORITY_ID)
);

CREATE TABLE INDEPENDENT_AUTHORITY_HISTORY
(
    INDEPENDENT_AUTHORITY_HISTORY_ID    UUID NOT NULL,
    INDEPENDENT_AUTHORITY_ID            UUID NOT NULL,
    AUTHORITY_NUMBER                    VARCHAR(3) NOT NULL,
    FAX_NUMBER                          VARCHAR(10),
    PHONE_NUMBER                        VARCHAR(10),
    EMAIL                               VARCHAR(255),
    DISPLAY_NAME                        VARCHAR(255) NOT NULL,
    AUTHORITY_GROUP_CODE                VARCHAR(10) NOT NULL,
    AUTHORITY_TYPE_CODE                 VARCHAR(10) NOT NULL,
    OPENED_DATE                         TIMESTAMP NOT NULL,
    CLOSED_DATE                         TIMESTAMP,
    CREATE_USER                         VARCHAR(32)         NOT NULL,
    CREATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                         VARCHAR(32)         NOT NULL,
    UPDATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT INDEPENDENT_AUTHORITY_HISTORY_ID_PK PRIMARY KEY (INDEPENDENT_AUTHORITY_HISTORY_ID)
);

CREATE TABLE AUTHORITY_GROUP_CODE
(
    AUTHORITY_GROUP_CODE              VARCHAR(10)           NOT NULL,
    LABEL                             VARCHAR(30),
    DESCRIPTION                       VARCHAR(255),
    DISPLAY_ORDER                     NUMERIC DEFAULT 1       NOT NULL,
    EFFECTIVE_DATE                    TIMESTAMP                   NOT NULL,
    EXPIRY_DATE                       TIMESTAMP                   NOT NULL,
    CREATE_USER                       VARCHAR(32)           NOT NULL,
    CREATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                       VARCHAR(32)           NOT NULL,
    UPDATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT AUTHORITY_GROUP_CODE_PK PRIMARY KEY (AUTHORITY_GROUP_CODE)
);

CREATE TABLE AUTHORITY_TYPE_CODE
(
    AUTHORITY_TYPE_CODE               VARCHAR(10)           NOT NULL,
    LABEL                             VARCHAR(30),
    DESCRIPTION                       VARCHAR(255),
    DISPLAY_ORDER                     NUMERIC DEFAULT 1       NOT NULL,
    EFFECTIVE_DATE                    TIMESTAMP                   NOT NULL,
    EXPIRY_DATE                       TIMESTAMP                   NOT NULL,
    CREATE_USER                       VARCHAR(32)           NOT NULL,
    CREATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                       VARCHAR(32)           NOT NULL,
    UPDATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT AUTHORITY_TYPE_CODE_PK PRIMARY KEY (AUTHORITY_TYPE_CODE)
);

CREATE TABLE CONTACT
(
    CONTACT_ID                          UUID NOT NULL,
    SCHOOL_ID                           UUID,
    DISTRICT_ID                         UUID,
    INDEPENDENT_AUTHORITY_ID            UUID,
    FIRST_NAME                          VARCHAR(255) NOT NULL,
    LAST_NAME                           VARCHAR(255) NOT NULL,
    PHONE_NUMBER                        VARCHAR(10),
    EMAIL                               VARCHAR(255),
    CONTACT_TYPE_CODE                   VARCHAR(10) NOT NULL,
    CREATE_USER                         VARCHAR(32)         NOT NULL,
    CREATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                         VARCHAR(32)         NOT NULL,
    UPDATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT CONTACT_ID_PK PRIMARY KEY (CONTACT_ID)
);

CREATE TABLE CONTACT_TYPE_CODE
(
    CONTACT_TYPE_CODE                 VARCHAR(10)           NOT NULL,
    LABEL                             VARCHAR(30),
    DESCRIPTION                       VARCHAR(255),
    DISPLAY_ORDER                     NUMERIC DEFAULT 1       NOT NULL,
    EFFECTIVE_DATE                    TIMESTAMP                   NOT NULL,
    EXPIRY_DATE                       TIMESTAMP                   NOT NULL,
    CREATE_USER                       VARCHAR(32)           NOT NULL,
    CREATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                       VARCHAR(32)           NOT NULL,
    UPDATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT CONTACT_TYPE_CODE_PK PRIMARY KEY (CONTACT_TYPE_CODE)
);

CREATE TABLE ADDRESS
(
    ADDRESS_ID                          UUID NOT NULL,
    SCHOOL_ID                           UUID,
    DISTRICT_ID                         UUID,
    INDEPENDENT_AUTHORITY_ID            UUID,
    ADDRESS_LINE_1                      VARCHAR(255) NOT NULL,
    ADDRESS_LINE_2                      VARCHAR(255),
    CITY                                VARCHAR(255) NOT NULL,
    POSTAL                              VARCHAR(10) NOT NULL,
    PROVINCE_CODE                       VARCHAR(10) NOT NULL,
    COUNTRY_CODE                        VARCHAR(10) NOT NULL,
    ADDRESS_TYPE_CODE                   VARCHAR(10) NOT NULL,
    CREATE_USER                         VARCHAR(32)         NOT NULL,
    CREATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                         VARCHAR(32)         NOT NULL,
    UPDATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT ADDRESS_ID_PK PRIMARY KEY (ADDRESS_ID)
);

CREATE TABLE ADDRESS_HISTORY
(
    ADDRESS_HISTORY_ID                  UUID NOT NULL,
    ADDRESS_ID                          UUID NOT NULL,
    SCHOOL_ID                           UUID,
    DISTRICT_ID                         UUID,
    INDEPENDENT_AUTHORITY_ID            UUID,
    ADDRESS_LINE_1                      VARCHAR(255) NOT NULL,
    ADDRESS_LINE_2                      VARCHAR(255),
    CITY                                VARCHAR(255) NOT NULL,
    POSTAL                              VARCHAR(10) NOT NULL,
    PROVINCE_CODE                       VARCHAR(10) NOT NULL,
    COUNTRY_CODE                        VARCHAR(10) NOT NULL,
    ADDRESS_TYPE_CODE                   VARCHAR(10) NOT NULL,
    CREATE_USER                         VARCHAR(32)         NOT NULL,
    CREATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                         VARCHAR(32)         NOT NULL,
    UPDATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT ADDRESS_HISTORY_ID_PK PRIMARY KEY (ADDRESS_HISTORY_ID)
);

CREATE TABLE ADDRESS_TYPE_CODE
(
    ADDRESS_TYPE_CODE                 VARCHAR(10)           NOT NULL,
    LABEL                             VARCHAR(30),
    DESCRIPTION                       VARCHAR(255),
    DISPLAY_ORDER                     NUMERIC DEFAULT 1       NOT NULL,
    EFFECTIVE_DATE                    TIMESTAMP                   NOT NULL,
    EXPIRY_DATE                       TIMESTAMP                   NOT NULL,
    CREATE_USER                       VARCHAR(32)           NOT NULL,
    CREATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                       VARCHAR(32)           NOT NULL,
    UPDATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT ADDRESS_TYPE_CODE_PK PRIMARY KEY (ADDRESS_TYPE_CODE)
);

CREATE TABLE COUNTRY_CODE
(
    COUNTRY_CODE                     VARCHAR(10)           NOT NULL,
    LABEL                             VARCHAR(30),
    DESCRIPTION                       VARCHAR(255),
    DISPLAY_ORDER                     NUMERIC DEFAULT 1       NOT NULL,
    EFFECTIVE_DATE                    TIMESTAMP                   NOT NULL,
    EXPIRY_DATE                       TIMESTAMP                   NOT NULL,
    CREATE_USER                       VARCHAR(32)           NOT NULL,
    CREATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                       VARCHAR(32)           NOT NULL,
    UPDATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT COUNTRY_CODE_PK PRIMARY KEY (COUNTRY_CODE)
);

CREATE TABLE PROVINCE_CODE
(
    PROVINCE_CODE                     VARCHAR(10)             NOT NULL,
    LABEL                             VARCHAR(30),
    DESCRIPTION                       VARCHAR(255),
    DISPLAY_ORDER                     NUMERIC DEFAULT 1       NOT NULL,
    EFFECTIVE_DATE                    TIMESTAMP               NOT NULL,
    EXPIRY_DATE                       TIMESTAMP               NOT NULL,
    CREATE_USER                       VARCHAR(32)             NOT NULL,
    CREATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                       VARCHAR(32)             NOT NULL,
    UPDATE_DATE                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT PROVINCE_CODE_PK PRIMARY KEY (PROVINCE_CODE)
);

--School & School History Foreign Keys
ALTER TABLE SCHOOL
    ADD CONSTRAINT FK_SCHOOL_SCHOOL_ORGANIZATION_CODE FOREIGN KEY (SCHOOL_ORGANIZATION_CODE) REFERENCES SCHOOL_ORGANIZATION_CODE (SCHOOL_ORGANIZATION_CODE);
ALTER TABLE SCHOOL
    ADD CONSTRAINT FK_SCHOOL_SCHOOL_CATEGORY_CODE FOREIGN KEY (SCHOOL_CATEGORY_CODE) REFERENCES SCHOOL_CATEGORY_CODE (SCHOOL_CATEGORY_CODE);
ALTER TABLE SCHOOL
    ADD CONSTRAINT FK_SCHOOL_FACILITY_TYPE_CODE FOREIGN KEY (FACILITY_TYPE_CODE) REFERENCES FACILITY_TYPE_CODE (FACILITY_TYPE_CODE);
ALTER TABLE SCHOOL
    ADD CONSTRAINT FK_SCHOOL_DISTRICT_ID FOREIGN KEY (DISTRICT_ID) REFERENCES DISTRICT (DISTRICT_ID);
ALTER TABLE SCHOOL
    ADD CONSTRAINT FK_SCHOOL_INDEPENDENT_AUTHORITY_ID FOREIGN KEY (INDEPENDENT_AUTHORITY_ID) REFERENCES INDEPENDENT_AUTHORITY (INDEPENDENT_AUTHORITY_ID);
ALTER TABLE SCHOOL_HISTORY
    ADD CONSTRAINT FK_SCHOOL_HISTORY_SCHOOL_ID FOREIGN KEY (SCHOOL_ID) REFERENCES SCHOOL (SCHOOL_ID);
ALTER TABLE SCHOOL_HISTORY
    ADD CONSTRAINT FK_SCHOOL_HISTORY_SCHOOL_ORGANIZATION_CODE FOREIGN KEY (SCHOOL_ORGANIZATION_CODE) REFERENCES SCHOOL_ORGANIZATION_CODE (SCHOOL_ORGANIZATION_CODE);
ALTER TABLE SCHOOL_HISTORY
    ADD CONSTRAINT FK_SCHOOL_HISTORY_SCHOOL_CATEGORY_CODE FOREIGN KEY (SCHOOL_CATEGORY_CODE) REFERENCES SCHOOL_CATEGORY_CODE (SCHOOL_CATEGORY_CODE);
ALTER TABLE SCHOOL_HISTORY
    ADD CONSTRAINT FK_SCHOOL_HISTORY_FACILITY_TYPE_CODE FOREIGN KEY (FACILITY_TYPE_CODE) REFERENCES FACILITY_TYPE_CODE (FACILITY_TYPE_CODE);
ALTER TABLE SCHOOL_HISTORY
    ADD CONSTRAINT FK_SCHOOL_HISTORY_DISTRICT_ID FOREIGN KEY (DISTRICT_ID) REFERENCES DISTRICT (DISTRICT_ID);
ALTER TABLE SCHOOL_HISTORY
    ADD CONSTRAINT FK_SCHOOL_HISTORY_INDEPENDENT_AUTHORITY_ID FOREIGN KEY (INDEPENDENT_AUTHORITY_ID) REFERENCES INDEPENDENT_AUTHORITY (INDEPENDENT_AUTHORITY_ID);

--District Foreign Keys
ALTER TABLE DISTRICT
    ADD CONSTRAINT FK_DISTRICT_DISTRICT_REGION_CODE FOREIGN KEY (DISTRICT_REGION_CODE) REFERENCES DISTRICT_REGION_CODE (DISTRICT_REGION_CODE);
ALTER TABLE DISTRICT_HISTORY
    ADD CONSTRAINT FK_DISTRICT_HISTORY_DISTRICT_ID FOREIGN KEY (DISTRICT_ID) REFERENCES DISTRICT (DISTRICT_ID);
ALTER TABLE DISTRICT_HISTORY
    ADD CONSTRAINT FK_DISTRICT_HISTORY_DISTRICT_REGION_CODE FOREIGN KEY (DISTRICT_REGION_CODE) REFERENCES DISTRICT_REGION_CODE (DISTRICT_REGION_CODE);

--Independent Authority & History Foreign Keys
ALTER TABLE INDEPENDENT_AUTHORITY
    ADD CONSTRAINT FK_INDEPENDENT_AUTHORITY_AUTHORITY_GROUP_CODE FOREIGN KEY (AUTHORITY_GROUP_CODE) REFERENCES AUTHORITY_GROUP_CODE (AUTHORITY_GROUP_CODE);
ALTER TABLE INDEPENDENT_AUTHORITY
    ADD CONSTRAINT FK_INDEPENDENT_AUTHORITY_AUTHORITY_TYPE_CODE FOREIGN KEY (AUTHORITY_TYPE_CODE) REFERENCES AUTHORITY_TYPE_CODE (AUTHORITY_TYPE_CODE);
ALTER TABLE INDEPENDENT_AUTHORITY_HISTORY
    ADD CONSTRAINT FK_INDEPENDENT_AUTHORITY_HISTORY_INDEPENDENT_AUTHORITY_ID FOREIGN KEY (INDEPENDENT_AUTHORITY_ID) REFERENCES INDEPENDENT_AUTHORITY (INDEPENDENT_AUTHORITY_ID);
ALTER TABLE INDEPENDENT_AUTHORITY_HISTORY
    ADD CONSTRAINT FK_INDEPENDENT_AUTHORITY_HISTORY_AUTHORITY_GROUP_CODE FOREIGN KEY (AUTHORITY_GROUP_CODE) REFERENCES AUTHORITY_GROUP_CODE (AUTHORITY_GROUP_CODE);
ALTER TABLE INDEPENDENT_AUTHORITY_HISTORY
    ADD CONSTRAINT FK_INDEPENDENT_AUTHORITY_HISTORY_AUTHORITY_TYPE_CODE FOREIGN KEY (AUTHORITY_TYPE_CODE) REFERENCES AUTHORITY_TYPE_CODE (AUTHORITY_TYPE_CODE);

--Contact Foreign Keys
ALTER TABLE CONTACT
    ADD CONSTRAINT FK_CONTACT_CONTACT_TYPE_CODE FOREIGN KEY (CONTACT_TYPE_CODE) REFERENCES CONTACT_TYPE_CODE (CONTACT_TYPE_CODE);
ALTER TABLE CONTACT
    ADD CONSTRAINT FK_CONTACT_SCHOOL_ID FOREIGN KEY (SCHOOL_ID) REFERENCES SCHOOL (SCHOOL_ID);
ALTER TABLE CONTACT
    ADD CONSTRAINT FK_CONTACT_DISTRICT_ID FOREIGN KEY (DISTRICT_ID) REFERENCES DISTRICT (DISTRICT_ID);
ALTER TABLE CONTACT
    ADD CONSTRAINT FK_CONTACT_INDEPENDENT_AUTHORITY_ID FOREIGN KEY (INDEPENDENT_AUTHORITY_ID) REFERENCES INDEPENDENT_AUTHORITY (INDEPENDENT_AUTHORITY_ID);

--Address & Address History Foreign Keys
ALTER TABLE ADDRESS
    ADD CONSTRAINT FK_ADDRESS_ADDRESS_TYPE_CODE FOREIGN KEY (ADDRESS_TYPE_CODE) REFERENCES ADDRESS_TYPE_CODE (ADDRESS_TYPE_CODE);
ALTER TABLE ADDRESS
    ADD CONSTRAINT FK_ADDRESS_PROVINCE_CODE FOREIGN KEY (PROVINCE_CODE) REFERENCES PROVINCE_CODE (PROVINCE_CODE);
ALTER TABLE ADDRESS
    ADD CONSTRAINT FK_ADDRESS_COUNTRY_CODE FOREIGN KEY (COUNTRY_CODE) REFERENCES COUNTRY_CODE (COUNTRY_CODE);
ALTER TABLE ADDRESS
    ADD CONSTRAINT FK_ADDRESS_SCHOOL_ID FOREIGN KEY (SCHOOL_ID) REFERENCES SCHOOL (SCHOOL_ID);
ALTER TABLE ADDRESS
    ADD CONSTRAINT FK_ADDRESS_DISTRICT_ID FOREIGN KEY (DISTRICT_ID) REFERENCES DISTRICT (DISTRICT_ID);
ALTER TABLE ADDRESS
    ADD CONSTRAINT FK_ADDRESS_INDEPENDENT_AUTHORITY_ID FOREIGN KEY (INDEPENDENT_AUTHORITY_ID) REFERENCES INDEPENDENT_AUTHORITY (INDEPENDENT_AUTHORITY_ID);
ALTER TABLE ADDRESS_HISTORY
    ADD CONSTRAINT FK_ADDRESS_HISTORY_ADDRESS_ID FOREIGN KEY (ADDRESS_ID) REFERENCES ADDRESS (ADDRESS_ID);
ALTER TABLE ADDRESS_HISTORY
    ADD CONSTRAINT FK_ADDRESS_HISTORY_ADDRESS_TYPE_CODE FOREIGN KEY (ADDRESS_TYPE_CODE) REFERENCES ADDRESS_TYPE_CODE (ADDRESS_TYPE_CODE);
ALTER TABLE ADDRESS_HISTORY
    ADD CONSTRAINT FK_ADDRESS_HISTORY_PROVINCE_CODE FOREIGN KEY (PROVINCE_CODE) REFERENCES PROVINCE_CODE (PROVINCE_CODE);
ALTER TABLE ADDRESS_HISTORY
    ADD CONSTRAINT FK_ADDRESS_HISTORY_COUNTRY_CODE FOREIGN KEY (COUNTRY_CODE) REFERENCES COUNTRY_CODE (COUNTRY_CODE);
ALTER TABLE ADDRESS_HISTORY
    ADD CONSTRAINT FK_ADDRESS_HISTORY_SCHOOL_ID FOREIGN KEY (SCHOOL_ID) REFERENCES SCHOOL (SCHOOL_ID);
ALTER TABLE ADDRESS_HISTORY
    ADD CONSTRAINT FK_ADDRESS_HISTORY_DISTRICT_ID FOREIGN KEY (DISTRICT_ID) REFERENCES DISTRICT (DISTRICT_ID);
ALTER TABLE ADDRESS_HISTORY
    ADD CONSTRAINT FK_ADDRESS_HISTORY_INDEPENDENT_AUTHORITY_ID FOREIGN KEY (INDEPENDENT_AUTHORITY_ID) REFERENCES INDEPENDENT_AUTHORITY (INDEPENDENT_AUTHORITY_ID);

--Note Foreign Keys
ALTER TABLE NOTE
    ADD CONSTRAINT FK_NOTE_SCHOOL_ID FOREIGN KEY (SCHOOL_ID) REFERENCES SCHOOL (SCHOOL_ID);
ALTER TABLE NOTE
    ADD CONSTRAINT FK_NOTE_DISTRICT_ID FOREIGN KEY (DISTRICT_ID) REFERENCES DISTRICT (DISTRICT_ID);
ALTER TABLE NOTE
    ADD CONSTRAINT FK_NOTE_INDEPENDENT_AUTHORITY_ID FOREIGN KEY (INDEPENDENT_AUTHORITY_ID) REFERENCES INDEPENDENT_AUTHORITY (INDEPENDENT_AUTHORITY_ID);

--School Grade Foreign Keys
ALTER TABLE SCHOOL_GRADE
    ADD CONSTRAINT FK_SCHOOL_GRADE_SCHOOL_ID FOREIGN KEY (SCHOOL_ID) REFERENCES SCHOOL (SCHOOL_ID);
ALTER TABLE SCHOOL_GRADE
    ADD CONSTRAINT FK_SCHOOL_GRADE_SCHOOL_GRADE_CODE FOREIGN KEY (SCHOOL_GRADE_CODE) REFERENCES SCHOOL_GRADE_CODE (SCHOOL_GRADE_CODE);

--Neighborhood Learning Foreign Keys
ALTER TABLE NEIGHBORHOOD_LEARNING
    ADD CONSTRAINT FK_NEIGHBORHOOD_LEARNING_SCHOOL_ID FOREIGN KEY (SCHOOL_ID) REFERENCES SCHOOL (SCHOOL_ID);
ALTER TABLE NEIGHBORHOOD_LEARNING
    ADD CONSTRAINT FK_NEIGHBORHOOD_LEARNING_NEIGHBORHOOD_LEARNING_TYPE_CODE FOREIGN KEY (NEIGHBORHOOD_LEARNING_TYPE_CODE) REFERENCES NEIGHBORHOOD_LEARNING_TYPE_CODE (NEIGHBORHOOD_LEARNING_TYPE_CODE);