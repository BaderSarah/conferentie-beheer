package validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import repository.GebruikerRepository;

@Component
public class EmailConstraintValidator implements ConstraintValidator<ValidEmail, String> {

    @Autowired
    private GebruikerRepository gebruikerRepository;

    private String message;

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        this.message = constraintAnnotation.message(); 
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false; 
        }

        if (!value.contains("@")) {
            return false; 
        }

        if (gebruikerRepository != null && gebruikerRepository.existsByEmail(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                   .addPropertyNode("email")
                   .addConstraintViolation();
            return false;
        }

        return true;
    }
}


