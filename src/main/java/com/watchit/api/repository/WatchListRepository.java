package com.watchit.api.repository;

import java.util.List;

import com.watchit.api.entity.User;
import com.watchit.api.entity.WatchListMovie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchListRepository extends JpaRepository<WatchListMovie, Long>{

    List<WatchListMovie> findAllByUserWatchList(User user);
    
    List<WatchListMovie> findByIdMovieAndUserWatchList(int idMovie, User user);
}
