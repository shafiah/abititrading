package com.abiti_app_service.models;

import java.io.Serializable;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "Users")
@Table(name = "USERS")
@Getter
@Setter
public class Users implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "USER_NAME")
	private String userName;
	
	@Column(name = "EMAIL_ID")
	private String emailId;
	
	@Column(name = "PHONE_NUMBER")
	private String 	phoneNumber;
	
	@Column(name = "DEVICE_ID")
	private String 	deviceId;
	
	@Column(name = "PRIME")
	private boolean prime;

	@Column(name = "USER_TYPE")
	private String 	userType;
	
	@Column(name = "USER_STATUS")
	private String 	userStatus;
	
	@Column(name = "PASSWORD")
	private String password;
	
	@Column(name = "LOGIN_VERSION")
	private Long loginVersion;

	
}
