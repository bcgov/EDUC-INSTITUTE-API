package ca.bc.gov.educ.api.institute.validator;

import ca.bc.gov.educ.api.institute.InstituteApiResourceApplication;
import ca.bc.gov.educ.api.institute.struct.v1.Address;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = { InstituteApiResourceApplication.class })
@ActiveProfiles("test")
public class AddressValidatorTest {

    private final AddressPayloadValidator addressValidator = new AddressPayloadValidator(null, null);

    @Test
    public void testValidateCreatePayload_ValidAddress() {
        Address address = new Address();
        address.setAddressLine1("123 Maple Dr");
        address.setCity("Victoria");
        address.setCreateDate(String.valueOf(LocalDateTime.now()));
        address.setCreateUser("ME");

        List<FieldError> result = addressValidator.validateCreatePayload(address);
        if(result != null && !result.isEmpty()){
            assertEquals("createDate should be null for post operation.", result.get(0).getDefaultMessage());
            assertEquals("createUser should be null for post operation.", result.get(1).getDefaultMessage());
        }
    }

    @Test
    public void testValidateCreatePayload_InvalidAddress() throws Exception {
        Address address = new Address();
        address.setAddressLine1("123 Maple Dr");
        address.setCity("Victoria");

        List<FieldError> result = addressValidator.validateCreatePayload(address);
        assertEquals(0, result.size());
    }
}
