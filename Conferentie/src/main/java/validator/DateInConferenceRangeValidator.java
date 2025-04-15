package validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateInConferenceRangeValidator implements ConstraintValidator<DateInConferenceRange, LocalDate> {

    private LocalDate startDate;
    private LocalDate endDate;

    @Override
    public void initialize(DateInConferenceRange constraintAnnotation) {
        try {
            this.startDate = LocalDate.parse(constraintAnnotation.start());
            this.endDate = LocalDate.parse(constraintAnnotation.end());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format in @DateInConferenceRange annotation.");
        }
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) return true; // Let @NotNull handle null check
        return !(value.isBefore(startDate) || value.isAfter(endDate));
    }
}

