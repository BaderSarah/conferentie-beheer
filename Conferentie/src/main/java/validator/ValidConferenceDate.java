package validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ConferenceDateValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
public @interface ValidConferenceDate {

    String message() default "Datum ligt buiten conferentieperiode";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
