package com.watchit.api.services;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import com.watchit.api.common.component.IAuthenticationFacade;
import com.watchit.api.common.constant.ExceptionMessage;
import com.watchit.api.common.exception.CurrentUserAuthorizationException;
import com.watchit.api.common.exception.UserAlreadyExistsException;
import com.watchit.api.common.exception.UserNotFoundException;
import com.watchit.api.dto.filter.FilterDto;
import com.watchit.api.dto.user.UserBaseDto;
import com.watchit.api.dto.user.UserDto;
import com.watchit.api.entity.Filter;
import com.watchit.api.entity.User;
import com.watchit.api.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    IAuthenticationFacade authenticationFacade;

    @Autowired
    FilterService filterService;

    public UserDto getCurrentUserDto() throws CurrentUserAuthorizationException {
        return convertUserEntityToDto(authenticationFacade.getCurrentUser());
    }

    public User getCurrentUser() throws CurrentUserAuthorizationException {
        return authenticationFacade.getCurrentUser();
    }

    public User getUserById(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException(String.format("No user with id: %d were found", userId));
        }
        return user;
    }

    public User getUserByUsername(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            throw new UserNotFoundException(String.format("No user with pseudo: %s were found", username));
        }
        return user;
    }

    public void createUser(UserDto userDto) throws UserAlreadyExistsException {
        if (usernameTaken(userDto.getUsername()) || emailTaken(userDto.getEmail().toLowerCase())) {
            throw new UserAlreadyExistsException(ExceptionMessage.PSEUDO_EMAIL_TAKEN);
        }
        userDto.setEmail(userDto.getEmail().toLowerCase());
        userRepository.save(convertUserDtoToEntityWithPasswordEncoding(userDto));
    }

    public UserDto convertUserEntityToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public User convertUserDtoToEntityWithPasswordEncoding(UserBaseDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        if (user.getPassword() != null) {
            user.setPassword(encoder.encode(userDto.getPassword()));
        }
        return user;
    }

    private boolean usernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    private boolean emailTaken(String email) {
        return userRepository.existsByEmail(email);
    }

    public void deleteCurrentUser() throws CurrentUserAuthorizationException {
        User user = authenticationFacade.getCurrentUser();
        userRepository.deleteById(user.getId());
    }

    public List<FilterDto> getFilters() throws CurrentUserAuthorizationException {
        User user = authenticationFacade.getCurrentUser();
        List<Filter> filters = filterService.getAllFiltersByUser(user);
        return convertFilterToFilterDto(filters);
    }

    public List<FilterDto> updateFilters(List<FilterDto> filtersDto) throws CurrentUserAuthorizationException {
        User user = authenticationFacade.getCurrentUser();
        List<Filter> filters_converted = convertFilterDtoToFilter(filtersDto, user);
        filterService.updateFilters(filters_converted);
        return getFilters();
    }

    public List<Filter> convertFilterDtoToFilter(List<FilterDto> filtersDto, User user){
        List<Filter> filters =  modelMapper.map(filtersDto,  new TypeToken<List<Filter>>() {}.getType());
        for (Filter filter : filters) {
            filter.setUserFilter(user);
            List<Filter> existing_filters = filterService.existingFilter(filter, user);
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
