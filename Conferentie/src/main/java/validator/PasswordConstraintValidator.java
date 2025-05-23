package validator;

import org.springframework.stereotype.Component;

import domein.Gebruiker;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class PasswordConstraintValidator implements ConstraintValidator<ValidPasswords, Gebruiker> {

    private String message;

    @Override
    public void initialize(ValidPasswords constraintAnnotation) {
        this.message = constraintAnnotation.message(); 
    }

    @Override
    public boolean isValid(Gebruiker registration, ConstraintValidatorContext context) {
        
        if (registration.getWachtwoord() == null || registration.getBevestigWachtwoord() == null) {
            return false;
        }
        
        boolean isValid = registration.getWachtwoord().equals(registration.getBevestigWachtwoord());
        
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message) 
                   .addPropertyNode("bevestigWachtwoord")
                   .addConstraintViolation();
        }

        return isValid;
    }
}
