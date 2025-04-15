package validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

//@Documented
//@Constraint(validatedBy = DateInConferenceRangeValidator.class)
//@Target({ ElementType.FIELD })
//@Retention(RetentionPolicy.RUNTIME)
public @interface DateInConferenceRange {

    String message() default "Datum moet binnen de conferentieperiode vallen.";

    String start(); 
    String end(); 

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
