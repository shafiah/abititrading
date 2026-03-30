package com.abiti_app_service.controllers;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.abiti_app_service.models.Files;
import com.abiti_app_service.models.ResponseModel;
import com.abiti_app_service.service.FilesService;
import com.abiti_app_service.util.CONSTANTS;

@RestController
@RequestMapping("/file")
public class FileUploadController {

	@Autowired
	private FilesService filesService;

	@PostMapping(value = "/img/upload")
	public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file,
			@RequestParam("paid") Boolean paid,@RequestParam("title") String title) throws Exception {

		try {

			String fileName = file.getOriginalFilename();

			if (fileName != null && fileName.toLowerCase().endsWith(".jpg")) {

				// create folder if not exists
				File directory = new File(CONSTANTS.IMG_UPLOAD_DIR);
				if (!directory.exists()) {
					directory.mkdirs();
				}

				// ===== NEW : unique file name generate =====
				String newFileName = System.currentTimeMillis() + "_" + fileName;

				// file path
				String filePath = directory.getAbsolutePath() + File.separator + newFileName;

				// ===== OLD CODE REMOVED =====
				// Files existFiles= filesService.findByFileName(file.getOriginalFilename());
				// if(existFiles!=null) {
				// throw new Exception("File already exists");
				// }

				// save file
				file.transferTo(new File(filePath));

				Files files = new Files();
				files.setFileType(CONSTANTS.FILE_TYPE_JPG);

				// ===== SAVE NEW FILE NAME IN DB =====
				files.setFileName(newFileName);

				files.setPaid(paid);
				files.setTitle(title);

				filesService.saveFile(files);

				// return ResponseEntity.ok("Image uploaded successfully");
				return ResponseEntity.ok("{\"message\":\"Image uploaded successfully\"}");

			} else {
				throw new Exception("Only JPG file allowed");
			}
		} catch (IOException e) {
			return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
		}
	}

	@PostMapping(value = "/pdf/upload")
	public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file,
			@RequestParam("paid") Boolean paid,@RequestParam("title") String title) throws Exception {

		try {

			String fileName = file.getOriginalFilename();

			if (fileName != null && fileName.toLowerCase().endsWith(".pdf")) {

				// create folder if not exists
				File directory = new File(CONSTANTS.PDF_UPLOAD_DIR);
				if (!directory.exists()) {
					directory.mkdirs();
				}

				// ===== NEW : unique file name =====
				String newFileName = System.currentTimeMillis() + "_" + fileName;

				// file path
				String filePath = directory.getAbsolutePath() + File.separator + newFileName;

				// ===== OLD CODE REMOVED =====
//			 Files existFiles= filesService.findByFileName(file.getOriginalFilename());  
//			 if(existFiles!=null) {  
//				throw new Exception("File already exists");  
//			 }  

				// save file
				file.transferTo(new File(filePath));

				Files files = new Files();
				files.setFileType(CONSTANTS.FILE_TYPE_PDF);

				// ===== SAVE NEW NAME =====
				files.setFileName(newFileName);

				files.setPaid(paid);
				files.setTitle(title);

				filesService.saveFile(files);

				// return ResponseEntity.ok("pdf uploaded successfully");
				return ResponseEntity.ok("{\"message\":\"pdf uploaded successfully\"}");
			} else {
				throw new Exception("Only pdf file allowed");
			}
		} catch (IOException e) {
			return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
		}
	}

//🔥 CHANGE 1: Return type String se ResponseModel kar diya
	@PostMapping(value = "/vid/upload")
	public ResponseEntity<ResponseModel> uploadVideo(@RequestParam("file") MultipartFile file,
			@RequestParam("paid") Boolean paid,@RequestParam("title") String title) {

		try {

			String fileName = file.getOriginalFilename();

			if (fileName == null) {
				// 🔥 CHANGE 2: String ke jagah ResponseModel return
				return ResponseEntity.badRequest().body(new ResponseModel("Invalid file", null));
			}

			// ensure mp4 extension
			if (!fileName.toLowerCase().endsWith(".mp4")) {
				fileName = fileName + ".mp4";
			}

			File directory = new File(CONSTANTS.VID_UPLOAD_DIR);

			if (!directory.exists()) {
				directory.mkdirs();
			}

			String newFileName = System.currentTimeMillis() + "_" + fileName;

			String filePath = directory.getAbsolutePath() + File.separator + newFileName;

			// ❌ Duplicate check already removed (correct)

			// save file
			file.transferTo(new File(filePath));

			Files files = new Files();
			files.setFileType(CONSTANTS.FILE_TYPE_MP4);
			files.setFileName(newFileName);

			// 🔥 IMPORTANT: paid flag save ho raha hai
			files.setPaid(paid);
			files.setTitle(title);

			System.out.println("PAID VALUES :" + paid);

			filesService.saveFile(files);

			// 🔥 CHANGE 3: Proper success response object
			return ResponseEntity.ok(new ResponseModel(null, "Video uploaded successfully"));

		} catch (Exception e) {

			System.out.println("ERROR :" + e.getMessage());

			// 🔥 CHANGE 4: Proper error response
			return ResponseEntity.internalServerError()
					.body(new ResponseModel("Upload failed : " + e.getMessage(), null));
		}
	}
}