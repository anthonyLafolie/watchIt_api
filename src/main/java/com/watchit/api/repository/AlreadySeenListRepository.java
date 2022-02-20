package com.watchit.api.repository;

import java.util.List;

import com.watchit.api.entity.User;
import com.watchit.api.entity.AlreadySeenListMovie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlreadySeenListRepository extends JpaRepository<AlreadySeenListMovie, Long>{

    List<AlreadySeenListMovie> findAllByUserAlreadySeenList(User user);
    
    List<AlreadySeenListMovie> findByIdMovieAndUserAlreadySeenList(int idMovie, User user);
}
