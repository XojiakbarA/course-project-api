package com.courseproject.api.validator;

import com.courseproject.api.request.RegisterRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPassword, Object> {

    private String field;

    private String fieldMatch;

    private String message;

    @Override
    public void initialize(final ConfirmPassword constraintAnnotation) {
        this.message = constraintAnnotation.message();
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        RegisterRequest request = (RegisterRequest) object;
        List<String> fields = new ArrayList<>();
        fields.add(field);
        fields.add(fieldMatch);
        fields.forEach(f -> {
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(f)
                    .addConstraintViolation();
        });
        return ((RegisterRequest) object).getPassword().equals(request.getConfirmPassword());
    }

}
