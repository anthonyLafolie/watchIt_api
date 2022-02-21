package com.watchit.api.services;

import java.util.List;

import com.watchit.api.common.component.IAuthenticationFacade;
import com.watchit.api.common.exception.CurrentUserAuthorizationException;
import com.watchit.api.dto.movie.MovieDto;
import com.watchit.api.entity.User;
import com.watchit.api.entity.AlreadySeenListMovie;
import com.watchit.api.repository.AlreadySeenListRepository;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlreadySeenListService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AlreadySeenListRepository alreadySeenListRepository;

    @Autowired
    IAuthenticationFacade authenticationFacade;

    @Autowired
    UserService userService;

    public List<AlreadySeenListMovie> getAlreadySeenListByUser(User user) throws CurrentUserAuthorizationException {
        return alreadySeenListRepository.findAllByUserAlreadySeenList(user);
    }

    public List<AlreadySeenListMovie> existingAlreadySeenListMovie(AlreadySeenListMovie movie, User user) {
        return alreadySeenListRepository.findByIdMovieAndUserAlreadySeenList(movie.getIdMovie(), user);
    }

    public List<AlreadySeenListMovie> convertMovieDtoToAlreadySeenListMovie(List<MovieDto> AlreadySeenListDto, User user) {
        List<AlreadySeenListMovie> movies = modelMapper.map(AlreadySeenListDto, new TypeToken<List<AlreadySeenListMovie>>() {
        }.getType());
        for (AlreadySeenListMovie movie : movies) {
            movie.setUserAlreadySeenList(user);
            List<AlreadySeenListMovie> existing_movie = existingAlreadySeenListMovie(movie, user);
            if (!existing_movie.isEmpty()) {
                movie.setId(existing_movie.get(0).getId());
            }
        }
        return movies;
    }

    public List<MovieDto> convertAlreadySeenListMovieToMovieDto(List<AlreadySeenListMovie> AlreadySeenListMovie) {
        return modelMapper.map(AlreadySeenListMovie, new TypeToken<List<MovieDto>>() {
        }.getType());
    }

    public List<MovieDto> addmovie(List<MovieDto> AlreadySeenListDto) throws CurrentUserAuthorizationException {
        User user = authenticationFacade.getCurrentUser();
        List<AlreadySeenListMovie> AlreadySeenList_converted = convertMovieDtoToAlreadySeenListMovie(AlreadySeenListDto, user);
        alreadySeenListRepository.saveAll(AlreadySeenList_converted);
        return userService.getAlredySeenList();
    }

}
