package com.ty.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.ty.spring.dao.UserDao;
import com.ty.spring.entity.User;

@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(DemoApplication.class, args);
		UserDao dao = context.getBean(UserDao.class);
		
		User user=new User();
		user.setId(103);
		
		user.setName("Madhav");
		
		user.setAge(19);
		
		user.setGender("Male");
		
		user.setPhoneNo(94008087);
		User user2=dao.save(user);
		
		System.out.println(user2);
	}

}
