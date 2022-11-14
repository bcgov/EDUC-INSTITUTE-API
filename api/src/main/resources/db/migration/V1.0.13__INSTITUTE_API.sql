INSERT INTO FACILITY_TYPE_CODE (FACILITY_TYPE_CODE, LABEL, DESCRIPTION, LEGACY_CODE,
                                DISPLAY_ORDER, EFFECTIVE_DATE, EXPIRY_DATE, CREATE_USER,
                                CREATE_DATE, UPDATE_USER, UPDATE_DATE)
VALUES ('POST_SEC', 'Post Secondary', 'Post Secondary School', null, 10,
        to_date('2020-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
        to_date('2099-12-31 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'IDIR/SRATH',
        to_date('2019-12-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'IDIR/SRATH',
        to_date('2019-12-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'));
