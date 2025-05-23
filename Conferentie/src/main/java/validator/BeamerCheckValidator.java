package validator;

import domein.Evenement;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BeamerCheckValidator implements ConstraintValidator<ValidBeamerCheck, Evenement> {

    @Override
    public void initialize(ValidBeamerCheck constraintAnnotation) {
    }

    @Override
    public boolean isValid(Evenement evenement, ConstraintValidatorContext context) {
        if (evenement == null) {
            return true;
        }

        int beamercode = evenement.getBeamercode();
        int beamercheck = evenement.getBeamercheck();

        boolean valid = beamercheck == (beamercode % 97);

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                   .addPropertyNode("beamercheck")  
                   .addConstraintViolation();
        }

        return valid;
    }
}
