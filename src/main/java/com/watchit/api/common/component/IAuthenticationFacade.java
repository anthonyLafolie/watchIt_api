package com.watchit.api.common.component;

import com.watchit.api.common.exception.CurrentUserAuthorizationException;
import com.watchit.api.entity.User;
import com.watchit.api.security.UserAuthDetails;

public interface IAuthenticationFacade {
    UserAuthDetails getCurrentUserAuthDetails() throws CurrentUserAuthorizationException;

    User getCurrentUser() throws CurrentUserAuthorizationException;
}
