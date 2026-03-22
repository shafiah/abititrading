package com.abiti_app_service.service;

import java.util.List;

import com.abiti_app_service.models.Files;
import com.abiti_app_service.models.Users;

public interface FilesService {

	Files saveFile(Files files);
    Files findByFileName(String originalFilename);
	List<Files> findByFileType(String fileType);
	List<Files> findByFileTypeAndPaid(String fileType, boolean paid);
	void deleteFile(Long id);
	Files updateFile(Long id, Boolean paid);


}
