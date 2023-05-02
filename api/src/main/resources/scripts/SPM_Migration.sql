CREATE TABLE DISTRICT
as
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as DISTRICT_ID,
    TRIM(dist_mast.DISTNO) as DISTRICT_NUMBER,
    CASE WHEN regexp_like (CONCAT(TRIM(dist_mast.FAX_AREA),TRIM(dist_mast.FAX_NUMBER)),'^(\d{3}\d{3}?\d{4}|\d{10})$') AND TRIM(dist_mast.FAX_NUMBER) != '0000000000' then CONCAT(TRIM(dist_mast.FAX_AREA),TRIM(dist_mast.FAX_NUMBER)) ELSE (CAST(NULL as VARCHAR2(100))) END as FAX_NUMBER,
    CASE WHEN regexp_like (CONCAT(TRIM(dist_mast.PHONE_AREA),TRIM(dist_mast.PHONE_NUMBER)),'^(\d{3}\d{3}?\d{4}|\d{10})$') AND TRIM(dist_mast.PHONE_NUMBER) != '0000000000' then CONCAT(TRIM(dist_mast.PHONE_AREA),TRIM(dist_mast.PHONE_NUMBER)) ELSE (CAST(NULL as VARCHAR2(100))) END as PHONE_NUMBER,
    TRIM(LOWER(dist_mast.E_MAIL_ID)) as EMAIL,
    TRIM(LOWER(dist_mast.WEB_ADDRESS)) as WEBSITE,
    TRIM(INITCAP(dist_mast.DISTRICT_NAME)) as DISPLAY_NAME,
    (SELECT dist_reg.DISTRICT_REGION_CODE from DISTRICT_REGION_CODE dist_reg where TRIM(dist_mast.DIST_AREA_CODE) = dist_reg.LEGACY_CODE) as DISTRICT_REGION_CODE,
    CASE WHEN dist_mast.DISTRICT_STATUS_CODE = 'O' then 'ACTIVE' ELSE 'INACTIVE' END as DISTRICT_STATUS_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM DISTRICT_MASTER dist_mast;

CREATE TABLE DISTRICT_HISTORY
as
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as DISTRICT_HISTORY_ID,
    dist.*
FROM DISTRICT dist;

CREATE TABLE INDEPENDENT_AUTHORITY
as
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as INDEPENDENT_AUTHORITY_ID,
    TRIM(auth_mast.AUTH_NUMBER) as authority_number,
    CASE WHEN regexp_like (TRIM(auth_mast.FAX_NUMBER),'^(\d{3}\d{3}?\d{4}|\d{10})$') AND TRIM(auth_mast.FAX_NUMBER) != '0000000000' then TRIM(auth_mast.FAX_NUMBER) ELSE (CAST(NULL as VARCHAR2(100))) END as FAX_NUMBER,
    CASE WHEN regexp_like (TRIM(auth_mast.PHONE_NUMBER),'^(\d{3}\d{3}?\d{4}|\d{10})$') AND TRIM(auth_mast.PHONE_NUMBER) != '0000000000' then TRIM(auth_mast.PHONE_NUMBER) ELSE (CAST(NULL as VARCHAR2(100))) END as PHONE_NUMBER,
    TRIM(LOWER(auth_mast.E_MAIL_ID)) as EMAIL,
    TRIM(INITCAP(auth_mast.SCHOOL_AUTHORITY_NAME)) as DISPLAY_NAME,
    (SELECT auth_type.AUTHORITY_TYPE_CODE from AUTHORITY_TYPE_CODE auth_type where TRIM(auth_mast.AUTHORITY_TYPE) = SUBSTR(auth_type.AUTHORITY_TYPE_CODE, 0, 1)) as AUTHORITY_TYPE_CODE,
    auth_mast.DATE_OPENED as OPENED_DATE,
    CASE WHEN TRUNC(auth_mast.DATE_CLOSED) = '9999-12-31' then (CAST(NULL as DATE)) ELSE auth_mast.DATE_CLOSED END as CLOSED_DATE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM AUTHORITY_MASTER auth_mast;

CREATE TABLE INDEPENDENT_AUTHORITY_HISTORY
as
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as INDEPENDENT_AUTHORITY_HISTORY_ID,
    auth.*
FROM INDEPENDENT_AUTHORITY auth;

