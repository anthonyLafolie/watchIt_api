package com.watchit.api.services;

import java.util.List;

import com.watchit.api.common.component.IAuthenticationFacade;
import com.watchit.api.common.exception.CurrentUserAuthorizationException;
import com.watchit.api.dto.filter.FilterDto;
import com.watchit.api.entity.Filter;
import com.watchit.api.entity.User;
import com.watchit.api.repository.FilterRepository;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FilterService {

    @Autowired
    FilterRepository filterRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    IAuthenticationFacade authenticationFacade;

    @Autowired
    UserService userService;

    public List<Filter> getAllFiltersByUser(User user) throws CurrentUserAuthorizationException {
        return filterRepository.findAllByUserFilter(user);
    }


    public List<Filter> existingFilter(Filter filter,User user){
        return filterRepository.findByNameAndUserFilter(filter.getName(), user);
    }

    public List<FilterDto> updateFilters(List<FilterDto> filtersDto) throws CurrentUserAuthorizationException {
        User user = authenticationFacade.getCurrentUser();
        List<Filter> filters_converted = convertFilterDtoToFilter(filtersDto, user);
        filterRepository.saveAll(filters_converted);
        return userService.getFilters();
    }

    public List<Filter> convertFilterDtoToFilter(List<FilterDto> filtersDto, User user){
        List<Filter> filters =  modelMapper.map(filtersDto,  new TypeToken<List<Filter>>() {}.getType());
        for (Filter filter : filters) {
            filter.setUserFilter(user);
            List<Filter> existing_filters = existingFilter(filter, user);
            if(!existing_filters.isEmpty()){
                filter.setId(existing_filters.get(0).getId());
            }
        }
        return filters;
    }

    public List<FilterDto> convertFilterToFilterDto(List<Filter> filters){
        return modelMapper.map(filters,  new TypeToken<List<FilterDto>>() {}.getType());
    }
    
    
}
