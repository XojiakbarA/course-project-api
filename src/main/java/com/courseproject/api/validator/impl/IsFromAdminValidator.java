package com.courseproject.api.validator.impl;

import com.courseproject.api.entity.ERole;
import com.courseproject.api.validator.IsFromAdmin;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class IsFromAdminValidator implements ConstraintValidator<IsFromAdmin, Object> {


    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        if (object != null) {
            if (object instanceof List<?> && ((List<?>) object).isEmpty()) {
                return true;
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(ERole.ADMIN.name()));
        }
        return true;
    }

}