CREATE TABLE SCHOOL
as
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SCHOOL_ID,
    (SELECT dist.DISTRICT_ID from DISTRICT dist WHERE schl_mast.DISTNO = dist.DISTRICT_NUMBER) as DISTRICT_ID,
    (SELECT auth.INDEPENDENT_AUTHORITY_ID from INDEPENDENT_AUTHORITY auth WHERE schl_mast.AUTH_NUMBER = auth.AUTHORITY_NUMBER) as INDEPENDENT_AUTHORITY_ID,
    TRIM(schl_mast.SCHLNO) as SCHOOL_NUMBER,
    CASE WHEN regexp_like (TRIM(schl_mast.SC_FAX_NUMBER),'^(\d{3}\d{3}?\d{4}|\d{10})$') AND TRIM(schl_mast.SC_FAX_NUMBER) != '0000000000' then TRIM(schl_mast.SC_FAX_NUMBER) ELSE (CAST(NULL as VARCHAR2(100))) END as FAX_NUMBER,
    CASE WHEN regexp_like (TRIM(schl_mast.SC_PHONE_NUMBER),'^(\d{3}\d{3}?\d{4}|\d{10})$') AND TRIM(schl_mast.SC_PHONE_NUMBER) != '0000000000' then TRIM(schl_mast.SC_PHONE_NUMBER) ELSE (CAST(NULL as VARCHAR2(100))) END as PHONE_NUMBER,
    TRIM(LOWER(schl_mast.SC_E_MAIL_ID)) as EMAIL,
    (CAST(NULL as VARCHAR2(100))) as WEBSITE,
    TRIM(INITCAP(schl_mast.SCHOOL_NAME)) as DISPLAY_NAME,
    (SELECT schl_org.SCHOOL_ORGANIZATION_CODE from SCHOOL_ORGANIZATION_CODE schl_org where TRIM(schl_mast.SCHOOL_ORGANIZATION_CODE) = schl_org.LEGACY_CODE) as SCHOOL_ORGANIZATION_CODE,
    (SELECT schl_cat.SCHOOL_CATEGORY_CODE from SCHOOL_CATEGORY_CODE schl_cat where TRIM(schl_mast.SCHOOL_CATEGORY_CODE) = schl_cat.LEGACY_CODE) as SCHOOL_CATEGORY_CODE,
    (SELECT fac_type.FACILITY_TYPE_CODE from FACILITY_TYPE_CODE fac_type where TRIM(schl_mast.FACILITY_TYPE_CODE) = fac_type.LEGACY_CODE) as FACILITY_TYPE_CODE,
    schl_mast.DATE_OPENED as OPENED_DATE,
    CASE WHEN TRUNC(schl_mast.DATE_CLOSED) = '9999-12-31' then (CAST(NULL as DATE)) ELSE schl_mast.DATE_CLOSED END as CLOSED_DATE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast;

CREATE TABLE SCHOOL_HISTORY
as
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SCHOOL_HISTORY_ID,
    schl.*
FROM SCHOOL schl;

CREATE TABLE NOTE
as
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as NOTE_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    (CAST(NULL as VARCHAR2(100))) as DISTRICT_ID,
    (CAST(NULL as VARCHAR2(100))) as INDEPENDENT_AUTHORITY_ID,
    TRIM(schl_mast."COMMENT") as CONTENT,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast."COMMENT") != ' '
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as NOTE_ID,
    (CAST(NULL as VARCHAR2(100))) as SCHOOL_ID,
    (SELECT dist.DISTRICT_ID from DISTRICT dist WHERE dist_mast.DISTNO = dist.DISTRICT_NUMBER) as DISTRICT_ID,
    (CAST(NULL as VARCHAR2(100))) as INDEPENDENT_AUTHORITY_ID,
    TRIM(dist_mast.NOTES) as CONTENT,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM DISTRICT_MASTER dist_mast
WHERE TRIM(dist_mast.NOTES) != ' ';

