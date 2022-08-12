package com.courseproject.api.security.oauth2.user;

import com.courseproject.api.entity.EAuthProvider;
import com.courseproject.api.exception.OAuth2AuthenticationProcessingException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(EAuthProvider.google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(EAuthProvider.facebook.toString())) {
            return new FacebookOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(EAuthProvider.github.toString())) {
            return new GithubOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }

}
