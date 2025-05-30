package validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = EmailConstraintValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface ValidEmail {

    String message() default "{registration.err.mail}";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default {};
}