CREATE TABLE SCHOOL_GRADE
as
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SCHOOL_GRADE_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    'KINDHALF' as SCHOOL_GRADE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.GRADE_KH_IND) = 'Y'
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SCHOOL_GRADE_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    'KINDFULL' as SCHOOL_GRADE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.GRADE_KF_IND) = 'Y'
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SCHOOL_GRADE_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    'GRADE01' as SCHOOL_GRADE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.GRADE_01_IND ) = 'Y'
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SCHOOL_GRADE_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    'GRADE02' as SCHOOL_GRADE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.GRADE_02_IND ) = 'Y'
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SCHOOL_GRADE_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    'GRADE03' as SCHOOL_GRADE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.GRADE_03_IND ) = 'Y'
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SCHOOL_GRADE_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    'GRADE04' as SCHOOL_GRADE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.GRADE_04_IND ) = 'Y'
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SCHOOL_GRADE_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    'GRADE05' as SCHOOL_GRADE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.GRADE_05_IND ) = 'Y'
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SCHOOL_GRADE_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    'GRADE06' as SCHOOL_GRADE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.GRADE_06_IND ) = 'Y'
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SCHOOL_GRADE_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    'GRADE07' as SCHOOL_GRADE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.GRADE_07_IND ) = 'Y'
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SCHOOL_GRADE_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    'GRADE08' as SCHOOL_GRADE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.GRADE_08_IND ) = 'Y'
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SCHOOL_GRADE_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    'GRADE09' as SCHOOL_GRADE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.GRADE_09_IND ) = 'Y'
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SCHOOL_GRADE_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    'GRADE10' as SCHOOL_GRADE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.GRADE_10_IND ) = 'Y'
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SCHOOL_GRADE_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    'GRADE11' as SCHOOL_GRADE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.GRADE_11_IND ) = 'Y'
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SCHOOL_GRADE_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    'GRADE12' as SCHOOL_GRADE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.GRADE_12_IND ) = 'Y'
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SCHOOL_GRADE_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    'ELEMUNGR' as SCHOOL_GRADE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.GRADE_EU_IND ) = 'Y'
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SCHOOL_GRADE_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    'SECUNGR' as SCHOOL_GRADE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.GRADE_SU_IND ) = 'Y'
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SCHOOL_GRADE_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    'HOMESCHL' as SCHOOL_GRADE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.GRADE_HS_IND ) = 'Y'
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SCHOOL_GRADE_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    'GRADADULT' as SCHOOL_GRADE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.GRADE_GA_IND ) = 'Y';

CREATE TABLE NEIGHBORHOOD_LEARNING
as
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as NEIGHBORHOOD_LEARNING_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    'EARLYLEARN' as NEIGHBORHOOD_LEARNING_TYPE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.NLC_EARLY_LEARNING_FLAG) = 'Y'
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as NEIGHBORHOOD_LEARNING_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    'AFTERSCHL' as NEIGHBORHOOD_LEARNING_TYPE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.NLC_AFTER_SCHOOL_PROGRAM_FLAG ) = 'Y'
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as NEIGHBORHOOD_LEARNING_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    'CONTINEDUC' as NEIGHBORHOOD_LEARNING_TYPE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.NLC_CONTINUING_ED_FLAG ) = 'Y'
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as NEIGHBORHOOD_LEARNING_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    'SENIORS' as NEIGHBORHOOD_LEARNING_TYPE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.NLC_SENIORS_FLAG ) = 'Y'
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as NEIGHBORHOOD_LEARNING_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    'SPORTRECR' as NEIGHBORHOOD_LEARNING_TYPE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.NLC_SPORT_AND_REC_FLAG ) = 'Y'
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as NEIGHBORHOOD_LEARNING_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    'COMMUNITY' as NEIGHBORHOOD_LEARNING_TYPE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.NLC_COMMUNITY_USE_FLAG ) = 'Y'
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as NEIGHBORHOOD_LEARNING_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    'INTEGRSERV' as NEIGHBORHOOD_LEARNING_TYPE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.NLC_INTEGRATED_SERVICES_FLAG ) = 'Y';

----------------------------------------------------------------------------------------------

