package com.courseproject.api.validator;

import com.courseproject.api.entity.Collection;
import com.courseproject.api.entity.ERole;
import com.courseproject.api.entity.User;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.repository.CollectionRepository;
import com.courseproject.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsItAllowedCollectionIDValidator implements ConstraintValidator<IsItAllowedCollectionID, Long> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @Override
    public boolean isValid(Long collectionId, ConstraintValidatorContext constraintValidatorContext) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(
                () -> new ResourceNotFoundException("User with email: " + authentication.getName() + " not found.")
        );
        Collection collection = user.getCollections().stream()
                .filter(c -> c.getId().equals(collectionId))
                .findFirst().orElse(null);

        if (collection == null) {
            return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(ERole.ADMIN.name()));
        }
        return true;
    }

}
