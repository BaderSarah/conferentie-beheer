package validator;

import domein.Gebruiker;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPasswords, Gebruiker> {

    @Override
    public void initialize(ValidPasswords constraintAnnotation) {
    }

    @Override
    public boolean isValid(Gebruiker registration, ConstraintValidatorContext context) {
        
    	if (registration.getWachtwoord() == null || registration.getBevestigWachtwoord() == null) {
            return false;
        }
    	
        boolean isValid = registration.getWachtwoord().equals(registration.getBevestigWachtwoord());
        
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
            .addPropertyNode("bevestigWachtwoord")
            .addConstraintViolation();
        }

        return isValid;
    }
}