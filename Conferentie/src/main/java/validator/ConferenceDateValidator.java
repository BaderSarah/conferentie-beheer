package validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.springboot.conferentieapp.ConferentieEigenschappen;
import com.springboot.conferentieapp.SpringContext;

import domein.Evenement;

import java.time.LocalDate;

public class ConferenceDateValidator implements ConstraintValidator<ValidConferenceDate, Evenement> {

    private ConferentieEigenschappen conferEigenschappen;

    @Override
    public void initialize(ValidConferenceDate constraintAnnotation) {
        conferEigenschappen = SpringContext.getBean(ConferentieEigenschappen.class);
    }

    @Override
    public boolean isValid(Evenement evenement, ConstraintValidatorContext context) {
        if (evenement.getDatum() == null) {
            return true;
        }

        LocalDate conferentieStart = conferEigenschappen.getStart();
        LocalDate conferentieEinde = conferEigenschappen.getEinde();

        return !(evenement.getDatum().isBefore(conferentieStart) || evenement.getDatum().isAfter(conferentieEinde));
    }
}
