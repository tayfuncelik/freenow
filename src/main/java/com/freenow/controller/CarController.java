package com.freenow.controller;

import com.freenow.controller.mapper.CarMapper;
import com.freenow.datatransferobject.CarDTO;
import com.freenow.domainobject.CarDO;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;
import com.freenow.service.driver.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("v1/cars")
public class CarController
{
    private final CarService carService;


    @Autowired
    public CarController(CarService carService)
    {
        this.carService = carService;
    }


    @GetMapping("/{carId}")
    public CarDTO getCar(@PathVariable long carId) throws EntityNotFoundException
    {
        return CarMapper.makeCarDTO(carService.find(carId));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarDTO createCar(@Valid @RequestBody CarDTO carDTO) throws ConstraintsViolationException
    {
        CarDO carDO = CarMapper.makeCarDO(carDTO);
        return CarMapper.makeCarDTO(carService.create(carDO));
    }


    @DeleteMapping("/{carId}")
    public ResponseEntity<HttpStatus> deleteCar(@PathVariable long carId) throws EntityNotFoundException
    {
        carService.delete(carId);
        return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
    }


    @GetMapping
    public List<CarDTO> findCars()
    {
        return CarMapper.makeCarDTOList(carService.findAll());
    }


    @PutMapping("/{carId}")
    @ResponseStatus(HttpStatus.OK)
    public CarDTO updateCar(@PathVariable("carId") long carId, @Valid @RequestBody CarDTO carDTO) throws EntityNotFoundException
    {
        CarDO carDO = CarMapper.makeCarDO(carDTO);
        carService.update(carDO);
        return carDTO;
    }

}
