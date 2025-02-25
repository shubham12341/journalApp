package com.shubhamgangurde.journalApp.service;

import com.shubhamgangurde.journalApp.entity.User;
import com.shubhamgangurde.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserRepository userRepository;

    @Disabled
    @ParameterizedTest
    @CsvSource({
            "shubham",
            "alien",
            "shubh"
    })
    public void testGetUserByUserName(String userName){
//        User user = userRepository.findByUserName(userName);
//        assertNotNull(user.getJournalEntries());
        assertNotNull(userRepository.findByUserName(userName));
    }

}
