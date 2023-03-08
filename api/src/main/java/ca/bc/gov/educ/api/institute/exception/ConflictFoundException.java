package ca.bc.gov.educ.api.institute.exception;

import java.io.Serial;

public class ConflictFoundException extends RuntimeException {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -6634443908542004185L;

    /**
     * Instantiates a new ConflictFound exception.
     *
     * @param errorMessage error message
     */
    public ConflictFoundException(String errorMessage) {
        super(errorMessage);
    }
}
