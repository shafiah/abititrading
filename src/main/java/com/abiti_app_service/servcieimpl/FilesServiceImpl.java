package com.abiti_app_service.servcieimpl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abiti_app_service.dao.FilesDao;
import com.abiti_app_service.models.Files;
import com.abiti_app_service.service.FilesService;
import com.abiti_app_service.util.CONSTANTS;

@Transactional
@Service
public class FilesServiceImpl implements FilesService {

	@Autowired
	private FilesDao filesDao;

	@Override
	public Files saveFile(Files files) {
		// TODO Auto-generated method stub
		return filesDao.save(files);
	}

	@Override
	public Files findByFileName(String originalFilename) {
		// TODO Auto-generated method stub
		return filesDao.findByFileName(originalFilename);
	}

	@Override
	public List<Files> findByFileType(String fileType) {
		// TODO Auto-generated method stub
		return filesDao.findByFileType(fileType);
	}

	@Override
	public List<Files> findByFileTypeAndPaid(String fileType, boolean paid) {
		// TODO Auto-generated method stub
		return filesDao.findByFileTypeAndPaid(fileType, paid);
	}

	@Override
	public void deleteFile(Long id) {
		// TODO Auto-generated method stub
		Files file = filesDao.findById(id).orElseThrow(() -> new RuntimeException("File not found"));

		String filePath = "";

		if (file.getFileType().equalsIgnoreCase(CONSTANTS.FILE_TYPE_JPG)) {
			filePath=CONSTANTS.IMG_UPLOAD_DIR+""+file.getFileName();
		} else if (file.getFileType().equalsIgnoreCase(CONSTANTS.FILE_TYPE_PDF)) {
			filePath=CONSTANTS.PDF_UPLOAD_DIR+""+file.getFileName();
		} else if (file.getFileType().equalsIgnoreCase(CONSTANTS.FILE_TYPE_MP4)) {
			filePath=CONSTANTS.VID_UPLOAD_DIR+""+file.getFileName();
		}

		File deleteFilePath = new File(filePath);
		if (deleteFilePath.exists()) {
			if (deleteFilePath.delete()) {
				System.out.println("File deleted successfully");
			} else {
				System.out.println("Failed to delete file");
			}
		} else {
			System.out.println("File not found");
		}

		filesDao.deleteById(id);
	}

	@Override
	public Files updateFile(Long id, Boolean paid) {
		// TODO Auto-generated method stub

		Files file = filesDao.findById(id).orElseThrow(() -> new RuntimeException("File not found"));
		file.setPaid(paid);
		return filesDao.save(file);
	}
	
	@Override
	public List<Files> getRecentFiles() {
	    return filesDao.getLast24HoursFiles();
	}

	

}
