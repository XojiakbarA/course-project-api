package com.courseproject.api.service.impl;

import com.courseproject.api.entity.User;
import com.courseproject.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final Locale locale = LocaleContextHolder.getLocale();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Object[] arguments = new Object[] { email };
        String message = messageSource.getMessage("user.email.notFound", arguments, locale);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(message));
        return UserDetailsImpl.build(user);
    }

}
