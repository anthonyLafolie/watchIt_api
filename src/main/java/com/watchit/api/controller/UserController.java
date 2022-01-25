package com.watchit.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin
@RequestMapping("/user")
public interface UserController {
    /***
     * delete current user
     *
     * @return ResponseEntity<String> validation of deleting  or else : exception message when problem with user authenticated
     */
    @DeleteMapping("/me")
    ResponseEntity<String> deleteCurrentUser();
}
