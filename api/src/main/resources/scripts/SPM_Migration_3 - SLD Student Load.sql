--Create the base table
CREATE TABLE COLLECTION
(
    COLLECTION_ID   VARCHAR2(40)                        NOT NULL,
    COLLECTION_TYPE_CODE VARCHAR2(10)                   NOT NULL,
    OPEN_DATE       TIMESTAMP                           NOT NULL,
    CLOSE_DATE      TIMESTAMP                           NOT NULL,
    CREATE_USER     VARCHAR2(32)                        NOT NULL,
    CREATE_DATE     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER     VARCHAR2(32)                        NOT NULL,
    UPDATE_DATE     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT COLLECTION_ID_PK PRIMARY KEY (COLLECTION_ID)
);

--2020 Sep - 2021 July
INSERT INTO COLLECTION
(collection_id, collection_type_code, open_date, close_date, create_user, create_date, update_user, update_date)
VALUES(LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')), 'SEPTEMBER', TO_DATE('20200930', 'YYYYMMDD'), TO_DATE('20201116', 'YYYYMMDD'), 'SLD_MIGRATION', CURRENT_TIMESTAMP, 'SLD_MIGRATION', CURRENT_TIMESTAMP);

INSERT INTO COLLECTION
(collection_id, collection_type_code, open_date, close_date, create_user, create_date, update_user, update_date)
VALUES(LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')), 'FEBRUARY', TO_DATE('20210219', 'YYYYMMDD'), TO_DATE('20210307', 'YYYYMMDD'), 'SLD_MIGRATION', CURRENT_TIMESTAMP, 'SLD_MIGRATION', CURRENT_TIMESTAMP);

INSERT INTO COLLECTION
(collection_id, collection_type_code, open_date, close_date, create_user, create_date, update_user, update_date)
VALUES(LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')), 'MAY', TO_DATE('20210501', 'YYYYMMDD'), TO_DATE('20210530', 'YYYYMMDD'), 'SLD_MIGRATION', CURRENT_TIMESTAMP, 'SLD_MIGRATION', CURRENT_TIMESTAMP);

INSERT INTO COLLECTION
(collection_id, collection_type_code, open_date, close_date, create_user, create_date, update_user, update_date)
VALUES(LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')), 'JULY', TO_DATE('20210707', 'YYYYMMDD'), TO_DATE('20210730', 'YYYYMMDD'), 'SLD_MIGRATION', CURRENT_TIMESTAMP, 'SLD_MIGRATION', CURRENT_TIMESTAMP);

--2021 Sep - 2022 July
INSERT INTO COLLECTION
(collection_id, collection_type_code, open_date, close_date, create_user, create_date, update_user, update_date)
VALUES(LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')), 'SEPTEMBER', TO_DATE('20210930', 'YYYYMMDD'), TO_DATE('20211116', 'YYYYMMDD'), 'SLD_MIGRATION', CURRENT_TIMESTAMP, 'SLD_MIGRATION', CURRENT_TIMESTAMP);

INSERT INTO COLLECTION
(collection_id, collection_type_code, open_date, close_date, create_user, create_date, update_user, update_date)
VALUES(LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')), 'FEBRUARY', TO_DATE('20220219', 'YYYYMMDD'), TO_DATE('20220307', 'YYYYMMDD'), 'SLD_MIGRATION', CURRENT_TIMESTAMP, 'SLD_MIGRATION', CURRENT_TIMESTAMP);

INSERT INTO COLLECTION
(collection_id, collection_type_code, open_date, close_date, create_user, create_date, update_user, update_date)
VALUES(LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')), 'MAY', TO_DATE('20220501', 'YYYYMMDD'), TO_DATE('20220530', 'YYYYMMDD'), 'SLD_MIGRATION', CURRENT_TIMESTAMP, 'SLD_MIGRATION', CURRENT_TIMESTAMP);

INSERT INTO COLLECTION
(collection_id, collection_type_code, open_date, close_date, create_user, create_date, update_user, update_date)
VALUES(LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')), 'JULY', TO_DATE('20220707', 'YYYYMMDD'), TO_DATE('20220730', 'YYYYMMDD'), 'SLD_MIGRATION', CURRENT_TIMESTAMP, 'SLD_MIGRATION', CURRENT_TIMESTAMP);

--2022 Sep - 2023 July
INSERT INTO COLLECTION
(collection_id, collection_type_code, open_date, close_date, create_user, create_date, update_user, update_date)
VALUES(LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')), 'SEPTEMBER', TO_DATE('20220930', 'YYYYMMDD'), TO_DATE('20221116', 'YYYYMMDD'), 'SLD_MIGRATION', CURRENT_TIMESTAMP, 'SLD_MIGRATION', CURRENT_TIMESTAMP);

INSERT INTO COLLECTION
(collection_id, collection_type_code, open_date, close_date, create_user, create_date, update_user, update_date)
VALUES(LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')), 'FEBRUARY', TO_DATE('20230219', 'YYYYMMDD'), TO_DATE('20230307', 'YYYYMMDD'), 'SLD_MIGRATION', CURRENT_TIMESTAMP, 'SLD_MIGRATION', CURRENT_TIMESTAMP);

INSERT INTO COLLECTION
(collection_id, collection_type_code, open_date, close_date, create_user, create_date, update_user, update_date)
VALUES(LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')), 'MAY', TO_DATE('20230501', 'YYYYMMDD'), TO_DATE('20230530', 'YYYYMMDD'), 'SLD_MIGRATION', CURRENT_TIMESTAMP, 'SLD_MIGRATION', CURRENT_TIMESTAMP);

INSERT INTO COLLECTION
(collection_id, collection_type_code, open_date, close_date, create_user, create_date, update_user, update_date)
VALUES(LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')), 'JULY', TO_DATE('20230707', 'YYYYMMDD'), TO_DATE('20230730', 'YYYYMMDD'), 'SLD_MIGRATION', CURRENT_TIMESTAMP, 'SLD_MIGRATION', CURRENT_TIMESTAMP);

CREATE TABLE SDC_SCHOOL_COLLECTION
as
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SDC_SCHOOL_COLLECTION_ID,
    (SELECT COLLECTION_ID FROM COLLECTION coll WHERE TO_DATE(sld_student.REPORT_DATE,'YYYYMMDD') BETWEEN coll.OPEN_DATE AND coll.CLOSE_DATE) AS COLLECTION_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE SUBSTR( DISTNO||'' ||SCHLNO, 4) = schl.SCHOOL_NUMBER AND SUBSTR( sld_student.DISTNO||'' ||SCHLNO, 0 , 3) = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
	'COMPLETED' AS SDC_SCHOOL_COLLECTION_STATUS_CODE,
	TO_DATE(sld_student.REPORT_DATE,'YYYYMMDD') AS UPLOAD_DATE,
	'NOT_AVAILLABLE' AS UPLOAD_FILE_NAME,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM STUDENT sld_student
where REPORT_DATE > 20110929 AND REPORT_DATE < 20140801 GROUP BY DISTNO||'' ||SCHLNO, REPORT_DATE;

CREATE TABLE SDC_SCHOOL_COLLECTION_HISTORY
as
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SDC_SCHOOL_COLLECTION_HISTORY_ID,
    sdc.*
FROM SDC_SCHOOL_COLLECTION sdc;

CREATE INDEX STUDENT_PEN_INDX
ON STUDENT(PEN);

CREATE INDEX STUDENT_REPORT_DATE_INDX
ON STUDENT(REPORT_DATE);

CREATE INDEX STUDENT_PROGRAMS_PEN_INDX
ON STUDENT_PROGRAMS(PEN);

CREATE INDEX STUDENT_PROGRAMS_REPORT_DATE_INDX
ON STUDENT_PROGRAMS(REPORT_DATE);

CREATE TABLE SDC_STUDENT_ELL
AS
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SDC_STUDENT_ELL_ID,
    (SELECT STUDENT_ID
	 FROM STUDENT_LINK stud_link
	 WHERE stud_link.PEN = sld_student.PEN) AS STUDENT_ID,
    (SELECT MAX(ESL_YEARS)
	 FROM STUDENT stud
	 WHERE stud.REPORT_DATE = sld_student.REPORT_DATE
	 AND stud.PEN = sld_student.PEN) AS YEARS_IN_ELL,
    CREATE_USER                         VARCHAR(32)         NOT NULL,
    CREATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UPDATE_USER                         VARCHAR(32)         NOT NULL,
    UPDATE_DATE                         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
FROM STUDENT sld_student;

CREATE TABLE SDC_SCHOOL_COLLECTION_STUDENT
as
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SDC_SCHOOL_COLLECTION_STUDENT_ID,
    'VERIFIED' AS SDC_SCHOOL_COLLECTION_STUDENT_STATUS_CODE,
	sld_student.LOCAL_STUDENT_ID AS LOCAL_ID,
    sld_student.PEN AS STUDENT_PEN,
    sld_student.LEGAL_GIVEN_NAME AS LEGAL_FIRST_NAME,
    sld_student.LEGAL_MIDDLE_NAME AS LEGAL_MIDDLE_NAMES,
    sld_student.LEGAL_SURNAME AS LEGAL_LAST_NAME,
    sld_student.USUAL_GIVEN_NAME AS USUAL_FIRST_NAME,
    sld_student.USUAL_MIDDLE_NAME AS USUAL_MIDDLE_NAMES,
    sld_student.USUAL_SURNAME AS USUAL_LAST_NAME,
    sld_student.BIRTH_DATE AS DOB,
    sld_student.SEX AS GENDER_CODE,
    sld_student.SPED_CAT AS SPECIAL_EDUCATION_CATEGORY_CODE,
    sld_student.SCHOOL_FUNDING_CODE AS SCHOOL_FUNDING_CODE,
    sld_student.NATIVE_ANCESTRY_IND AS NATIVE_ANCESTRY_IND,
    sld_student.HOME_LANGUAGE_SPOKEN AS HOME_LANGUAGE_SPOKEN_CODE,
    sld_student.OTHER_COURSES AS OTHER_COURSES,
    sld_student.NUMBER_OF_SUPPORT_BLOCKS AS SUPPORT_BLOCKS,
    sld_student.ENROLLED_GRADE_CODE AS ENROLLED_GRADE_CODE,
    (SELECT LISTAGG(stud_prog.ENROLLED_PROGRAM_CODE, '') WITHIN GROUP (ORDER BY 1)
	 FROM STUDENT_PROGRAMS stud_prog
	 WHERE stud_prog.REPORT_DATE = sld_student.REPORT_DATE
	 AND stud_prog.PEN = sld_student.PEN
	 GROUP BY PEN) AS ENROLLED_PROGRAM_CODES,
	'ABC' AS CAREER_PROGRAM_CODE,
    sld_student.NUMBER_OF_COURSES AS NUMBER_OF_COURSES,
    sld_student.BAND_CODE AS BAND_CODE,
    sld_student.POSTAL AS POSTAL_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM STUDENT sld_student;
