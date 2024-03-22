package com.exam.service.impl;

import com.exam.config.EmailUtils;
import com.exam.config.JwtAuthenticationFilter;
import com.exam.config.JwtUtils;
import com.exam.helper.UserFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.exam.helper.UserNotFoundException;
import com.exam.model.Role;
import com.exam.model.User;
import com.exam.model.UserRole;
import com.exam.repo.RoleRepository;
import com.exam.repo.UserRepository;
import com.exam.service.UserService;

import org.apache.logging.log4j.util.Strings;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {

	private static final long EXPIRE_TOKEN_AFTER_MINUTES = 5;

	
	
	
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    private JwtAuthenticationFilter jwtFilter;
    
    @Autowired
    private EmailUtils emailUtils;

//	HttpSession session;
//	
//	Random random = new Random();
//	int otp = random.nextInt(9999);
//	String otp1= Integer.toString(otp);

    //creating user
    @Override
    public User createUser(User user, Set<UserRole> userRoles) throws Exception {


        User local = this.userRepository.findByUsername(user.getUsername());
        if (local != null) {
            System.out.println("User is already there !!");
            throw new UserFoundException();
        } else {
            //user create
            for (UserRole ur : userRoles) {
                roleRepository.save(ur.getRole());
            }

            user.getUserRoles().addAll(userRoles);
            local = this.userRepository.save(user);

        }

        return local;
    }

    //getting user by username
    @Override
    public User getUser(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public void deleteUser(Long userId) {
        this.userRepository.deleteById(userId);
    }

	@Override
	public User forgotPassword(Map<String, String> reMap) throws UserNotFoundException {
		Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(reMap.get("email")));
		User user = new User();
//		user= userRepository.findByEmail(reMap.get("email"));
		if(!userOptional.isPresent()) {
			throw new UserNotFoundException();
		}else {
//			User user = userOptional.get();
			user.setToken(generateToken());
			user.setTokenCreationDate(LocalDateTime.now());
			user= userRepository.save(user);
			return user;
		}
		
	}

	

	@Override
	public User resetPassword( Map<String,String> reMap) throws Exception {
		Optional<User> userOptional = Optional.ofNullable(userRepository.findByToken(reMap.get("token")));
		if(!userOptional.isPresent()) {
			throw new UserNotFoundException();
		}
		LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();
		
		if(isTokenExpired(tokenCreationDate)) {
			  throw new Exception("Linked is Expired");
 
		}
		
		User user = userOptional.get();
		user.setPassword(this.bCryptPasswordEncoder.encode(reMap.get("password")));
		user.setToken(null);
		user.setTokenCreationDate(null);
		userRepository.save(user);
		return user;
	}
	
	private String generateToken() {
		StringBuilder token = new StringBuilder();

		return token.append(UUID.randomUUID().toString())
				.append(UUID.randomUUID().toString()).toString();
	}
	
	private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {

		LocalDateTime now = LocalDateTime.now();
		Duration diff = Duration.between(tokenCreationDate, now);

		return diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;
	}

	@Override
	public ResponseEntity<?> changePassword(Map<String, String> requestMap) {

		
			User userObj = userRepository.findByUsername(jwtFilter.getCurrentUser());
			System.out.println(userObj.getPassword());
			System.out.println(requestMap.get("oldPassword"));
			if(!userObj.equals(null)) {
				if(this.bCryptPasswordEncoder.matches(requestMap.get("oldPassword"), userObj.getPassword())) {
					userObj.setPassword(this.bCryptPasswordEncoder.encode(requestMap.get("newPassword")));
					userRepository.save(userObj);
					System.out.println(userObj);
					return ResponseEntity.ok("Password updated successfully..");

				}
			}
			
		return ResponseEntity.ok("Something went wrong");
	}

//	@Override
//	public ResponseEntity<?> forgotPassword(Map<String, String> requestMap) {
//		try {
//			
//			
////			String otp1 = Integer.toString(otp);
//
//			User user = userRepository.findByEmail(requestMap.get("email"));
////			System.out.println(otp);
//			
//			System.out.println(requestMap.get("email"));
////			System.out.println(user.getEmail());
//			System.out.println("_________________");
//			if(!Objects.isNull(user) && !Strings.isEmpty(user.getEmail())) {
////				System.out.println(user.getEmail());
//				user.setToken(otp1);
//				map.put(user.getEmail(), otp1);
//				user= userRepository.save(user);
//				System.out.println(user.getToken());
//				
////						session.setAttribute("token", user.getToken());
//
//				emailUtils.forgotMail(user.getEmail(), "Credential are", otp1);
//			}
//			return ResponseEntity.ok("OTP sent successfully to your email");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return ResponseEntity.ok("Something went Wrong!!!");
//	}

	@Override
	public ResponseEntity<?> verifyOtp(Map<String, String> requestMap) {
		
		User user = new User();
		System.out.println(user.getToken());
		 user= userRepository.findByToken(requestMap.get("otp"));
		System.out.println(requestMap.get("otp")); 
		System.out.println(user.getToken());
		if(user.getToken().equals(requestMap.get("otp"))) {
//			user.setToken(null);
//			userRepository.save(user);
//			session.setAttribute("token", requestMap.get("otp"));
			return ResponseEntity.ok("Otp is verify");
			
		}
		else {
			return ResponseEntity.ok("Invalid otp");
		}
//		if(!userOptional.isPresent()) {
//			return ResponseEntity.ok("Invalid OTP");
//		}
//
//			User user = userOptional.get();
////			user.setPassword(this.bCryptPasswordEncoder.encode(password));
//			user.setToken(null);
//			user.setTokenCreationDate(null);
//			userRepository.save(user);
////			return "Your password successfully updated.";
			
	}

//	@Override
//	public ResponseEntity<?> resetPassword(Map<String, String> requestMap) {
//		
//		
////			String otp1 = Integer.toString(otp);
//			User user = new User();
////			String token = (String) session.getAttribute("token");
////			userRepository.findByEmail(otp1)
//			 user= userRepository.findByToken(otp1);
//			 if(!user.getToken().isEmpty()) {
//			user.setPassword(this.bCryptPasswordEncoder.encode(requestMap.get("password")));
//			user.setToken(null);
//			session.removeAttribute(otp1);
//			userRepository.save(user);
//			return ResponseEntity.ok("Password updated");
//	
//			}
//			 return ResponseEntity.badRequest().body("Something went wrong");
//			 
//	}





	
	

}
