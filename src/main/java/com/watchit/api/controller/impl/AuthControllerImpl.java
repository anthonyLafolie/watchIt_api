package com.watchit.api.controller.impl;

import com.watchit.api.common.exception.CurrentUserAuthorizationException;
import com.watchit.api.controller.AuthController;
import com.watchit.api.dto.authentification.AuthDto;
import com.watchit.api.dto.authentification.CredentialDto;
import com.watchit.api.dto.user.UserDto;
import com.watchit.api.services.AuthService;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthControllerImpl implements AuthController {
    @Autowired
    AuthService authService;

    @Override
    public ResponseEntity<AuthDto> login(CredentialDto credentials) {
        if (credentials.getUsername() == null || credentials.getUsername() == "" || credentials.getPassword() == null
                || credentials.getPassword() == "") {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            AuthDto authDto = authService.getLogin(credentials);
            return new ResponseEntity<>(authDto, HttpStatus.OK);
        } catch (AuthenticationException | CurrentUserAuthorizationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<AuthDto> signup(UserDto userDto) {
        if (userDto.getUsername() == null || userDto.getUsername() == "" || userDto.getPassword() == null
                || userDto.getPassword() == "") {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!isValidEmail(userDto.getEmail())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!authService.createNewUser(userDto)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        CredentialDto connect = new CredentialDto();
        connect.setUsername(userDto.getUsername());
        connect.setPassword(userDto.getPassword());
        try {
            return new ResponseEntity<>(authService.getLogin(connect), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public static boolean isValidEmail(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }
}
