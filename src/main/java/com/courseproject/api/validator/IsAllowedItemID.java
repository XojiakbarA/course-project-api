package com.courseproject.api.validator;

import com.courseproject.api.validator.impl.IsAllowedItemIDValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsAllowedItemIDValidator.class)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsAllowedItemID {

    String message() default "Only admin will be able to create/update on another item.";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};


}
