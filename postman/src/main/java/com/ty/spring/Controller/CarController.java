package com.ty.spring.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarController {
	@Autowired
	private CarService service;
	
	@GetMapping("/cars")
	public String getCars() {
		return "sending for testing purpose";
	}
	@GetMapping("/car")
	public Car getCar() {
		Car c= new Car();
		c.setId(101);
		c.setBrand("BMW");
		c.setCost(343434.0);
		c.setColor("black");
		return c;
	}
	
	@Autowired
	private CarService services;
	
	@GetMapping("/allcars")
	
	public ResponseEntity<List<Car>>getAllCar(){
		List<Car>list=service.getAllCar();
		if(list.size()<=0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			
		}
		else {
			return ResponseEntity.of(Optional.of(list));
		}
	}
	  @PostMapping("/cars")
	    public Car addCar(@RequestBody Car car) {
	        Car c = this.service.addCar(car);
	        return c;
	    }

	    // 5️⃣ Delete a car by ID
	    @DeleteMapping("/car/{id}")
	    public ResponseEntity<Void> deleteCar(@PathVariable("id") int id) {
	        try {
	            this.service.deleteCar(id);
	            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	    }

	    // 6️⃣ Update a car by ID
	    @PutMapping("/car/{cid}")
	    public Car updateCar(@RequestBody Car car, @PathVariable("cid") int cid) {
	        this.service.updateCar(car, cid);
	        return car;
	    }
	}




