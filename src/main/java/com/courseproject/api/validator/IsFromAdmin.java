package com.courseproject.api.validator;

import com.courseproject.api.validator.impl.IsFromAdminValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsFromAdminValidator.class)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsFromAdmin {

    String message() default "Only admin will be able to create/update on another user.";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

}
