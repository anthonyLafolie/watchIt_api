package com.watchit.api.controller;

import com.watchit.api.dto.authentification.AuthDto;
import com.watchit.api.dto.authentification.CredentialDto;
import com.watchit.api.dto.user.UserDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin
@RequestMapping("/auth")
public interface AuthController {
    /***
     * User identification
     *
     * @param credentials the information allowing the user to connect
     * @return ResponseEntity<AuthDto> user credentials
     */
    @PostMapping("/login")
    ResponseEntity<AuthDto> login(@RequestBody CredentialDto credentials);

    /***
     * Création d'un compte utilisateur
     *
     * @param userDto the information to create a user
     * @return ResponseEntity<AuthDto> user credentials
     */
    @PostMapping("/signup")
    ResponseEntity<AuthDto> signup(@RequestBody UserDto userDto);
}
