package com.abiti_app_service.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.abiti_app_service.models.Files;
import com.abiti_app_service.models.Users;

@Repository
public interface FilesDao extends JpaRepository<Files, Long> {

	Files findByFileName(String originalFilename);
	List<Files> findByFileType(String fileType);
	List<Files> findByFileTypeAndPaid(String fileType, boolean paid);

    

}
