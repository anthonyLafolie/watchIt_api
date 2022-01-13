package com.watchit.api.service;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.watchit.api.common.component.IAuthenticationFacade;
import com.watchit.api.common.exception.CurrentUserAuthorizationException;
import com.watchit.api.common.exception.UserAlreadyExistsException;
import com.watchit.api.dto.authentification.AuthDto;
import com.watchit.api.dto.authentification.CredentialDto;
import com.watchit.api.dto.user.UserDto;
import com.watchit.api.entity.User;
import com.watchit.api.security.JwtGenerator;
import com.watchit.api.services.AuthService;
import com.watchit.api.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtGenerator jwtGenerator;

    @Mock
    private IAuthenticationFacade authenticationFacade;
    

    private CredentialDto credentialDto;
    private AuthDto authDto;
    private UserDto userDto;
    private User user;
    private TestingAuthenticationToken auth;
    private UsernamePasswordAuthenticationToken userAuthToken;

    private static final String CREDENTIALS = "test";
    private static final String EMAIL = "test@test.fr";
    private static final String TOKEN = "TestToken";
    private static final Long USER_ID = 1L;

    private CredentialDto buildCredentialDto(){
        CredentialDto credentialDto = new CredentialDto();
        credentialDto.setUsername(CREDENTIALS);
        credentialDto.setPassword(CREDENTIALS);
        return credentialDto;
    }

    private AuthDto buildAuthDto(){
        AuthDto authDto = new AuthDto();
        authDto.setToken(TOKEN);
        authDto.setUserId(USER_ID);
        return authDto;
    }

    public UserDto buildUserDto(){
        UserDto userDto = new UserDto();
        userDto.setUsername(CREDENTIALS);
        userDto.setPassword(CREDENTIALS);
        userDto.setEmail(EMAIL);
        return userDto;
    }

    public User buildUser(){
        User user = new User();
        user.setUsername(CREDENTIALS);
        user.setId(USER_ID);
        return user;
    }


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        credentialDto = buildCredentialDto();
        authDto = buildAuthDto();
        userDto = buildUserDto();
        auth = new TestingAuthenticationToken(credentialDto, credentialDto.getPassword());
        userAuthToken = new UsernamePasswordAuthenticationToken(credentialDto.getUsername(), credentialDto.getPassword());
        user = buildUser();
    }

    @Test
    public void createNewUserRetuenFalseWhenUserAlreadyExist() throws UserAlreadyExistsException{
        doThrow(UserAlreadyExistsException.class).when(userService).createUser(any(UserDto.class));
        boolean created = authService.createNewUser(userDto);
        verify(userService).createUser(userDto); 
        verify(userService, times(0)).createUser(not(eq(userDto)));
        assertFalse(created);
    }

    @Test
    public void createNewUserRetrunTrueWhenAllGood() throws UserAlreadyExistsException{
        doNothing().when(userService).createUser(any(UserDto.class));
        boolean created = authService.createNewUser(userDto);
        verify(userService).createUser(userDto); 
        verify(userService, times(0)).createUser(not(eq(userDto)));
        assertTrue(created);
    }

    @Test
    public void getLoginThrowsBadCredentialsExceptionWhenBadCredentials() throws AuthenticationException, CurrentUserAuthorizationException{
        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(BadCredentialsException.class);
        assertThatExceptionOfType(BadCredentialsException.class)
                .isThrownBy(() -> authService.getLogin(credentialDto));
    }

    @Test
    public void getLoginReturnGoodInformationWhenAllIsCorrect() throws AuthenticationException, CurrentUserAuthorizationException{
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(auth);
        when(jwtGenerator.generateToken(auth)).thenReturn(TOKEN);
        when(authenticationFacade.getCurrentUser()).thenReturn(user);
        AuthDto loginInformations = authService.getLogin(credentialDto);
        verify(authenticationManager).authenticate(userAuthToken);
        verify(authenticationManager, times(0)).authenticate(not(eq(userAuthToken)));
        verify(jwtGenerator).generateToken(auth);
        verify(jwtGenerator, times(0)).generateToken(not(eq(auth)));
        assertEquals(authDto, loginInformations);
    }



}
