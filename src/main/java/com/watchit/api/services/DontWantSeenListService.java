package com.watchit.api.services;

import java.util.List;

import com.watchit.api.common.component.IAuthenticationFacade;
import com.watchit.api.common.exception.CurrentUserAuthorizationException;
import com.watchit.api.dto.movie.MovieDto;
import com.watchit.api.entity.User;
import com.watchit.api.entity.DontWantSeenListMovie;
import com.watchit.api.repository.DontWantSeenListRepository;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DontWantSeenListService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    DontWantSeenListRepository dontWantSeenListRepository;

    @Autowired
    IAuthenticationFacade authenticationFacade;

    @Autowired
    UserService userService;

    public List<DontWantSeenListMovie> getDontWantSeenListByUser(User user) throws CurrentUserAuthorizationException {
        return dontWantSeenListRepository.findAllByUserDontWantSeenListMovie(user);
    }

    public List<DontWantSeenListMovie> existingDontWantSeenListMovie(DontWantSeenListMovie movie, User user) {
        return dontWantSeenListRepository.findByIdMovieAndUserDontWantSeenListMovie(movie.getIdMovie(), user);
    }

    public DontWantSeenListMovie convertMovieDtoToDontWantSeenListMovie(MovieDto DontWantSeenListMovieDto, User user) {
        DontWantSeenListMovie movie = modelMapper.map(DontWantSeenListMovieDto, DontWantSeenListMovie.class);
        movie.setUserDontWantSeenListMovie(user);
        List<DontWantSeenListMovie> existing_movie = existingDontWantSeenListMovie(movie, user);
        if (!existing_movie.isEmpty()) {
            movie.setId(existing_movie.get(0).getId());
        }
        return movie;
    }

    public List<MovieDto> convertDontWantSeenListMovieToMovieDto(List<DontWantSeenListMovie> DontWantSeenListMovie) {
        return modelMapper.map(DontWantSeenListMovie, new TypeToken<List<MovieDto>>() {
        }.getType());
    }

    public List<MovieDto> addmovie(MovieDto DontWantSeenListMovieDto) throws CurrentUserAuthorizationException {
        User user = authenticationFacade.getCurrentUser();
        DontWantSeenListMovie DontWantSeenList_converted = convertMovieDtoToDontWantSeenListMovie(
                DontWantSeenListMovieDto, user);
        dontWantSeenListRepository.save(DontWantSeenList_converted);
        return userService.getDontWantSeenList();
    }

}
