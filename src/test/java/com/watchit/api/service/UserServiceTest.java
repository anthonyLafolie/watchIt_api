package com.watchit.api.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.watchit.api.common.component.IAuthenticationFacade;
import com.watchit.api.common.constant.ExceptionMessage;
import com.watchit.api.common.exception.CurrentUserAuthorizationException;
import com.watchit.api.common.exception.UserAlreadyExistsException;
import com.watchit.api.common.exception.UserNotFoundException;
import com.watchit.api.dto.filter.FilterDto;
import com.watchit.api.dto.user.UserDto;
import com.watchit.api.entity.Filter;
import com.watchit.api.entity.User;
import com.watchit.api.repository.UserRepository;
import com.watchit.api.services.FilterService;
import com.watchit.api.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    ModelMapper modelMapper;

    @Mock
    FilterService filterService;

    @Mock
    PasswordEncoder encoder;

    @Mock
    private UserRepository userRepository;


    @Mock
    private IAuthenticationFacade authenticationFacade;

    private User user;
    private UserDto userDto;
    private List<Filter> filters;
    private List<FilterDto> filtersDto;

    private static final String CREDENTIALS = "test";
    private static final String ENCODED_PASSWORD = "testEncoded";
    private static final String EMAIL = "test@test.fr";
    private static final Long USER_ID = 1L;


    public UserDto buildUserDto() {
        UserDto userDto = new UserDto();
        userDto.setUsername(CREDENTIALS);
        userDto.setPassword(CREDENTIALS);
        userDto.setEmail(EMAIL);
        return userDto;
    }

    public User buildUser() {
        User user = new User();
        user.setUsername(CREDENTIALS);
        user.setPassword(CREDENTIALS);
        user.setId(USER_ID);
        user.setEmail(EMAIL);
        user.setFilters(filters);
        return user;
    }

    public List<Filter> buildFilters(){
        List<Filter> filters = new ArrayList<>();
        filters.add(buildFilter(0, "action", true));
        filters.add(buildFilter(1, "sci-fi", false));
        return filters;
    }

    public Filter buildFilter(int id, String name, boolean checked) {
        Filter filter = new Filter();
        filter.setId(id);
        filter.setName(name);
        filter.setChecked(checked);
        return filter;
    }

    public List<FilterDto> buildFiltersDto() {
        List<FilterDto> filtersDto = new ArrayList<>();
        filtersDto.add(buildFilterDto("action", true));
        filtersDto.add(buildFilterDto("sci-fi", false));
        return filtersDto;
    }

    public FilterDto buildFilterDto(String name, boolean checked) {
        FilterDto filter = new FilterDto();
        filter.setName(name);
        filter.setChecked(checked);
        return filter;
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        filtersDto = buildFiltersDto();
        filters = buildFilters();
        userDto = buildUserDto();
        user = buildUser();
    }

    @Test
    public void getCurrentUserDtoThrowsCurrentUserAuthorizationExceptionWhenUserNotFound()
            throws CurrentUserAuthorizationException {
        when(authenticationFacade.getCurrentUser()).thenThrow(
                new CurrentUserAuthorizationException(ExceptionMessage.CURRENT_USER_CANNOT_BE_AUTHENTICATED));
        assertThatExceptionOfType(CurrentUserAuthorizationException.class)
                .isThrownBy(() -> userService.getCurrentUserDto())
                .withMessage(ExceptionMessage.CURRENT_USER_CANNOT_BE_AUTHENTICATED);
        verify(authenticationFacade).getCurrentUser();
    }

    @Test
    public void getCurrentUserDtoReturnCurrentUserDto() throws CurrentUserAuthorizationException {
        when(authenticationFacade.getCurrentUser()).thenReturn(user);
        when(userService.convertUserEntityToDto(user)).thenReturn(userDto);
        UserDto currentUserDto = userService.getCurrentUserDto();
        verify(authenticationFacade).getCurrentUser();
        verify(modelMapper).map(user, UserDto.class);
        verify(modelMapper, times(0)).map(not(eq(user)), any(UserDto.class));
        assertEquals(userDto, currentUserDto);
    }

    @Test
    public void getCurrentUserThrowsCurrentUserAuthorizationExceptionWhenUserNotFound()
            throws CurrentUserAuthorizationException {
        when(authenticationFacade.getCurrentUser()).thenThrow(
                new CurrentUserAuthorizationException(ExceptionMessage.CURRENT_USER_CANNOT_BE_AUTHENTICATED));
        assertThatExceptionOfType(CurrentUserAuthorizationException.class)
                .isThrownBy(() -> userService.getCurrentUser())
                .withMessage(ExceptionMessage.CURRENT_USER_CANNOT_BE_AUTHENTICATED);
        verify(authenticationFacade).getCurrentUser();
    }

    @Test
    public void getCurrentUserReturnCurrentUser() throws CurrentUserAuthorizationException {
        when(authenticationFacade.getCurrentUser()).thenReturn(user);
        User currentUser = userService.getCurrentUser();
        verify(authenticationFacade).getCurrentUser();
        assertEquals(user, currentUser);
    }

    @Test
    public void getUserByIdthrowsUserNotFoundExceptionWhenUserNotExist() throws UserNotFoundException {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.getUserById(USER_ID));
        verify(userRepository).findById(USER_ID);
        verify(userRepository, times(0)).findById(not(eq(USER_ID)));
    }

    @Test
    public void getUserByIdReturnGoodUser() throws UserNotFoundException {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        User userById = userService.getUserById(USER_ID);
        verify(userRepository).findById(USER_ID);
        verify(userRepository, times(0)).findById(not(eq(USER_ID)));
        assertEquals(user, userById);
    }

    @Test
    public void getUserByUsernamethrowsUserNotFoundExceptionWhenUserNotExist() throws UserNotFoundException {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.getUserByUsername(CREDENTIALS));
        verify(userRepository).findByUsername(CREDENTIALS);
        verify(userRepository, times(0)).findByUsername(not(eq(CREDENTIALS)));
    }

    @Test
    public void getUserByUsernameReturnGoodUser() throws UserNotFoundException {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        User userByUsername = userService.getUserByUsername(CREDENTIALS);
        verify(userRepository).findByUsername(CREDENTIALS);
        verify(userRepository, times(0)).findByUsername(not(eq(CREDENTIALS)));
        assertEquals(user, userByUsername);
    }

    @Test
    public void createUserThrowsUserAlreadyExistsExceptionWhenUsernameAlreadyExist() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        assertThatExceptionOfType(UserAlreadyExistsException.class)
                .isThrownBy(() -> userService.createUser(userDto))
                .withMessage(ExceptionMessage.PSEUDO_EMAIL_TAKEN);
        verify(userRepository).existsByUsername(CREDENTIALS);
        verify(userRepository, times(0)).existsByUsername(not(eq(CREDENTIALS)));
        verify(userRepository, times(0)).existsByEmail(EMAIL);
        verify(modelMapper, times(0)).map(userDto, User.class);
        verify(userRepository, times(0)).save(user);
    }

    @Test
    public void createUserThrowsUserAlreadyExistsExceptionWhenEmailAlreadyExist() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        assertThatExceptionOfType(UserAlreadyExistsException.class)
                .isThrownBy(() -> userService.createUser(userDto))
                .withMessage(ExceptionMessage.PSEUDO_EMAIL_TAKEN);
        verify(userRepository).existsByUsername(CREDENTIALS);
        verify(userRepository, times(0)).existsByUsername(not(eq(CREDENTIALS)));
        verify(userRepository).existsByEmail(EMAIL);
        verify(userRepository, times(0)).existsByEmail(not(eq(EMAIL)));
        verify(modelMapper, times(0)).map(userDto, User.class);
        verify(userRepository, times(0)).save(user);
    }

    @Test
    public void createUserDontThrowsUserAlreadyExistsExceptionWhenUserNotExist() throws UserAlreadyExistsException {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(modelMapper.map(userDto, User.class)).thenReturn(user);
        assertDoesNotThrow(() -> userService.createUser(userDto));
        verify(userRepository).existsByUsername(CREDENTIALS);
        verify(userRepository, times(0)).existsByUsername(not(eq(CREDENTIALS)));
        verify(userRepository).existsByEmail(EMAIL);
        verify(userRepository, times(0)).existsByEmail(not(eq(EMAIL)));
        verify(modelMapper).map(userDto, User.class);
        verify(modelMapper, times(0)).map(not(eq(userDto)), any(User.class));
        verify(userRepository).save(user);
        verify(userRepository, times(0)).save(not(eq(user)));
    }

    @Test
    public void convertUserDtoToEntityWithPasswordEncodingWithNoPassword() {
        user.setPassword(null);
        when(modelMapper.map(userDto, User.class)).thenReturn(user);
        User convertedUser = userService.convertUserDtoToEntityWithPasswordEncoding(userDto);
        verify(modelMapper).map(userDto, User.class);
        verify(modelMapper, times(0)).map(not(eq(userDto)), any(User.class));
        verify(encoder, times(0)).encode(anyString());
        assertEquals(user, convertedUser);
    }

    @Test
    public void convertUserDtoToEntityWithPasswordEncodingWithPassword() {
        when(modelMapper.map(userDto, User.class)).thenReturn(user);
        when(encoder.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        User convertedUser = userService.convertUserDtoToEntityWithPasswordEncoding(userDto);
        verify(modelMapper).map(userDto, User.class);
        verify(modelMapper, times(0)).map(not(eq(userDto)), any(User.class));
        verify(encoder).encode(CREDENTIALS);
        verify(encoder, times(0)).encode(not(eq(CREDENTIALS)));
        assertEquals(ENCODED_PASSWORD, convertedUser.getPassword());
    }

    @Test
    public void deleteCurrentUserThrowsCurrentUserAuthorizationExceptionWhenNotExistingUser()
            throws CurrentUserAuthorizationException {
        when(authenticationFacade.getCurrentUser()).thenThrow(
                new CurrentUserAuthorizationException(ExceptionMessage.CURRENT_USER_CANNOT_BE_AUTHENTICATED));
        assertThatExceptionOfType(CurrentUserAuthorizationException.class)
                .isThrownBy(() -> userService.getCurrentUserDto())
                .withMessage(ExceptionMessage.CURRENT_USER_CANNOT_BE_AUTHENTICATED);
        verify(authenticationFacade).getCurrentUser();
    }

    @Test
    void deleteCurrentUserWellDeleted() throws CurrentUserAuthorizationException {
        when(authenticationFacade.getCurrentUser()).thenReturn(user);
        assertDoesNotThrow(() -> userService.deleteCurrentUser());
        verify(userRepository).deleteById(user.getId());
        verify(userRepository, times(0)).deleteById(not(eq(user.getId())));
    }

    @Test
    public void getFiltersThrowsCurrentUserAuthorizationExceptionWhenNotAuthentificated()
            throws CurrentUserAuthorizationException {
        when(authenticationFacade.getCurrentUser()).thenThrow(
                new CurrentUserAuthorizationException(ExceptionMessage.CURRENT_USER_CANNOT_BE_AUTHENTICATED));
        assertThatExceptionOfType(CurrentUserAuthorizationException.class)
                .isThrownBy(() -> userService.getFilters())
                .withMessage(ExceptionMessage.CURRENT_USER_CANNOT_BE_AUTHENTICATED);
        verify(authenticationFacade).getCurrentUser();
        verify(filterService, times(0)).getAllFiltersByUser(any(User.class));
    }

    @Test
    public void getFilterswhenAuthentificated()
            throws CurrentUserAuthorizationException {
        when(authenticationFacade.getCurrentUser()).thenReturn(user);
        when(filterService.getAllFiltersByUser(user)).thenReturn(filters);
        when(filterService.convertFilterToFilterDto(filters)).thenReturn(filtersDto);
        List<FilterDto> result = userService.getFilters();
        verify(authenticationFacade).getCurrentUser();
        verify(filterService).getAllFiltersByUser(user);
        verify(filterService, times(0)).getAllFiltersByUser(not(eq(user)));
        verify(filterService).convertFilterToFilterDto(filters);
        verify(filterService, times(0)).convertFilterToFilterDto(not(eq(filters)));
        assertEquals(filtersDto, result);
    }
}
