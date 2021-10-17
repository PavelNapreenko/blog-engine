package ru.pnapreenko.blogengine.api.components;

import ru.pnapreenko.blogengine.api.utils.ConfigStrings;
import ru.pnapreenko.blogengine.enums.ModerationDecision;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidModerationDecision.ModerationDecisionValidator.class)
@Documented
public @interface ValidModerationDecision {

    String message() default ConfigStrings.MODERATION_WRONG_DECISION;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class ModerationDecisionValidator implements ConstraintValidator<ValidModerationDecision, String> {
        @Override
        public boolean isValid(String decision, ConstraintValidatorContext context) {
            try {
                ModerationDecision.valueOf(decision.toUpperCase());
                return true;
            } catch (IllegalArgumentException ignored) {
                return false;
            }
        }
    }
}