CREATE TABLE SCHOOL_ADDRESS
as
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as ADDRESS_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    TRIM(UPPER(schl_mast.SC_ADDRESS_LINE_1)) as ADDRESS_LINE_1,
    TRIM(UPPER(schl_mast.SC_ADDRESS_LINE_2)) as ADDRESS_LINE_2,
    TRIM(UPPER(schl_mast.SC_CITY)) as CITY,
    (SELECT prov.PROVINCE_CODE FROM PROVINCE_CODE prov where TRIM(schl_mast.SC_PROVINCE_CODE) = prov.LEGACY_CODE) as PROVINCE_CODE,
    (SELECT cnty.COUNTRY_CODE FROM COUNTRY_CODE cnty where TRIM(schl_mast.SC_COUNTRY_CODE) = cnty.LEGACY_CODE) as COUNTRY_CODE,
    TRIM(schl_mast.SC_POSTAL_CODE) as POSTAL,
    'MAILING' as ADDRESS_TYPE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.SC_ADDRESS_LINE_1) != ' '
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as ADDRESS_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    TRIM(UPPER(schl_mast.PHYS_ADDRESS_LINE_1)) as ADDRESS_LINE_1,
    TRIM(UPPER(schl_mast.PHYS_ADDRESS_LINE_2)) as ADDRESS_LINE_2,
    TRIM(UPPER(schl_mast.PHYS_CITY)) as CITY,
    (SELECT prov.PROVINCE_CODE FROM PROVINCE_CODE prov where TRIM(schl_mast.PHYS_PROVINCE_CODE) = prov.LEGACY_CODE) as PROVINCE_CODE,
    (SELECT cnty.COUNTRY_CODE FROM COUNTRY_CODE cnty where TRIM(schl_mast.PHYS_COUNTRY_CODE) = cnty.LEGACY_CODE) as COUNTRY_CODE,
    TRIM(UPPER(schl_mast.PHYS_POSTAL_CODE)) as POSTAL,
    'PHYSICAL' as ADDRESS_TYPE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.PHYS_ADDRESS_LINE_1) != ' ' AND schl_mast.SCHOOL_CATEGORY_CODE != '09';

CREATE TABLE DISTRICT_ADDRESS
as
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as ADDRESS_ID,
    (SELECT dist.DISTRICT_ID from DISTRICT dist WHERE dist_mast.DISTNO = dist.DISTRICT_NUMBER) as DISTRICT_ID,
    TRIM(UPPER(dist_mast.MAIL_ADDRESS_LINE_1)) as ADDRESS_LINE_1,
    TRIM(UPPER(dist_mast.MAIL_ADDRESS_LINE_2)) as ADDRESS_LINE_2,
    TRIM(UPPER(dist_mast.MAIL_CITY)) as CITY,
    TRIM(UPPER(dist_mast.MAIL_PROVINCE_CODE)) as PROVINCE_CODE,
    TRIM(UPPER(dist_mast.MAIL_COUNTRY_CODE)) as COUNTRY_CODE,
    TRIM(UPPER(dist_mast.MAIL_POSTAL_CODE)) as POSTAL,
    'MAILING' as ADDRESS_TYPE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM DISTRICT_MASTER dist_mast
WHERE TRIM(dist_mast.MAIL_ADDRESS_LINE_1) != ' '
UNION
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as ADDRESS_ID,
    (SELECT dist.DISTRICT_ID from DISTRICT dist WHERE dist_mast.DISTNO = dist.DISTRICT_NUMBER) as DISTRICT_ID,
    TRIM(UPPER(dist_mast.PHYS_ADDRESS_LINE_1)) as ADDRESS_LINE_1,
    TRIM(UPPER(dist_mast.PHYS_ADDRESS_LINE_2)) as ADDRESS_LINE_2,
    TRIM(UPPER(dist_mast.PHYS_CITY)) as CITY,
    TRIM(UPPER(dist_mast.PHYS_PROVINCE_CODE)) as PROVINCE_CODE,
    TRIM(UPPER(dist_mast.PHYS_COUNTRY_CODE)) as COUNTRY_CODE,
    TRIM(UPPER(dist_mast.PHYS_POSTAL_CODE)) as POSTAL,
    'PHYSICAL' as ADDRESS_TYPE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM DISTRICT_MASTER dist_mast
WHERE TRIM(dist_mast.PHYS_ADDRESS_LINE_1) != ' ';

CREATE TABLE AUTHORITY_ADDRESS
as
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as ADDRESS_ID,
    (SELECT auth.INDEPENDENT_AUTHORITY_ID from INDEPENDENT_AUTHORITY auth WHERE auth_mast.AUTH_NUMBER = auth.AUTHORITY_NUMBER) as INDEPENDENT_AUTHORITY_ID,
    TRIM(UPPER(auth_mast.ADDRESS_LINE_1)) as ADDRESS_LINE_1,
    TRIM(UPPER(auth_mast.ADDRESS_LINE_2)) as ADDRESS_LINE_2,
    TRIM(UPPER(auth_mast.CITY)) as CITY,
    (SELECT prov.PROVINCE_CODE FROM PROVINCE_CODE prov where TRIM(auth_mast.PROVINCE_CODE) = prov.LEGACY_CODE) as PROVINCE_CODE,
    (SELECT cnty.COUNTRY_CODE FROM COUNTRY_CODE cnty where TRIM(auth_mast.COUNTRY_CODE) = cnty.LEGACY_CODE) as COUNTRY_CODE,
    TRIM(UPPER(auth_mast.POSTAL_CODE)) as POSTAL,
    'MAILING' as ADDRESS_TYPE_CODE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM AUTHORITY_MASTER auth_mast
