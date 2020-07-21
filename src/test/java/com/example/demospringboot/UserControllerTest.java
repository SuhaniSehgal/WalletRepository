package com.example.demospringboot;

import com.example.demospringboot.controller.UserController;
import com.example.demospringboot.entity.User;
import com.example.demospringboot.service.serviceImpl.UserServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


public class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testCreateUser() throws Exception {

        User mockUser = new User();
        mockUser.setUserId(40);
        mockUser.setUserName("mockame");
        mockUser.setMobileNo("567788686");
        mockUser.setEmailId("adsffr@ggh.con");
        mockUser.setAddress1("dfg");
        mockUser.setAddress2("frg");


        when(userService.addUser(Mockito.any(User.class))).thenReturn(mockUser);

        ResponseEntity<User> userResponseEntity = userController.addUser(new User());

        assertEquals(HttpStatus.OK.value(), userResponseEntity.getStatusCode());

    }


}
