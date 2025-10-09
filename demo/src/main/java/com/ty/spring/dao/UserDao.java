package com.ty.spring.dao;
import org.springframework.data.repository.CrudRepository;

import com.ty.spring.entity.User;

public interface UserDao extends CrudRepository<User, String> {
	

}