WHERE TRIM(auth_mast.ADDRESS_LINE_1) != ' ';

CREATE TABLE DISTRICT_ADDRESS_HISTORY
as
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as DISTRICT_ADDRESS_HISTORY_ID,
    (SELECT DISTRICT_HISTORY_ID FROM DISTRICT_HISTORY) as DISTRICT_HISTORY_ID,
    addr.DISTRICT_ID,
    addr.ADDRESS_LINE_1,
    addr.ADDRESS_LINE_2,
    addr.CITY,
    addr.PROVINCE_CODE,
    addr.COUNTRY_CODE,
    addr.POSTAL,
    addr.ADDRESS_TYPE_CODE,
    addr.CREATE_USER,
    addr.CREATE_DATE,
    addr.UPDATE_USER,
    addr.UPDATE_DATE
FROM DISTRICT_ADDRESS addr;

CREATE TABLE SCHOOL_ADDRESS_HISTORY
as
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SCHOOL_ADDRESS_HISTORY_ID,
    (SELECT SCHOOL_HISTORY_ID FROM SCHOOL_HISTORY) as SCHOOL_HISTORY_ID,
    addr.SCHOOL_ID,
    addr.ADDRESS_LINE_1,
    addr.ADDRESS_LINE_2,
    addr.CITY,
    addr.PROVINCE_CODE,
    addr.COUNTRY_CODE,
    addr.POSTAL,
    addr.ADDRESS_TYPE_CODE,
    addr.CREATE_USER,
    addr.CREATE_DATE,
    addr.UPDATE_USER,
    addr.UPDATE_DATE
FROM SCHOOL_ADDRESS addr;

CREATE TABLE AUTHORITY_ADDRESS_HISTORY
as
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as AUTHORITY_ADDRESS_HISTORY_ID,
    (SELECT AUTHORITY_HISTORY_ID FROM AUTHORITY_HISTORY) as AUTHORITY_HISTORY_ID,
    addr.AUTHORITY_ID,
    addr.ADDRESS_LINE_1,
    addr.ADDRESS_LINE_2,
    addr.CITY,
    addr.PROVINCE_CODE,
    addr.COUNTRY_CODE,
    addr.POSTAL,
    addr.ADDRESS_TYPE_CODE,
    addr.CREATE_USER,
    addr.CREATE_DATE,
    addr.UPDATE_USER,
    addr.UPDATE_DATE
FROM AUTHORITY_ADDRESS addr;

CREATE TABLE SCHOOL_CONTACT
as
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as SCHOOL_CONTACT_ID,
    (SELECT schl.SCHOOL_ID from SCHOOL schl WHERE schl_mast.SCHLNO = schl.SCHOOL_NUMBER AND schl_mast.DISTNO = (SELECT dist.DISTRICT_NUMBER from DISTRICT dist WHERE schl.DISTRICT_ID = dist.DISTRICT_ID)) as SCHOOL_ID,
    TRIM(INITCAP(schl_mast.PR_GIVEN_NAME)) as FIRST_NAME,
    TRIM(INITCAP(schl_mast.PR_SURNAME)) as LAST_NAME,
    'Principal' as JOB_TITLE,
    (CAST(NULL as VARCHAR2(100))) as PHONE_NUMBER,
    (CAST(NULL as VARCHAR2(100))) as PHONE_EXTENSION,
    (CAST(NULL as VARCHAR2(100))) as ALT_PHONE_NUMBER,
    (CAST(NULL as VARCHAR2(100))) as ALT_PHONE_EXTENSION,
    (CAST(NULL as VARCHAR2(100))) as EMAIL,
    'PRINCIPAL' as SCHOOL_CONTACT_TYPE_CODE,
    sysdate as EFFECTIVE_DATE,
    (CAST(NULL as DATE)) as EXPIRY_DATE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM SCHOOL_MASTER schl_mast
WHERE TRIM(schl_mast.PR_SURNAME) != ' ';

