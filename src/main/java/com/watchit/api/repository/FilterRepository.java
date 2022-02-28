package com.watchit.api.repository;

import java.util.List;

import com.watchit.api.entity.Filter;
import com.watchit.api.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FilterRepository extends JpaRepository<Filter, Long> {

	List<Filter> findAllByUserFilter(User user);

	List<Filter> findByNameAndUserFilter(String name, User user);
}