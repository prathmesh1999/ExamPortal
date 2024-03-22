package com.exam.controller;

import com.exam.config.EmailUtils;
import com.exam.config.JwtUtils;
import com.exam.helper.UserFoundException;
import com.exam.helper.UserNotFoundException;
import com.exam.model.Role;
import com.exam.model.User;
import com.exam.model.UserRole;
import com.exam.repo.UserRepository;
import com.exam.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    
    @Autowired
    private EmailUtils emailUtils;

    //creating user
    @PostMapping("/")
    public User createUser(@RequestBody User user) throws Exception {


        user.setProfile("default.png");
        
        //encoding password with bcryptpasswordencoder this.bCryptPasswordEncoder.encode
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));

        Set<UserRole> roles = new HashSet<>();

        Role role = new Role();
        role.setRoleId(45L);
        role.setRoleName("NORMAL");

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);

        roles.add(userRole);


        return this.userService.createUser(user, roles);

    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable("username") String username) {
        return this.userService.getUser(username);
    }

    //delete the user by id
    @DeleteMapping("/resetPassword/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        this.userService.deleteUser(userId);
    }


    //update api


    @ExceptionHandler(UserFoundException.class)
    public ResponseEntity<?> exceptionHandler(UserFoundException ex) {
        return ResponseEntity.ok(ex.getMessage());
    }

    @PostMapping("/forgotPassword")
	public User forgotPassword(@RequestBody Map<String, String> reMap ) throws MessagingException, UserNotFoundException {
    	System.out.println(reMap);
//    	String response = userService.forgotPassword(reMap);
    	User user = userService.forgotPassword(reMap);
    	String response = user.getToken();
    	if(user!=null) {
    		response = "http://localhost:4200/reset-password?token=" + response;
    		emailUtils.forgotMail(reMap.get("email"), " Password change ", response);
    	}
    		
    	
		return user;
	}
    

    @PutMapping("/reset-password")
    
    public User resetPassword( @RequestBody Map<String,String> reMap) throws Exception{
    
    		 
		
		return this.userService.resetPassword(reMap);
             
    }
    
    
    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody Map<String,String> requestMap){
		
    	return userService.changePassword(requestMap);
    	
    }
    
//    @PostMapping("/forgotPassword")
//    ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> requestMap){
//		return userService.forgotPassword(requestMap);
//
//    	
//    }
//    
//    @PostMapping("/otp-verify")
//    ResponseEntity<?> verifyOtp(@RequestBody Map<String ,String> requestMap){
//    	return userService.verifyOtp(requestMap);
//    }
//    
//    @PostMapping("/resetPassword")
//    public ResponseEntity<?> resetPassword(@RequestBody Map<String,String> requestMap){
//		
//    	return userService.resetPassword(requestMap);
//    	
//    }
		
}


