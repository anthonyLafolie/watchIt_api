package com.watchit.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.watchit.api.common.exception.CurrentUserAuthorizationException;
import com.watchit.api.controller.impl.AuthControllerImpl;
import com.watchit.api.dto.authentification.AuthDto;
import com.watchit.api.dto.authentification.CredentialDto;
import com.watchit.api.dto.user.UserDto;
import com.watchit.api.services.AuthService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;
@ExtendWith(MockitoExtension.class)  
public class AuthControllerTest {

    @InjectMocks
    private AuthControllerImpl authController;
    
    @Mock
    private AuthService authService;

    private CredentialDto credentialDto;
    private AuthDto authDto;
    private UserDto userDto;

    private static final String CREDENTIALS = "test";
    private static final String EMAIL = "test@test.fr";
    private static final String TOKEN = "TestToken";
    private static final Long USER_ID = 1L;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        credentialDto = new CredentialDto();
        credentialDto.setUsername(CREDENTIALS);
        credentialDto.setPassword(CREDENTIALS);
        authDto = new AuthDto();
        authDto.setUserId(USER_ID);
        authDto.setToken(TOKEN);
        userDto = new UserDto();
        userDto.setUsername(CREDENTIALS);
        userDto.setPassword(CREDENTIALS);
        userDto.setEmail(EMAIL);
    }

    @Test
    public void loginWhenBadCredentials() throws CurrentUserAuthorizationException{
        when(authService.getLogin(any(CredentialDto.class))).thenThrow(mock(AuthenticationException.class));
        ResponseEntity<AuthDto> response = authController.login(credentialDto);
        verify(authService).getLogin(credentialDto);
        verify(authService, times(0)).getLogin(not(eq(credentialDto)));
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());        
    }

    @Test
    public void loginWhenGoodCredentials() throws CurrentUserAuthorizationException{
        when(authService.getLogin(any(CredentialDto.class))).thenReturn(authDto);
        ResponseEntity<AuthDto> response = authController.login(credentialDto);
        verify(authService).getLogin(credentialDto);
        verify(authService, times(0)).getLogin(not(eq(credentialDto)));
        assertEquals(HttpStatus.OK, response.getStatusCode());        
        assertEquals(authDto, response.getBody());  
    }

    @Test
    public void signupWhenUserAlreadyExist() throws AuthenticationException, CurrentUserAuthorizationException{
        when(authService.createNewUser(any(UserDto.class))).thenReturn(false);
        ResponseEntity<AuthDto> response = authController.signup(userDto);
        verify(authService).createNewUser(userDto);
        verify(authService, times(0)).createNewUser(not(eq(userDto)));
        verify(authService,times(0)).getLogin(any(CredentialDto.class));
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void signupButCannotLogin() throws CurrentUserAuthorizationException{
        when(authService.createNewUser(any(UserDto.class))).thenReturn(true);
        when(authService.getLogin(any(CredentialDto.class))).thenThrow(mock(AuthenticationException.class));
        ResponseEntity<AuthDto> response = authController.signup(userDto);
        verify(authService).createNewUser(userDto);
        verify(authService, times(0)).createNewUser(not(eq(userDto)));
        verify(authService).getLogin(credentialDto);
        verify(authService, times(0)).getLogin(not(eq(credentialDto)));
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }

    @Test
    public void signupAndLogin() throws AuthenticationException, CurrentUserAuthorizationException{
        when(authService.createNewUser(any(UserDto.class))).thenReturn(true);
        when(authService.getLogin(any(CredentialDto.class))).thenReturn(authDto);
        ResponseEntity<AuthDto> response = authController.signup(userDto);
        verify(authService).createNewUser(userDto);
        verify(authService, times(0)).createNewUser(not(eq(userDto)));
        verify(authService).getLogin(credentialDto);
        verify(authService, times(0)).getLogin(not(eq(credentialDto)));
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(authDto,response.getBody());
    }
}
