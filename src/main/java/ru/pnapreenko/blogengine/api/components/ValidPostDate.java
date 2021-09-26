package ru.pnapreenko.blogengine.api.components;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.time.Instant;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPostDate.PostDateValidator.class)
@Documented
public @interface ValidPostDate {
    String message() default "Неправильный формат даты! Используйте: 'yyyy-MM-ddTHH:mm'.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class PostDateValidator implements ConstraintValidator<ValidPostDate, Instant> {
        @Override
        public boolean isValid(Instant time, ConstraintValidatorContext context) {
            return time != null;
        }
    }
}
