package com.watchit.api.controller;

import java.util.List;

import com.watchit.api.dto.movie.MovieDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@CrossOrigin
@RequestMapping("/watchlist")
public interface WatchListController {
    /***
     * add movie in watchlist of current user
     *
     * @return ResponseEntity<WatchListMovie[]> list of Movie
     */
    @PostMapping()
    ResponseEntity<List<MovieDto>> addMovie(@RequestBody List<MovieDto> watchListMovieDto);

}
