package com.abiti_app_service.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.abiti_app_service.models.Users;

@Repository
public interface UsersDao extends  JpaRepository<Users, Long> {

	Users findByPhoneNumber(String phoneNumber);

	List<Users> findByUserType(String type);
	

}
