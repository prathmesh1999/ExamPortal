package com.exam.service;

import com.exam.helper.UserNotFoundException;
import com.exam.model.User;
import com.exam.model.UserRole;

import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;

public interface UserService {

    //creating user
    public User createUser(User user, Set<UserRole> userRoles) throws Exception;

    //get user by username
    public User getUser(String username);

    //delete user by id
    public void deleteUser(Long userId);
     
    public User forgotPassword(Map<String, String> reMap) throws UserNotFoundException;
    
//    ResponseEntity<?> forgotPassword(Map<String,String> requestMap);

    public User resetPassword( Map<String,String> reMap) throws Exception;
    
    ResponseEntity<?> changePassword(Map<String,String> requestMap);
    
    ResponseEntity<?> verifyOtp(Map<String,String> requestMap);
    
//    ResponseEntity<?> resetPassword(Map<String,String> requestMap);

    
    
}
