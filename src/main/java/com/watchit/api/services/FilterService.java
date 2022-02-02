package com.watchit.api.services;

import java.util.List;

import com.watchit.api.common.exception.CurrentUserAuthorizationException;
import com.watchit.api.entity.Filter;
import com.watchit.api.entity.User;
import com.watchit.api.repository.FilterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FilterService {

    @Autowired
    FilterRepository filterRepository;

    public List<Filter> getAllFiltersByUser(User user) throws CurrentUserAuthorizationException {
        return filterRepository.findAllByUserFilter(user);
    }

    public List<Filter> updateFilters(List<Filter> filters) throws CurrentUserAuthorizationException {
        return filterRepository.saveAll(filters);
    }

    public List<Filter> existingFilter(Filter filter,User user){
        return filterRepository.findByNameAndUserFilter(filter.getName(), user);
    }
    
}
