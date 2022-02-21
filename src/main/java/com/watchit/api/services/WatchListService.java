package com.watchit.api.services;

import java.util.List;

import com.watchit.api.common.component.IAuthenticationFacade;
import com.watchit.api.common.exception.CurrentUserAuthorizationException;
import com.watchit.api.dto.movie.MovieDto;
import com.watchit.api.entity.User;
import com.watchit.api.entity.WatchListMovie;
import com.watchit.api.repository.WatchListRepository;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WatchListService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    WatchListRepository watchListRepository;

    @Autowired
    IAuthenticationFacade authenticationFacade;

    @Autowired
    UserService userService;

    public List<WatchListMovie> getWatchListByUser(User user) throws CurrentUserAuthorizationException {
        return watchListRepository.findAllByUserWatchList(user);
    }

    public List<WatchListMovie> existingWatchListMovie(WatchListMovie movie, User user) {
        return watchListRepository.findByIdMovieAndUserWatchList(movie.getIdMovie(), user);
    }

    public List<WatchListMovie> convertMovieDtoToWatchListMovie(List<MovieDto> watchListDto, User user) {
        List<WatchListMovie> movies = modelMapper.map(watchListDto, new TypeToken<List<WatchListMovie>>() {
        }.getType());
        for (WatchListMovie movie : movies) {
            movie.setUserWatchList(user);
            List<WatchListMovie> existing_movie = existingWatchListMovie(movie, user);
            if (!existing_movie.isEmpty()) {
                movie.setId(existing_movie.get(0).getId());
            }
        }
        return movies;
    }

    public List<MovieDto> convertWatchListMovieToMovieDto(List<WatchListMovie> watchListMovie) {
        return modelMapper.map(watchListMovie, new TypeToken<List<MovieDto>>() {
        }.getType());
    }

    public List<MovieDto> addmovie(List<MovieDto> watchlistDto) throws CurrentUserAuthorizationException {
        User user = authenticationFacade.getCurrentUser();
        List<WatchListMovie> watchlist_converted = convertMovieDtoToWatchListMovie(watchlistDto, user);
        watchListRepository.saveAll(watchlist_converted);
        return userService.getWatchList();
    }

}
