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

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty())
            return false;

        if (!value.contains("@"))
            return false;

        return !gebruikerRepository.existsByEmail(value);
    }
}
