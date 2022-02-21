package com.watchit.api.controller.impl;

import java.util.List;

import com.watchit.api.common.exception.CurrentUserAuthorizationException;
import com.watchit.api.controller.AlreadySeenListController;
import com.watchit.api.dto.movie.MovieDto;
import com.watchit.api.services.AlreadySeenListService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlreadySeenListControllerImpl implements AlreadySeenListController{

    @Autowired
    AlreadySeenListService alreadySeenListService;

    @Override
    public ResponseEntity<List<MovieDto>> addMovie(MovieDto movieDto) {
        try {
            List<MovieDto> movie_dto_updated = alreadySeenListService.addmovie(movieDto);
            return new ResponseEntity<List<MovieDto>>(movie_dto_updated, HttpStatus.OK);
        } catch (CurrentUserAuthorizationException unfe) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
