package com.shubhamgangurde.journalApp.service;

import com.shubhamgangurde.journalApp.entity.User;
import com.shubhamgangurde.journalApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    Authentication authentication = null;

    public void addNewUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        userRepository.save(user);
    }
    public void addNewAdminUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER","ADMIN"));
        userRepository.save(user);
    }

    public void addUser(User user){
        userRepository.save(user);
    }

    // To get a particular User Entry by userName from our database
    public User getUserByUserName(String userName){
        return userRepository.findByUserName(userName);
    }

    // To update an existing User Entry in our database
    public void updateUserEntry(User user){
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDb = userRepository.findByUserName(userName);
        if(userInDb != null){
            userInDb.setUserName(user.getUserName());
            if(user.getPassword() != null && !user.getPassword().isEmpty()){
                userInDb.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userRepository.save(userInDb);
        }

    }

    //To delete an existing User Entry in our database
    public void deleteUserEntry(){
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        userRepository.deleteByUserName(userName);
    }


}
