package com.abiti_app_service.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "Files")
@Table(name = "FILES")
@Getter
@Setter
public class Files implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "FILE_NAME")
	private String fileName;

	@Column(name = "FILE_TYPE")
	private String fileType;
	
	@Column(name = "PAID")
	private boolean paid;
	
	@CreationTimestamp
	@Column(name = "UPLOAD_DATE")
	private LocalDateTime uploadDate;
	
	
}
