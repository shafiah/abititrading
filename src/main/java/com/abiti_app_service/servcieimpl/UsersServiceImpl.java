package com.abiti_app_service.servcieimpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abiti_app_service.dao.OtpRepository;
import com.abiti_app_service.dao.UsersDao;
import com.abiti_app_service.models.OtpEntity;
import com.abiti_app_service.models.Users;
import com.abiti_app_service.service.UsersServcie;

@Service
@Transactional
public class UsersServiceImpl implements UsersServcie {

	@Autowired
	private UsersDao usersDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder; // ⭐ NEW
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private OtpRepository otpRepository;
	

	@Override
	public Users findByPhoneNumber(String phoneNumber) {
		// TODO Auto-generated method stub
		return usersDao.findByPhoneNumber(phoneNumber);
	}

	@Override
	public Users saveUser(Users user) throws Exception {
		// TODO Auto-generated method stub

		Users findUser = usersDao.findByPhoneNumber(user.getPhoneNumber());
		
		// EMAIL ALREADY EXISTS CHECK
		if (user.getEmailId() != null &&
		        !user.getEmailId().trim().isEmpty()) {

		    Users emailUser =
		            usersDao.findByEmailId(
		                    user.getEmailId());

		    if (emailUser != null) {

		        // SAME MOBILE + SAME EMAIL
		        if (findUser != null &&
		                emailUser.getId().equals(findUser.getId())) {

		            // same user, allow update
		        } else {

		            throw new Exception(
		                    "Email already registered. Please use another email.");
		        }
		    }
		}

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
	        findUser.setEmailId(user.getEmailId());
	        findUser.setPassword(passwordEncoder.encode(user.getPassword()));
	        findUser.setDeviceId(user.getDeviceId());

	        return usersDao.save(findUser);
	    }

	    // NEW USER → CREATE
	    user.setPassword(passwordEncoder.encode(user.getPassword()));
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

	    
	    if (!passwordEncoder.matches(password, user.getPassword())) {
	        throw new Exception("Invalid credential");
	    }
	
	 // Google Play Review Account - Skip Device Validation
	    if (!phoneNumber.equals("7828103669")) {

	        if (!user.getDeviceId().equals(deviceId)) {
	            throw new Exception("This device isn't registered with this number. Please register device.");
	        }

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

	   // otpStorage.put(emailId, otp);
	    OtpEntity otpEntity =
	            otpRepository.findByEmailId(emailId);
	    if (otpEntity == null) {

	        otpEntity = new OtpEntity();

	        otpEntity.setCreatedAt(
	                LocalDateTime.now());
	    }

	    otpEntity.setEmailId(emailId);

	    otpEntity.setOtp(otp);

	    otpEntity.setVerified(false);

	    otpEntity.setExpiryTime(
	            LocalDateTime.now().plusMinutes(10));

	    otpRepository.save(otpEntity);

	    SimpleMailMessage message =
	            new SimpleMailMessage();

	    message.setTo(emailId);

	    message.setSubject(
	            "ABITI TRADING ZONE - Password Reset OTP");

	    message.setText(
	            "ABITI Trading Zone\n\n" +

	            "Your OTP is: " + otp +

	            "\n\nThis OTP is valid for 10 minutes only." +

	            "\n\nDo not share this OTP with anyone." +

	            "\n\nIf you did not request a password reset, please ignore this email." +

	            "\n\nThank you," +
	            "\nABITI Trading Zone Team"
	    );
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

	    OtpEntity otpEntity =
	            otpRepository.findByEmailId(emailId);

	    if (otpEntity == null) {

	        throw new Exception(
	                "OTP not found");
	    }

	    if (otpEntity.isVerified()) {

	        throw new Exception(
	                "OTP already used");
	    }

	    if (LocalDateTime.now()
	            .isAfter(
	                    otpEntity.getExpiryTime())) {

	        throw new Exception(
	                "OTP expired");
	    }

	    if (!otp.equals(
	            otpEntity.getOtp())) {

	        throw new Exception(
	                "Invalid OTP");
	    }

	    user.setPassword(
	            passwordEncoder.encode(
	                    newPassword));

	    usersDao.save(user);
	    
	    otpEntity.setVerified(true);

	    otpRepository.save(otpEntity);

	    //otpStorage.remove(emailId);

	    return "Password reset successfully";
	}
}
