package com.watchit.api.controller.impl;

import java.util.List;

import com.watchit.api.common.exception.CurrentUserAuthorizationException;
import com.watchit.api.controller.UserController;
import com.watchit.api.dto.filter.FilterDto;
import com.watchit.api.dto.movie.MovieDto;
import com.watchit.api.dto.user.UserDto;
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
    public ResponseEntity<List<FilterDto>> getCurrentUserFilters() {
        try {
            List<FilterDto> filters = userService.getFilters();
            return new ResponseEntity<List<FilterDto>>(filters, HttpStatus.OK);
        } catch (CurrentUserAuthorizationException unfe) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public ResponseEntity<List<MovieDto>> getCurrentUserWatchList() {
        try {
            List<MovieDto> moovies = userService.getWatchList();
            return new ResponseEntity<List<MovieDto>>(moovies, HttpStatus.OK);
        } catch (CurrentUserAuthorizationException unfe) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public ResponseEntity<List<MovieDto>> getCurrentUserAlreadySeenList() {
        try {
            List<MovieDto> moovies = userService.getAlredySeenList();
            return new ResponseEntity<List<MovieDto>>(moovies, HttpStatus.OK);
        } catch (CurrentUserAuthorizationException unfe) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public ResponseEntity<List<MovieDto>> getCurrentUserDontWantSeenList() {
        try {
            List<MovieDto> moovies = userService.getDontWantSeenList();
            return new ResponseEntity<List<MovieDto>>(moovies, HttpStatus.OK);
        } catch (CurrentUserAuthorizationException unfe) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

}
