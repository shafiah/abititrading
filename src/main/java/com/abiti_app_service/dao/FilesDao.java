package com.abiti_app_service.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.abiti_app_service.models.Files;
import com.abiti_app_service.models.Users;

@Repository
public interface FilesDao extends JpaRepository<Files, Long> {

	Files findByFileName(String originalFilename);
	List<Files> findByFileType(String fileType);
	List<Files> findByFileTypeAndPaid(String fileType, boolean paid);
	
	
	// ⭐ NEW: LAST 24 HOURS FILES
	@Query(value = "SELECT * FROM FILES WHERE UPLOAD_DATE >= DATEADD('HOUR', -24, CURRENT_TIMESTAMP()) ORDER BY UPLOAD_DATE DESC", nativeQuery = true)
	List<Files> getLast24HoursFiles();
	
    

}
