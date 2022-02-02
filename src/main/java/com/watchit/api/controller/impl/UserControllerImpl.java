package com.watchit.api.controller.impl;

import java.util.List;

import com.watchit.api.common.exception.CurrentUserAuthorizationException;
import com.watchit.api.controller.UserController;
import com.watchit.api.dto.user.UserDto;
import com.watchit.api.entity.Filter;
import com.watchit.api.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserControllerImpl implements UserController {

    @Autowired
    UserService userService;

    @Override
     public ResponseEntity<UserDto> getCurrentUser() {
        try {
            UserDto userDto = userService.getCurrentUserDto();
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } catch (CurrentUserAuthorizationException unfe) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public ResponseEntity<String> deleteCurrentUser() {
        try {
            userService.deleteCurrentUser();
        } catch (CurrentUserAuthorizationException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("Current user deleted", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Filter>> getCurrentUserFilters() {
        try {
            List<Filter> filters = userService.getFilters();
            return new ResponseEntity<List<Filter>>(filters, HttpStatus.OK);
        } catch (CurrentUserAuthorizationException unfe) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }


}
