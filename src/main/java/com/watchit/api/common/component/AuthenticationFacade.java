package com.watchit.api.common.component;

import com.watchit.api.common.constant.ExceptionMessage;
import com.watchit.api.common.exception.CurrentUserAuthorizationException;
import com.watchit.api.common.exception.UserNotFoundException;
import com.watchit.api.entity.User;
import com.watchit.api.security.UserAuthDetails;
import com.watchit.api.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {

    @Autowired
    UserService userService;

    @Override
    public UserAuthDetails getCurrentUserAuthDetails() throws CurrentUserAuthorizationException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object userAuth = auth.getPrincipal();
        if (userAuth instanceof UserAuthDetails) {
            return (UserAuthDetails) userAuth;
        }
        throw new CurrentUserAuthorizationException(ExceptionMessage.CURRENT_USER_CANNOT_BE_AUTHENTICATED);
    }

    public User getCurrentUser() throws CurrentUserAuthorizationException {
        try {
            UserAuthDetails currentUserAuth = this.getCurrentUserAuthDetails();
            return userService.getUserByUsername(currentUserAuth.getUsername());
        } catch (UserNotFoundException e) {
            throw new CurrentUserAuthorizationException(ExceptionMessage.CURRENT_USER_CANNOT_BE_AUTHENTICATED);
        }
    }
}