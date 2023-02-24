package ca.bc.gov.educ.api.institute.exception;

import java.io.Serial;

public class PreConditionFailedException extends RuntimeException{
    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -6634443908542004185L;

    /**
     * Instantiates a new PreCondition failed exception.
     *
     * @param errorMessage error message
     */
    public PreConditionFailedException(String errorMessage) {
        super(errorMessage);
    }
}
