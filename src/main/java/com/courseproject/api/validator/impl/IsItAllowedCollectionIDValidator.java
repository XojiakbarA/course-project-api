package com.courseproject.api.validator.impl;

import com.courseproject.api.entity.Collection;
import com.courseproject.api.entity.enums.ERole;
import com.courseproject.api.service.CollectionService;
import com.courseproject.api.validator.IsItAllowedCollectionID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsItAllowedCollectionIDValidator implements ConstraintValidator<IsItAllowedCollectionID, Long> {

    @Autowired
    private CollectionService collectionService;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection collection = collectionService.getById(id);
        if (collection.getUser().getEmail().equals(authentication.getName())) {
            return true;
        }
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(ERole.ADMIN.name()));
    }

}