CREATE TABLE DISTRICT_CONTACT_2
as
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as DISTRICT_CONTACT_ID,
    (SELECT dist.DISTRICT_ID from DISTRICT dist WHERE dist_mast.DISTNO = dist.DISTRICT_NUMBER) as DISTRICT_ID,
    TRIM(INITCAP(dist_mast.GIVEN_NAME)) as FIRST_NAME,
    TRIM(INITCAP(dist_mast.SURNAME)) as LAST_NAME,
    TRIM(INITCAP(dist_mast.JOB_TITLE)) as JOB_TITLE,
    CASE WHEN regexp_like (CONCAT(TRIM(dist_mast.PHONE_AREA_1),TRIM(dist_mast.PHONE_NUMBER_1)),'^(\d{3}\d{3}?\d{4}|\d{10})$') AND TRIM(CONCAT(TRIM(dist_mast.PHONE_AREA_1),TRIM(dist_mast.PHONE_NUMBER_1))) != '0000000000' then CONCAT(TRIM(dist_mast.PHONE_AREA_1),TRIM(dist_mast.PHONE_NUMBER_1)) ELSE (CAST(NULL as VARCHAR2(100))) END as PHONE_NUMBER,
    TRIM(dist_mast.PHONE_EXT_1) as PHONE_EXTENSION,
    CASE WHEN regexp_like (CONCAT(TRIM(dist_mast.PHONE_AREA_2),TRIM(dist_mast.PHONE_NUMBER_2)),'^(\d{3}\d{3}?\d{4}|\d{10})$') AND TRIM(CONCAT(TRIM(dist_mast.PHONE_AREA_2),TRIM(dist_mast.PHONE_NUMBER_2))) != '0000000000' then CONCAT(TRIM(dist_mast.PHONE_AREA_2),TRIM(dist_mast.PHONE_NUMBER_2)) ELSE (CAST(NULL as VARCHAR2(100))) END as ALT_PHONE_NUMBER,
    TRIM(dist_mast.PHONE_EXT_2) as ALT_PHONE_EXTENSION,
    TRIM(LOWER(dist_mast.EMAIL_ID)) as EMAIL,
    (SELECT cont.DISTRICT_CONTACT_TYPE_CODE from DISTRICT_CONTACT_TYPE_CODE cont where TRIM(dist_mast.CONTACT_TYPE_CODE) = cont.LEGACY_CODE) as DISTRICT_CONTACT_TYPE_CODE,
    dist_mast.EFFECTIVE_FROM_DATE as EFFECTIVE_DATE,
    CASE WHEN TRUNC(dist_mast.EFFECTIVE_TO_DATE) = '9999-12-31' then (CAST(NULL as DATE)) ELSE dist_mast.EFFECTIVE_TO_DATE END as EXPIRY_DATE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM DISTRICT_CONTACT dist_mast
WHERE TRIM(dist_mast.SURNAME) != ' ';

CREATE TABLE AUTHORITY_CONTACT
AS
SELECT
    LOWER(REGEXP_REPLACE(dbms_crypto.randombytes(16), '(.{8})(.{4})(.{4})(.{4})(.{12})', '\1-\2-\3-\4-\5')) as AUTHORITY_CONTACT_ID,
    (SELECT auth.INDEPENDENT_AUTHORITY_ID from INDEPENDENT_AUTHORITY auth WHERE auth_mast.AUTH_NUMBER = auth.AUTHORITY_NUMBER) as INDEPENDENT_AUTHORITY_ID,
    TRIM(INITCAP(auth_mast.GIVEN_NAME)) as FIRST_NAME,
    TRIM(INITCAP(auth_mast.SURNAME)) as LAST_NAME,
    'Authority Representative' as JOB_TITLE,
    (CAST(NULL as VARCHAR2(100))) as PHONE_NUMBER,
    (CAST(NULL as VARCHAR2(100))) as PHONE_EXTENSION,
    (CAST(NULL as VARCHAR2(100))) as ALT_PHONE_NUMBER,
    (CAST(NULL as VARCHAR2(100))) as ALT_PHONE_EXTENSION,
    (CAST(NULL as VARCHAR2(100))) as EMAIL,
    'INDAUTHREP' as AUTHORITY_CONTACT_TYPE_CODE,
    sysdate as EFFECTIVE_DATE,
    (CAST(NULL as DATE)) as EXPIRY_DATE,
    'SPM_MIGRATION' as CREATE_USER,
    sysdate as CREATE_DATE,
    'SPM_MIGRATION' as UPDATE_USER,
    sysdate as UPDATE_DATE
FROM AUTHORITY_MASTER auth_mast
WHERE TRIM(auth_mast.SURNAME) != ' ';

