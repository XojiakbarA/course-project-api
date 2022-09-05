package com.courseproject.api.validator.impl;

import com.courseproject.api.entity.enums.ERole;
import com.courseproject.api.entity.Item;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.repository.ItemRepository;
import com.courseproject.api.validator.IsAllowedItemID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsAllowedItemIDValidator implements ConstraintValidator<IsAllowedItemID, Long> {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Item item = itemRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Item with id: " + id + " not found.")
        );
        if (item.getCollection().getUser().getEmail().equals(authentication.getName())) {
            return true;
        }
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(ERole.ADMIN.name()));
    }
}
