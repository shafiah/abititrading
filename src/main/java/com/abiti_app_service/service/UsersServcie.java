package com.abiti_app_service.service;

import java.util.List;

import com.abiti_app_service.models.Users;

public interface UsersServcie  {

	public Users findByPhoneNumber(String phoneNumber);

	public Users saveUser(Users user) throws Exception;

	public List<Users> findByUserType(String type);

	public void deleteUser(Long id);

	public Users updateUser(Long id,boolean prime) throws Exception;
	
	public Users loginUser(String phoneNumber, String password, String deviceId) throws Exception;
}
