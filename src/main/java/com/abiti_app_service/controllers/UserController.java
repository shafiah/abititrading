package com.abiti_app_service.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abiti_app_service.models.Users;
import com.abiti_app_service.service.UsersServcie;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UsersServcie usersServcie;
	
	@GetMapping("/home")
	public String homePage() {
		return "welcome to ABITI Tranding Investment apps";
	}

    @PostMapping("/create")
    public ResponseEntity<Users> createUser(@RequestBody Users user) throws Exception {
        Users userSaved= usersServcie.saveUser(user);
        return ResponseEntity.ok().body(userSaved);
    }
	
    @GetMapping("/get-users-list/{type}")
    public ResponseEntity<List<Users>> getUsersListByType(
    		@PathVariable String type ) {
        List<Users> userList= usersServcie.findByUserType(type);
        return ResponseEntity.ok().body(userList);
    }
    
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String>  deleteUser(@PathVariable Long id) {
    	usersServcie.deleteUser(id);
        return ResponseEntity.ok().body("User deleted successfully");
    }
    
    @PutMapping("/update-user-plan/{id}/{prime}")
    public Users updateUserPlanById(
            @PathVariable Long id,
            @PathVariable Boolean prime) throws Exception {
        return usersServcie.updateUser(id,prime);
    }
    
    
    // Used to show correct pop up error
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Users request) {

        try {

            Users response = usersServcie.loginUser(
                    request.getPhoneNumber(),
                    request.getPassword(),
                    request.getDeviceId()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
    
    
    // Correct Code
//    @PostMapping("/login")
//    public ResponseEntity<?> loginUser(@RequestBody Users user) throws Exception {
//
//        Users loginUser = usersServcie.loginUser(
//                user.getPhoneNumber(),
//                user.getPassword(),
//                user.getDeviceId()
//        );
//
//        return ResponseEntity.ok().body(loginUser);
//    }
	
}

