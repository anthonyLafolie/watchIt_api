package com.watchit.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.watchit.api.common.constant.ExceptionMessage;
import com.watchit.api.common.exception.CurrentUserAuthorizationException;
import com.watchit.api.controller.impl.UserControllerImpl;
import com.watchit.api.services.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    
    @InjectMocks
    private UserControllerImpl userController;
    
    @Mock
    private UserService userService;

    @Test
    void deleteCurrentUserNotFound() throws CurrentUserAuthorizationException {
        doThrow(new CurrentUserAuthorizationException(ExceptionMessage.CURRENT_USER_CANNOT_BE_AUTHENTICATED)).when(userService).deleteCurrentUser();

        ResponseEntity<String> response = userController.deleteCurrentUser();

        verify(userService).deleteCurrentUser();
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void deleteCurrentUserFound() throws CurrentUserAuthorizationException {
        ResponseEntity<String> response = userController.deleteCurrentUser();
        
        verify(userService).deleteCurrentUser();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    
}
