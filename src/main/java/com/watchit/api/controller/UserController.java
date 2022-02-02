package com.watchit.api.controller;

import java.util.List;

import com.watchit.api.dto.user.UserDto;
import com.watchit.api.entity.Filter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin
@RequestMapping("/user")
public interface UserController {
    /***
     * get current user
     *
     * @return ResponseEntity<UserDto> user information
     */
    @GetMapping("/me")
    ResponseEntity<UserDto> getCurrentUser();

    /***
     * delete current user
     *
     * @return ResponseEntity<String> validation of deleting  or else : exception message when problem with user authenticated
     */
    @DeleteMapping("/me")
    ResponseEntity<String> deleteCurrentUser();

    /***
     * get Filters of current user
     *
     * @return ResponseEntity<Filters[]> list of filters
     */
    @GetMapping("/filters")
    ResponseEntity<List<Filter>> getCurrentUserFilters();
}
