package com.ty.spring.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class User {
	@Id
	private String name;
	private int age;
	private String gender;
	private long phoneNo;
	
	
	
	
	@Override
	public String toString() {
		return "User [name=" + name + ", age=" + age + ", gender=" + gender + ", phoneNo=" + phoneNo + ", id=" + id
				+ "]";
	}
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public long getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(long phoneNo) {
		this.phoneNo = phoneNo;
	}
	public User(String name, int age, String gender, long phoneNo, int id) {
		super();
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.phoneNo = phoneNo;
		this.id = id;
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}



