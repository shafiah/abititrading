package com.abiti_app_service.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abiti_app_service.models.Files;
import com.abiti_app_service.models.Users;
import com.abiti_app_service.service.FilesService;
import com.abiti_app_service.service.UsersServcie;

@RestController
@RequestMapping("/file")
public class FileController {

	@Autowired
	private FilesService filesService;
	
	@Autowired
	private UsersServcie usersServcie;
	
	@GetMapping("/get/list/{fileType}/{phoneNumber}")
	public ResponseEntity<List<Files>> getFileListByType(
			@PathVariable String fileType,
			@PathVariable String phoneNumber){
	
	     List<Files> fileList= null;
		 Users user= usersServcie.findByPhoneNumber(phoneNumber);
		 
		 if(user!=null && user.isPrime()) {
		    fileList = filesService.findByFileType(fileType);
		 }else {
			fileList = filesService.findByFileTypeAndPaid(fileType,false);
		 }
		 
      return ResponseEntity.ok().body(fileList);
	}
	
	@PostMapping("/update-file/{paid}/{id}")
    public ResponseEntity<Files> createUser(
    		@PathVariable Boolean paid,
    		@PathVariable Long id) {
        Files updatedFile= filesService.updateFile(id,paid);

        return ResponseEntity.ok().body(updatedFile);
    }
	
	@DeleteMapping("/delete/file/{id}")
    public ResponseEntity<String>  deleteFile(@PathVariable Long id) {
		filesService.deleteFile(id);
        return ResponseEntity.ok().body("File deleted successfully");
    }

}
