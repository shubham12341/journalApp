package com.shubhamgangurde.journalApp.service;

import com.shubhamgangurde.journalApp.entity.User;
import com.shubhamgangurde.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.mockito.Mockito.*;
import org.springframework.test.context.aot.DisabledInAotMode;

import java.util.ArrayList;
import static org.mockito.Mockito.when;

@DisabledInAotMode
public class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Mock // for injecting our Mock Repository
    private UserRepository userRepository;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Disabled
    @Test
    public void testLoadUserByUsername(){
        when(userRepository.findByUserName(ArgumentMatchers.anyString())).thenReturn(User.builder().userName("shubhu").password("****").roles(new ArrayList<>()).build());
        UserDetails user = userDetailsServiceImpl.loadUserByUsername("shubhu");
        Assertions.assertNotNull(user);
    }
}
