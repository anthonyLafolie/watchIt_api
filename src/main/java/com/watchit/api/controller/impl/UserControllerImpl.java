package com.watchit.api.controller.impl;

import com.watchit.api.common.exception.CurrentUserAuthorizationException;
import com.watchit.api.controller.UserController;
import com.watchit.api.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserControllerImpl implements  UserController{

    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<String> deleteCurrentUser() {
        try {
            userService.deleteCurrentUser();
        } catch (CurrentUserAuthorizationException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("Current user deleted", HttpStatus.OK);
    }
    
}
