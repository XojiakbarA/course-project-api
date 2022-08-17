package com.courseproject.api.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsItAllowedIDValidator.class)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsItAllowedID {

    String message() default "Only admin will be able to create/update on another user.";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

}
