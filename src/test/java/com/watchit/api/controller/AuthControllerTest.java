package com.watchit.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.watchit.api.common.exception.CurrentUserAuthorizationException;
import com.watchit.api.controller.impl.AuthControllerImpl;
import com.watchit.api.dto.authentification.AuthDto;
import com.watchit.api.dto.authentification.CredentialDto;
import com.watchit.api.services.AuthService;

import org.checkerframework.checker.units.qual.C;
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

@ExtendWith(MockitoExtension.class)  
public class AuthControllerTest {

    @InjectMocks
    private AuthControllerImpl authController;
    
    @Mock
    private AuthService authService;

    private CredentialDto credentialDto;
    private AuthDto authDto;

    private static final String TOKEN = "TestToken";
    private static final Long USER_ID = 1L;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        credentialDto = new CredentialDto();
        authDto = new AuthDto();
        authDto.setUserId(USER_ID);
        authDto.setToken(TOKEN);
    }

    @Test
    public void loginWhenBadCredentials() throws CurrentUserAuthorizationException{
        when(authService.getLogin(any(CredentialDto.class))).thenThrow(mock(AuthenticationException.class));
        ResponseEntity<AuthDto> response = authController.login(credentialDto);
        verify(authService).getLogin(any(CredentialDto.class));
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());        
    }

    @Test
    public void loginWhenGoodCredentials() throws CurrentUserAuthorizationException{
        when(authService.getLogin(any(CredentialDto.class))).thenReturn(authDto);
        ResponseEntity<AuthDto> response = authController.login(credentialDto);
        verify(authService).getLogin(any(CredentialDto.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());        
        assertEquals(authDto, response.getBody());  
    }
}
