package com.watchit.api.controller.impl;

import java.util.List;

import com.watchit.api.common.exception.CurrentUserAuthorizationException;
import com.watchit.api.controller.AlreadySeenListController;
import com.watchit.api.controller.DontWantSeenListController;
import com.watchit.api.dto.movie.MovieDto;
import com.watchit.api.services.DontWantSeenListService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DontWantSeenListControllerImpl implements DontWantSeenListController{

    @Autowired
    DontWantSeenListService dontWantSeenListService;

    @Override
    public ResponseEntity<List<MovieDto>> addMovie(List<MovieDto> movieDto) {
        try {
            List<MovieDto> movie_dto_updated = dontWantSeenListService.addmovie(movieDto);
            return new ResponseEntity<List<MovieDto>>(movie_dto_updated, HttpStatus.OK);
        } catch (CurrentUserAuthorizationException unfe) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
