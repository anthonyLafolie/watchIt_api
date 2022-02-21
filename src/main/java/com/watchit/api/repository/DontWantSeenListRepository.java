package com.watchit.api.repository;

import java.util.List;

import com.watchit.api.entity.User;
import com.watchit.api.entity.DontWantSeenListMovie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DontWantSeenListRepository extends JpaRepository<DontWantSeenListMovie, Long>{

    List<DontWantSeenListMovie> findAllByUserDontWantSeenListMovie(User user);
    
    List<DontWantSeenListMovie> findByIdMovieAndUserDontWantSeenListMovie(int idMovie, User user);
}
