package com.abiti_app_service.servcieimpl;

import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abiti_app_service.dao.UsersDao;
import com.abiti_app_service.models.Users;
import com.abiti_app_service.service.UsersServcie;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
@Transactional
public class UsersServiceImpl implements UsersServcie {

	@Autowired
	private UsersDao usersDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder; // ⭐ NEW
	
	@Autowired
	private JavaMailSender mailSender;
	
	private final ConcurrentHashMap<String, String> otpStorage = new ConcurrentHashMap<>();

	@Override
	public Users findByPhoneNumber(String phoneNumber) {
		// TODO Auto-generated method stub
		return usersDao.findByPhoneNumber(phoneNumber);
	}

	@Override
	public Users saveUser(Users user) throws Exception {
		// TODO Auto-generated method stub

		Users findUser = usersDao.findByPhoneNumber(user.getPhoneNumber());

	    // USER ALREADY EXISTS
	    if (findUser != null) {

	        // SAME DEVICE → ALREADY REGISTERED
	        if (findUser.getDeviceId() != null 
	                && findUser.getDeviceId().equals(user.getDeviceId())) {

	            throw new Exception("User already registered with this mobile number");
	        }

	        // NEW DEVICE → UPDATE USER
	        findUser.setUserName(user.getUserName());
	       // findUser.setPassword(user.getPassword());
	        findUser.setPassword(passwordEncoder.encode(user.getPassword()));
	        findUser.setDeviceId(user.getDeviceId());

	        return usersDao.save(findUser);
	    }

	    // NEW USER → CREATE
	    user.setPassword(passwordEncoder.encode(user.getPassword()));
	    // New Code added june 2026
	    user.setEmailId(user.getEmailId());
	    return usersDao.save(user);
	}

	@Override
	public List<Users> findByUserType(String type) {
		// TODO Auto-generated method stub
		return usersDao.findByUserType(type);
	}

	@Override
	public void deleteUser(Long id) {
		// TODO Auto-generated method stub
		usersDao.deleteById(id);
	}

	@Override
	public Users updateUser(Long id, boolean prime) throws Exception {
		// TODO Auto-generated method stub
		Users user = usersDao.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
		if (user == null) {
			throw new Exception("User not found.");
		}
		user.setPrime(prime);
		return usersDao.save(user);
	}
	@Override
	public Users loginUser(String phoneNumber, String password, String deviceId) throws Exception {

	    Users user = usersDao.findByPhoneNumber(phoneNumber);

	    if (user == null) {
	        throw new Exception("Invalid credential");
	    }

	   // if (!user.getPassword().equals(password)) {
	     //   throw new Exception("Invalid credential");
	   // }
	    
	    if (!passwordEncoder.matches(password, user.getPassword())) {
	        throw new Exception("Invalid credential");
	    }
	    if (!user.getDeviceId().equals(deviceId)) {
	        throw new Exception("This device isn't registered with this number. Please register device.");
	    }

	    return user;

    
	}
	
	@Override
	public Users findUserById(Long id) {

	    return usersDao.findById(id)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	}
	
	
	@Override
	public Users updateUserDetails(Long id, Users updatedUser) throws Exception {

	    Users user = usersDao.findById(id)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    // UPDATE FIELDS (ONLY REQUIRED)
	    user.setUserName(updatedUser.getUserName());
	  //  user.setPhoneNumber(updatedUser.getPhoneNumber());

	    return usersDao.save(user);
	}
	
	@Override
	public String sendForgotPasswordOtp(String emailId)
	        throws Exception {

	    Users user = usersDao.findByEmailId(emailId);

	    if (user == null) {
	        throw new Exception("Email not registered");
	    }

	    String otp = String.valueOf(
	            100000 + new Random().nextInt(900000));

	    otpStorage.put(emailId, otp);

	    SimpleMailMessage message =
	            new SimpleMailMessage();

	    message.setTo(emailId);

	    message.setSubject(
	            "ABITI TRADING ZONE - Password Reset OTP");

	    message.setText(
	            "Your OTP is : "
	            + otp
	            + "\n\nValid for password reset.");

	    mailSender.send(message);

	    return "OTP sent successfully";
	}
	
	@Override
	public String resetPassword(
	        String emailId,
	        String otp,
	        String newPassword)
	        throws Exception {

	    Users user =
	            usersDao.findByEmailId(emailId);

	    if (user == null) {
	        throw new Exception(
	                "User not found");
	    }

	    String savedOtp =
	            otpStorage.get(emailId);

	    if (savedOtp == null ||
	            !savedOtp.equals(otp)) {

	        throw new Exception(
	                "Invalid OTP");
	    }

	    user.setPassword(
	            passwordEncoder.encode(
	                    newPassword));

	    usersDao.save(user);

	    otpStorage.remove(emailId);

	    return "Password reset successfully";
	}
}
