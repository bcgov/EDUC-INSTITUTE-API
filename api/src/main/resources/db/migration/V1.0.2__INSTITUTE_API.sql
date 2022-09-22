UPDATE SCHOOL_GRADE_CODE
SET
    LABEL = 'Kindergarten Half',
    DESCRIPTION = 'Kindergarten half'
WHERE
    SCHOOL_GRADE_CODE = 'KINDHALF';

UPDATE SCHOOL_GRADE_CODE
SET
    LABEL = 'Kindergarten Full',
    DESCRIPTION = 'Kindergarten full'
WHERE
        SCHOOL_GRADE_CODE = 'KINDFULL';
