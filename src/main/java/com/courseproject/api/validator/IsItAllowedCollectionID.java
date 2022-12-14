package com.courseproject.api.validator;

import com.courseproject.api.validator.impl.IsItAllowedCollectionIDValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsItAllowedCollectionIDValidator.class)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsItAllowedCollectionID {

    String message() default "Only admin will be able to create/update on another collection.";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

}
