package com.courseproject.api.validator.impl;

import com.courseproject.api.entity.enums.ERole;
import com.courseproject.api.entity.User;
import com.courseproject.api.service.UserService;
import com.courseproject.api.validator.IsItAllowedUserID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsItAllowedUserIDValidator implements ConstraintValidator<IsItAllowedUserID, Long> {

    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(Long userId, ConstraintValidatorContext constraintValidatorContext) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getByEmail(authentication.getName());
        if (!user.getId().equals(userId)) {
            return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(ERole.ADMIN.name()));
        }
        return true;
    }
}
