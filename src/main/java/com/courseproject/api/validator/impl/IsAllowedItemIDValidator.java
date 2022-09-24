package com.courseproject.api.validator.impl;

import com.courseproject.api.entity.enums.ERole;
import com.courseproject.api.entity.Item;
import com.courseproject.api.service.ItemService;
import com.courseproject.api.validator.IsAllowedItemID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsAllowedItemIDValidator implements ConstraintValidator<IsAllowedItemID, Long> {

    @Autowired
    private ItemService itemService;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Item item = itemService.getById(id);
        if (item.getCollection().getUser().getEmail().equals(authentication.getName())) {
            return true;
        }
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(ERole.ADMIN.name()));
    }
}
