package com.ty.spring.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ty.spring.Dao.Cardao;

@Service
public class CarService {
	@Autowired
	private Cardao Dao;
	
	public List <Car> getAllCar(){
		List<Car> car=(List<Car>)Dao.findAll();
		return car;
	}
	public Car addCar(Car c) {
		Car result = Dao.save(c);
		return result;
		
	}
	public void deleteCar(int id) {
		Dao.deleteById(id);
	}
	public Car updateCar (Car car1, int cid) {
		car1.setId(cid);
		Dao.save(car1);
		return car1;
	}

}
